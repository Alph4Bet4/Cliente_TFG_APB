package Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.tfg_apb.tfg_apb_cliente.Main;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button btnInicioSesion;

    @FXML
    private Hyperlink hyperLink;

    @FXML
    private PasswordField lblContrasenia;

    @FXML
    private TextField lblCorreoElectronico;

    @FXML
    void iniciarSesion(ActionEvent event) {

    }

    @FXML
    void registrarseHyperLink(ActionEvent event) {
        try {
            Main.setRaiz("Registro");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
