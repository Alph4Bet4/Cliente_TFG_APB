package Controladores;

import Modelos.*;
import TransformadorJSON.Actividad.TransformadorActividad;
import TransformadorJSON.Participacion.TransformadorParticipacion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.tfg_apb.tfg_apb_cliente.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FiltradoActividadesController implements Initializable {

    ArrayList<ActividadModel> actividadesDisponibles = new ArrayList<>();
    ArrayList<ActividadModel> actividadesFinalizadas = new ArrayList<>();
    ArrayList<ActividadModel> actividadesInscritas = new ArrayList<>();
    ArrayList<ActividadModel> actividadesCompletas = new ArrayList<>();

    @FXML
    private VBox actividadDisponibleLayout;

    @FXML
    private VBox actividadFinalizadaLayout;

    @FXML
    private VBox actividadesCompletasLayout;

    @FXML
    private VBox actividadesInscritasLayout;

    @FXML
    private ImageView imgRecarga;

    @FXML
    void recargarListas(MouseEvent event) {
        //Limpiamos los layouts
        actividadDisponibleLayout.getChildren().clear();
        actividadesCompletasLayout.getChildren().clear();
        actividadesInscritasLayout.getChildren().clear();
        actividadFinalizadaLayout.getChildren().clear();

        //Llenamos las listas
        actividadesDisponibles = llenarListaActividades("Disponible");
        actividadesFinalizadas = llenarListaActividades("Finalizado");
        actividadesCompletas = llenarListaActividades("Completo");
        actividadesDisponibles = llenarListaActividadesInscritas();

        //Rellenamos los layouts
        try {
            rellenarLayout(actividadesDisponibles, actividadDisponibleLayout);
            rellenarLayout(actividadesFinalizadas, actividadFinalizadaLayout);
            rellenarLayout(actividadesInscritas, actividadesInscritasLayout);
            rellenarLayout(actividadesCompletas, actividadesCompletasLayout);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void volverAtras(MouseEvent event) {
        try {
            Main.enviarrActividad(null);
            Main.setRaiz("VistaPrincipal");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cambiarTamScene();

        //Llenamos las listas
        actividadesDisponibles = llenarListaActividades("Disponible");
        actividadesFinalizadas = llenarListaActividades("Finalizado");
        actividadesCompletas = llenarListaActividades("Completo");
        actividadesInscritas = llenarListaActividadesInscritas();

        //Rellenamos los layouts
        try {
            rellenarLayout(actividadesDisponibles, actividadDisponibleLayout);
            rellenarLayout(actividadesFinalizadas, actividadFinalizadaLayout);
            rellenarLayout(actividadesInscritas, actividadesInscritasLayout);
            rellenarLayout(actividadesCompletas, actividadesCompletasLayout);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<ActividadModel> llenarListaActividades(String estado) {
        ArrayList<ActividadModel> listaActividades = new TransformadorActividad().recibirInformacionGet();
        ArrayList<ActividadModel> listaActividadesDevolver = new ArrayList<>();

        for (ActividadModel actividad : listaActividades) {
            if (actividad.getEstado().name().equals(estado)) {
                listaActividadesDevolver.add(actividad);
            }
        }


        return listaActividadesDevolver;
    }

    /**
     * Llena las actividades en la que estas inscrito
     *
     * @return
     */
    public ArrayList<ActividadModel> llenarListaActividadesInscritas() {
        ArrayList<ActividadModel> listaActividades = new ArrayList<>();
        //Nos aseguramos que sea un consumidor
        if (Main.recibirDatosUsuario() instanceof ConsumidorModel) {
            ArrayList<ParticipacionModel> listaParticipacion = new TransformadorParticipacion().recibirParticipacionPorIdUsuario(((ConsumidorModel) Main.recibirDatosUsuario()).getId_consumidor());

            //Insertamos las actividades de las participaciones
            for (ParticipacionModel participacion : listaParticipacion) {
                listaActividades.add(participacion.getActividad());
            }

        }
        return listaActividades;
    }


    /**
     * Metodo generico que rellena las listas
     *
     * @param lista
     * @param layoutRellenar
     * @throws IOException
     */
    private void rellenarLayout(List lista, VBox layoutRellenar) throws IOException {
        for (Object objeto : lista) {
            //Cargamos la vista
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Vistas/ActividadIndividual.fxml"));
            //Actualizamos el panel
            HBox hbox = fxmlLoader.load();
            ActividadIndividualController actividadIndividualController = fxmlLoader.getController();
            actividadIndividualController.introducirDatos((ActividadModel) objeto);
            //Lo añadimos
            layoutRellenar.getChildren().add(hbox);

        }
    }

    public void cambiarTamScene() {
        Main.cambiarTamVentana(1620, 900);
    }
}
