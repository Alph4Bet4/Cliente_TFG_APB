package Controladores;

import Modelos.ActividadModel;
import Modelos.ConsumidorModel;
import Modelos.OfertanteModel;
import Modelos.SugerenciaActividadModel;
import TransformadorJSON.Actividad.TransformadorActividad;
import TransformadorJSON.Sugerencia.TransformadorSugerencia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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

public class VistaPrincipalController implements Initializable {

    @FXML
    private VBox actividadLayout;

    @FXML
    private ImageView imgFiltro;

    @FXML
    private ImageView imgRecarga;

    @FXML
    private Label lblNombre;

    @FXML
    private VBox sugerenciaLayout;

    ArrayList<ActividadModel> listaActividades;
    ArrayList<SugerenciaActividadModel> listaSugerencias;

    @FXML
    void cambiarVistaActividad(ActionEvent event) {
        try {
            Main.setRaiz("VistaCrearActividad");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void filtrarActividades(MouseEvent event) {
        //TODO
    }

    @FXML
    void recargarListas(MouseEvent event) {
        sugerenciaLayout.getChildren().clear();
        actividadLayout.getChildren().clear();

        listaSugerencias = llenarListaSugerencias();
        listaActividades = llenarListaActividades();

        try {

            //Rellenamos la lista de actividades
            rellenarLayout(listaActividades, actividadLayout);

            //Rellenamos la lista de sugerencias
            rellenarLayout(listaSugerencias, sugerenciaLayout);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void verAjustesCuenta(MouseEvent event) {
        //TODO
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listaActividades = llenarListaActividades();
        listaSugerencias = llenarListaSugerencias();
        cambiarTamScene();
        Object usuario = Main.recibirDatosUsuario();

        if (usuario instanceof ConsumidorModel) {
            lblNombre.setText(lblNombre.getText() + ((ConsumidorModel) usuario).getNombreConsumidor());
        } else if (usuario instanceof OfertanteModel) {
            lblNombre.setText(lblNombre.getText() + ((OfertanteModel) usuario).getNombreOfertante());
        }

        try {

            //Rellenamos la lista de actividades
            rellenarLayout(listaActividades, actividadLayout);

            //Rellenamos la lista de sugerencias
            rellenarLayout(listaSugerencias, sugerenciaLayout);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
            if (objeto instanceof SugerenciaActividadModel) {
                if (((SugerenciaActividadModel) objeto).getActividad().getCreador_ofertante() == null) {
                    //Cargamos la vista
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Vistas/SugerenciaIndividual.fxml"));
                    //Actualizamos el panel
                    HBox hbox = fxmlLoader.load();
                    SugerenciaIndividualController sugerenciaIndividualController = fxmlLoader.getController();
                    sugerenciaIndividualController.rellenarDatos((SugerenciaActividadModel) objeto);
                    //Cambiamos su tamaño
                    layoutRellenar.setPrefHeight(layoutRellenar.getHeight() + 238);
                    //Lo añadimos
                    layoutRellenar.getChildren().add(hbox);
                }
            } else if (objeto instanceof ActividadModel) {
                if (((ActividadModel) objeto).getCreador_ofertante() != null) {
                    //Cargamos la vista
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Vistas/ActividadIndividual.fxml"));
                    //Actualizamos el panel
                    HBox hbox = fxmlLoader.load();
                    ActividadIndividualController actividadIndividualController = fxmlLoader.getController();
                    actividadIndividualController.introducirDatos((ActividadModel) objeto);
                    //Cambiamos su tamaño
                    layoutRellenar.setPrefHeight(layoutRellenar.getHeight() + 238);
                    //Lo añadimos
                    layoutRellenar.getChildren().add(hbox);
                }
            }
        }
    }

    private ArrayList<ActividadModel> llenarListaActividades() {
        return new TransformadorActividad().recibirInformacionGet();
    }

    private ArrayList<SugerenciaActividadModel> llenarListaSugerencias() {
        return new TransformadorSugerencia().recibirInformacionGet();
    }

    public void cambiarTamScene() {
        Main.cambiarTamVentana(1220, 780);
    }
}
