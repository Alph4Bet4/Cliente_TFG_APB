package Controladores;

import Modelos.SugerenciaActividadModel;
import javafx.event.ActionEvent;
import Modelos.ActividadModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class ActividadIndividualController {

    @FXML
    private HBox box;

    @FXML
    private Button btnInscripcion;

    @FXML
    private Label lblCantidadPersonasAct;

    @FXML
    private Label lblCantidadPersonasMax;

    @FXML
    private Label lblCreacion;

    @FXML
    private Label lblDescripcion;

    @FXML
    private Label lblEmailUsuario;

    @FXML
    private Label lblEstado;

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblHoraFin;

    @FXML
    private Label lblHoraInicio;

    @FXML
    private Label lblLocalizacion;

    @FXML
    private Label lblNombreEmpresa;

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private Label lblTipoAct;

    @FXML
    private Pane panelAbrirAct;

    @FXML
    void abrirActividad(MouseEvent event) {
        //TODO
    }

    @FXML
    synchronized void inscribirseActividad(ActionEvent event) {
        //TODO
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

    /**
     * Metodo que introduce los datos de las actividades automaticamente
     *
     * @param actividad
     */
    public void introducirDatos(ActividadModel actividad) {
        //Solo muestra las actividades que han sido creadas, las sugeridas no
        if (actividad.getCreador_ofertante() != null) {


            //Cambiamos el tipo de actividad
            lblTipoAct.setText(actividad.getTipoActividad());

            //Cambiamos la descripcion
            lblDescripcion.setText(actividad.getDescripcionActividad());

            //Cambiamos la fecha de la actividad
            lblFecha.setText(actividad.getFecha().toString());
            //TODO hacer controlaciones de tiempo y fecha
            //Cambiamos la hora de inicio y de finalizacion
            lblHoraInicio.setText(actividad.getHora_inicio().toString());
            lblHoraFin.setText(actividad.getHora_fin().toString());

            //Cambiamos la cantidad de personas actuales y maximas
            lblCantidadPersonasAct.setText(String.valueOf(actividad.getCantidad_actual_personas()));
            lblCantidadPersonasMax.setText(String.valueOf(actividad.getCantidad_max_personas()));

            //Cambiamos la localizacion
            lblLocalizacion.setText(actividad.getDireccion());

            //Cambiamos el estado
            lblEstado.setText(actividad.getEstado().name());

            lblCreacion.setText("Creado por:");

            //Cambiamos con los datos del usuario
            lblNombreUsuario.setText(actividad.getCreador_ofertante().getNombreOfertante());
            lblEmailUsuario.setText(actividad.getCreador_ofertante().getEmail_ofertante());
            if (actividad.getCreador_ofertante().getNombreEmpresa() != null || !actividad.getCreador_ofertante().getNombreEmpresa().isBlank()) {
                lblNombreEmpresa.setText(actividad.getCreador_ofertante().getNombreEmpresa());
            }

            if (actividad.getCantidad_actual_personas() <= 0) {
                btnInscripcion.setDisable(true);
            }
        }
    }

}
