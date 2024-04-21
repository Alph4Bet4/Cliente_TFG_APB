package Controladores;

import Modelos.OfertanteModel;
import Modelos.RecursosModel;
import TransformadorJSON.Recursos.TransformadorRecursos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.tfg_apb.tfg_apb_cliente.Main;

public class RecursoIndividualController {

    RecursosModel recursosModel;

    @FXML
    private HBox box;

    @FXML
    private Button btnEliminar;

    @FXML
    private Label lblBooleanDada;

    @FXML
    private Label lblCantidad;

    @FXML
    private Label lblNombreRecurso;

    @FXML
    void eliminarRecurso(ActionEvent event) {
        //Comprobamos si es el administrador o el creador
        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            if (((OfertanteModel) Main.recibirDatosUsuario()).isIs_administrador() || ((OfertanteModel) Main.recibirDatosUsuario()).getId_ofertante() == this.recursosModel.getActividad().getId_actividad()) {
                TransformadorRecursos transformadorRecursos = new TransformadorRecursos();
                if (transformadorRecursos.borrarPorId(recursosModel.getId_recurso())) {
                    btnEliminar.setDisable(true);
                }
            }
        }
    }

    /**
     * Metodo que introduce los datos
     *
     * @param recurso
     */
    public void introducirDatos(RecursosModel recurso) {
        //Le damos los valores al recurso
        this.recursosModel = recurso;

        //Comprobamos si el usuario es un administrador o el creador de la actividad
        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            if (recurso.getActividad() != null) {
                if (((OfertanteModel) Main.recibirDatosUsuario()).isIs_administrador() || ((OfertanteModel) Main.recibirDatosUsuario()).getId_ofertante() == recurso.getActividad().getId_actividad()) {
                    //Activamos el boton
                    btnEliminar.setDisable(false);
                    btnEliminar.setVisible(true);
                }
            }
        }

        //Rellenamos los datos
        lblNombreRecurso.setText(recurso.getNombre_recurso());
        lblCantidad.setText(String.valueOf(recurso.getCantidad()));
        if (recurso.isIs_ofertada_por_ofertante()) {
            lblBooleanDada.setText("Si");
        } else {
            lblBooleanDada.setText("No");
        }

    }

}
