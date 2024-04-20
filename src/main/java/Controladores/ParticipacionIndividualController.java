package Controladores;

import Modelos.ParticipacionModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ParticipacionIndividualController {

    ParticipacionModel participacionModel;

    @FXML
    private HBox box;

    @FXML
    private Label lblUsuario;

    /**
     * Metodo que introduce los datos
     *
     * @param participacion
     */
    public void introducirDatos(ParticipacionModel participacion) {
        this.participacionModel = participacion;

        lblUsuario.setText(participacion.getConsumidor().getNombreConsumidor() + " " + participacion.getConsumidor().getPrimerApellidoConsumidor());
    }


}
