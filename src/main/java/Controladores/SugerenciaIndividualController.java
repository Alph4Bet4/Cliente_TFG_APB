package Controladores;

import Modelos.ActividadModel;
import Modelos.ConsumidorModel;
import Modelos.OfertanteModel;
import Modelos.SugerenciaActividadModel;
import TransformadorJSON.Actividad.TransformadorActividad;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.tfg_apb.tfg_apb_cliente.Main;

import java.io.IOException;

public class SugerenciaIndividualController {

    int idSugerencia;
    int idActividadSugerida;

    ActividadModel actividadSugerida;


    @FXML
    private HBox box;

    @FXML
    private Button btnCrear;

    @FXML
    private Label lblCantidadPersonasAct;

    @FXML
    private Label lblCantidadPersonasMax;

    @FXML
    private Label lblDescripcion;

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
    private Label lblTipoAct;

    @FXML
    private Pane panelAbrirAct;

    @FXML
    void abrirActividad(MouseEvent event) {
        //Te deja entrar si eres un ofertante, sino no
        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            try {
                Main.enviarrActividad(actividadSugerida);
                Main.setRaiz("VistaCrearActividad");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void crearActividad(ActionEvent event) {
        //Te deja entrar si eres un ofertante, sino no
        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            try {
                Main.enviarrActividad(actividadSugerida);
                Main.setRaiz("VistaCrearActividad");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Metodo que introduce los datos de las sugerencias automaticamente
     *
     * @param sugerencia
     */
    public void rellenarDatos(SugerenciaActividadModel sugerencia) {
        //Primero comprobamos si lo ha creado algun ofertante, si lo ha hecho no se rellena, si nadie lo ha hecho lo rellenamos
        if (sugerencia.getActividad().getCreador_ofertante() == null) {

            this.idSugerencia = sugerencia.getId_sugerencia();
            this.idActividadSugerida = sugerencia.getActividad().getId_actividad();
            //Cogemos los datos de la actividad sugerida
            TransformadorActividad transformadorActividad = new TransformadorActividad();
            actividadSugerida = transformadorActividad.recibirActividadPorId(idActividadSugerida);

            //Rellenamos con el tipo de la actividad
            lblTipoAct.setText(sugerencia.getActividad().getTipoActividad());

            //Rellenamos la descripcion
            lblDescripcion.setText(sugerencia.getActividad().getDescripcionActividad());

            //Rellenamos la fecha
            lblFecha.setText(sugerencia.getActividad().getFecha().toString());

            //Rellenamos las horas de inicio y fin
            lblHoraInicio.setText(sugerencia.getActividad().getHora_inicio().toString());
            lblHoraFin.setText(sugerencia.getActividad().getHora_fin().toString());

            //Rellenamos con las cantidades de personas actuales y maximas
            lblCantidadPersonasAct.setText(String.valueOf(sugerencia.getActividad().getCantidad_actual_personas()));
            lblCantidadPersonasMax.setText(String.valueOf(sugerencia.getActividad().getCantidad_max_personas()));

            //Rellenamos la localizacion
            lblLocalizacion.setText(sugerencia.getActividad().getDireccion());

            //Rellenamos el estado
            lblEstado.setText(sugerencia.getActividad().getEstado().name());

            //Comprobamos para que si es un consumidor no pueda crear la actividad
            if (Main.recibirDatosUsuario() instanceof ConsumidorModel) {
                btnCrear.setDisable(true);
            }
        } else {
            //No hace nada
        }

    }

}
