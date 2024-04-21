package Controladores;

import Modelos.*;
import TransformadorJSON.Participacion.TransformadorParticipacion;
import TransformadorJSON.Recursos.TransformadorRecursos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.tfg_apb.tfg_apb_cliente.Main;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ActividadController implements Initializable {

    ActividadModel actividad;
    ArrayList<ParticipacionModel> listaParticipantes;
    ArrayList<RecursosModel> listaRecursos;

    @FXML
    private Button btnAniadirRecurso;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminarActividad;

    @FXML
    private Button btnInscribir;

    @FXML
    private Button btnQuitarse;

    @FXML
    private Label lblCantidadPersonasAct;

    @FXML
    private Label lblCantidadPersonasMax;

    @FXML
    private Label lblEmailUsuario;

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblHoraFin;

    @FXML
    private Label lblHoraInicio;

    @FXML
    private Label lblNombreEmpresa;

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private VBox participantesLayout;

    @FXML
    private VBox recursosLayout;

    @FXML
    private TextArea txtAreaDescripcionCambiar;

    @FXML
    private TextField txtFieldLocalizacion;

    @FXML
    private TextField txtFieldTipoAct;

    @FXML
    private WebView wViewMapa;
    WebEngine webEngine;

    @FXML
    synchronized void addRecursoNuevo(ActionEvent event) {
        //TODO solo si eres el creador
    }

    @FXML
    synchronized void editarActividad(ActionEvent event) {
        //TODO solo si eres un administrador
    }

    @FXML
    synchronized void eliminarActividadActual(ActionEvent event) {
        //TODO solo si eres un administrador o el creador
    }

    @FXML
    synchronized void eliminarseDeLaLista(ActionEvent event) {
        //TODO si está en la lista
    }

    @FXML
    synchronized void inscribirseActividad(ActionEvent event) {
        //Comprobamos la cantidad de personas actuales
        if (Integer.parseInt(lblCantidadPersonasAct.getText()) < Integer.parseInt(lblCantidadPersonasMax.getText())) {
            //Nos aseguramos de que sea un consumidor y no un ofertante
            if (Main.recibirDatosUsuario() instanceof ConsumidorModel) {
                //Desactivamos el boton
                btnInscribir.setDisable(true);
                //Enviamos los datos de la actividad y del usuario a la api para inscribirnos en la actividad
                TransformadorParticipacion transformadorParticipacion = new TransformadorParticipacion(actividad.getId_actividad(), ((ConsumidorModel) Main.recibirDatosUsuario()).getId_consumidor());
                transformadorParticipacion.enviarInformacionPost();
            }
        } else {
            //No hacemos nada
        }
    }

    @FXML
    void volverAtras(ActionEvent event) {
        try {
            //Volvemos la actividad que esta viendo como null
            Main.enviarrActividad(null);
            Main.setRaiz("VistaPrincipal");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void enviarCorreo(MouseEvent event) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                URI uri = new URI("https://mail.google.com/mail/?view=cm&fs=1&to=" + lblEmailUsuario.getText());
                desktop.browse(uri);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cambiarTamScene();

        this.listaParticipantes = llenarListaParticipacion();
        this.listaRecursos = llenarListaRecursos();

        cargarDatos(Main.recibirDatosActividad());

        try {
            //Rellenamos los scroll pane
            rellenarLayout(listaParticipantes, participantesLayout);
            rellenarLayout(listaRecursos, recursosLayout);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Comprobamos que no haya ningun campo vacio
        if (!txtFieldLocalizacion.getText().isBlank() || !txtFieldLocalizacion.getText().isEmpty()) {
            // Lo hacemos lo ultimo para que no haya ningun problema
            webEngine = wViewMapa.getEngine();
            wViewMapa.setZoom(0.6);
            webEngine.load("https://www.google.com/maps/search/?api=1&query=" + txtFieldLocalizacion.getText());
        }
    }


    private void cargarDatos(ActividadModel actividadActual) {
        //Guardamos la actividad
        this.actividad = actividadActual;

        //Comprobamos si el usuario es un administrador o es el creador de la actividad para darle permisos para cambiar ciertos campos
        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            //Si es administrador le damos permisos, si es el creador de la actividad tambien
            if (((OfertanteModel) Main.recibirDatosUsuario()).isIs_administrador() || ((OfertanteModel) Main.recibirDatosUsuario()).getId_ofertante() == actividadActual.getId_actividad()) {
                //Activamos las opciones que el resto no puede usar
                txtFieldLocalizacion.setEditable(true);
                txtAreaDescripcionCambiar.setEditable(true);
                txtFieldTipoAct.setEditable(true);

                //Activamos los botones
                btnEditar.setDisable(false);
                btnEliminarActividad.setDisable(false);
                btnAniadirRecurso.setDisable(false);
            }
        }

        //Rellenamos los campos
        txtFieldLocalizacion.setText(actividadActual.getDireccion());
        txtFieldTipoAct.setText(actividadActual.getTipoActividad());
        txtAreaDescripcionCambiar.setText(actividadActual.getDescripcionActividad());

        lblFecha.setText(actividadActual.getFecha().toString());
        lblHoraInicio.setText(String.valueOf(actividadActual.getHora_inicio()));
        lblHoraFin.setText(String.valueOf(actividadActual.getHora_fin()));

        lblCantidadPersonasAct.setText(String.valueOf(actividadActual.getCantidad_actual_personas()));
        lblCantidadPersonasMax.setText(String.valueOf(actividadActual.getCantidad_max_personas()));

        if (actividadActual.getCreador_ofertante() != null) {
            lblNombreUsuario.setText(actividadActual.getCreador_ofertante().getNombreOfertante());
            if (!actividadActual.getCreador_ofertante().getNombreEmpresa().equals("null") || actividadActual.getCreador_ofertante().getNombreEmpresa() != null) {
                lblNombreEmpresa.setText(actividadActual.getCreador_ofertante().getNombreEmpresa());
                lblNombreEmpresa.setVisible(true);
            }
            lblEmailUsuario.setText(actividadActual.getCreador_ofertante().getEmail_ofertante());
        }


        //Comprobamos la cantidad de personas en total para activar o desactivar el boton de inscripcion
        if (actividadActual.getCantidad_actual_personas() < actividadActual.getCantidad_max_personas()) {
            btnInscribir.setDisable(false);
        } else {
            btnInscribir.setDisable(true);
        }

        //Comprobamos si el usuario esta dentro de la lista de inscritos
        for (ParticipacionModel participante : listaParticipantes) {
            //Primero comprobamos que sea un consumidor
            if (Main.recibirDatosUsuario() instanceof ConsumidorModel) {
                //Hacemos la comprobacion
                if (participante.getConsumidor().getId_consumidor() == ((ConsumidorModel) Main.recibirDatosUsuario()).getId_consumidor()) {
                    btnQuitarse.setDisable(false);
                }
            }
        }

        //Desactivamos los botones de quitarse e inscribirse si el usuario que lo esta viendo es un ofertante
        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            btnInscribir.setDisable(true);
            btnQuitarse.setDisable(true);
        }
    }

    public void cambiarTamScene() {
        Main.cambiarTamVentana(760, 840);
    }

    public ArrayList<ParticipacionModel> llenarListaParticipacion() {
        return new TransformadorParticipacion().recibirListaParticipantesPorIdActividad(Main.recibirDatosActividad());
    }

    public ArrayList<RecursosModel> llenarListaRecursos() {
        return new TransformadorRecursos().recibirListaRecursosPorIdActividad(Main.recibirDatosActividad());
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
            if (objeto instanceof ParticipacionModel) {
                //Cargamos la vista
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Vistas/ParticipacionIndividual.fxml"));
                //Actualizamos el panel
                HBox hbox = fxmlLoader.load();
                ParticipacionIndividualController participacionIndividualController = fxmlLoader.getController();
                participacionIndividualController.introducirDatos((ParticipacionModel) objeto);
                //Lo añadimos
                layoutRellenar.getChildren().add(hbox);
            } else if (objeto instanceof RecursosModel) {
                //Cargamos la vista
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Vistas/RecursoIndividual.fxml"));
                //Actualizamos el panel
                HBox hbox = fxmlLoader.load();
                RecursoIndividualController recursoIndividualController = fxmlLoader.getController();
                recursoIndividualController.introducirDatos((RecursosModel) objeto);
                //Lo añadimos
                layoutRellenar.getChildren().add(hbox);
            }
        }
    }
}
