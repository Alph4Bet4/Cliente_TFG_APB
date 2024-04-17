package Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.tfg_apb.tfg_apb_cliente.Main;

import java.io.IOException;

public class RegistrarseController {

    @FXML
    private Button btnRegistro;
    @FXML
    private Hyperlink hyperLink;
    @FXML
    private PasswordField lblContrasenia;
    @FXML
    private TextField lblCorreoElectronico;
    @FXML
    private TextField lblNombre;
    @FXML
    private TextField lblNombreEmpresa;
    @FXML
    private TextField lblPrimerApellido;
    @FXML
    private TextField lblSegundoApellido;
    @FXML
    private RadioButton rBOfertante;
    @FXML
    private RadioButton rbConsumidor;

    @FXML
    void iniciarSesion(ActionEvent event) {
        //TODO renombrar
    }

    @FXML
    void inicioSesionHyperLink(ActionEvent event) {
        try {
            Main.setRaiz("Login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void comprobarUsuario(ActionEvent event) {
        if (rbConsumidor.isSelected()) {
            lblNombreEmpresa.setDisable(true);
        } else if (rBOfertante.isSelected()) {
            lblNombreEmpresa.setDisable(false);
        }
    }

}
