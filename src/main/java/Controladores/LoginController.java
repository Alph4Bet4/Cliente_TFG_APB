package Controladores;

import Modelos.ConsumidorModel;
import Modelos.OfertanteModel;
import TransformadorJSON.Consumidor.TransformadorConsumidor;
import TransformadorJSON.Ofertante.TransformadorOfertante;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Paint;
import org.tfg_apb.tfg_apb_cliente.Main;

import java.io.IOException;

public class LoginController {
    @FXML
    private RadioButton rbConsumidor;

    @FXML
    private ToggleGroup rbEleccion;

    @FXML
    private RadioButton rbOfertante;

    @FXML
    private Button btnInicioSesion;

    @FXML
    private Hyperlink hyperLink;

    @FXML
    private Label lblCambiar;

    @FXML
    private PasswordField txtFieldContrasenia;

    @FXML
    private TextField txtFieldCorreoElectronico;

    @FXML
    private TextField txtFieldUsuario;

    @FXML
    void iniciarSesion(ActionEvent event) {
        String nombreUsuario = txtFieldUsuario.getText();
        String emailUsuario = txtFieldCorreoElectronico.getText();
        String pass = txtFieldContrasenia.getText();

        if (comprobarCamposObligatorios(nombreUsuario, emailUsuario, pass)) {
            login(nombreUsuario, pass, emailUsuario);
        }

    }

    private void login(String nombreUsuario, String pass, String emailUsuario) {
        //Si esta seleccionado el consumidor inicia sesion como uno
        if (rbConsumidor.isSelected()) {
            TransformadorConsumidor transformador = new TransformadorConsumidor(nombreUsuario, "", "", pass, emailUsuario);
            ConsumidorModel consumidor = transformador.recibirConsumidorPorDatos();
            if (consumidor != null) {
                //Metemos los valores en el contenedor
                Main.iniciarSesionUsuario(consumidor);

                //Cambiamos de ventana
                cambiarPestania();

            } else {
                //Mostramos el lbl
                lblCambiar.setVisible(true);
                //Cambiamos con el error
                lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
                lblCambiar.setText("Ha ocurrido algun error");
            }
        } else if (rbOfertante.isSelected()) {
            TransformadorOfertante transformador = new TransformadorOfertante(nombreUsuario, "", "", pass, "", emailUsuario);
            OfertanteModel ofertante = transformador.recibirOfertantePorDatos();
            if (ofertante != null) {
                //Metemos los valores en el contenedor
                Main.iniciarSesionUsuario(ofertante);

                //Cambiamos de ventana
                cambiarPestania();

            } else {
                //Mostramos el lbl
                lblCambiar.setVisible(true);
                //Cambiamos con el error
                lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
                lblCambiar.setText("Ha ocurrido algun error");
            }
        }
    }

    @FXML
    void registrarseHyperLink(ActionEvent event) {
        try {
            Main.setRaiz("Registro");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void cambiarPestania() {
        try {
            Main.setRaiz("VistaPrincipal");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que hace las comprobaciones necesarias para poder iniciar sesion
     *
     * @param nombreUsuario
     * @param emailUsuario
     * @param pass
     */
    private boolean comprobarCamposObligatorios(String nombreUsuario, String emailUsuario, String pass) {
        //Comprobamos el nombre de usuario
        if ((!nombreUsuario.isBlank() || !nombreUsuario.isEmpty()) && nombreUsuario.length() <= 45) {
            //Comprobamos el email
            if ((!emailUsuario.isBlank() || !emailUsuario.isEmpty()) && emailUsuario.length() <= 100) {
                //Comprobamos la pass
                if ((!pass.isBlank() || !pass.isEmpty()) && pass.length() <= 45) {
                    //Devolvemos verdadero si esta correcto
                    return true;
                } else {
                    //Mostramos el lbl
                    lblCambiar.setVisible(true);
                    //Cambiamos con el error
                    lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
                    lblCambiar.setText("La contraseña está vacia o es muy larga");
                    return false;
                }
            } else {
                //Mostramos el lbl
                lblCambiar.setVisible(true);
                //Cambiamos con el error
                lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
                lblCambiar.setText("El email está vacio o es muy largo");
                return false;
            }
        } else {
            //Mostramos el lbl
            lblCambiar.setVisible(true);
            //Cambiamos con el error
            lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
            lblCambiar.setText("El nombre está vacio o es muy largo");
            return false;
        }
    }
}
