package Controladores;

import TransformadorJSON.Consumidor.TransformadorConsumidor;
import TransformadorJSON.Ofertante.TransformadorOfertante;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import org.tfg_apb.tfg_apb_cliente.Main;

import java.io.IOException;

public class RegistrarseController {

    @FXML
    private Button btnRegistro;

    @FXML
    private Hyperlink hyperLink;

    @FXML
    private Label lblCambiar;

    @FXML
    private RadioButton rbOfertante;

    @FXML
    private RadioButton rbConsumidor;

    @FXML
    private PasswordField textFieldContrasenia;

    @FXML
    private TextField textFieldCorreoElectronico;

    @FXML
    private TextField textFieldNombre;

    @FXML
    private TextField textFieldNombreEmpresa;

    @FXML
    private TextField textFieldPrimerApellido;

    @FXML
    private TextField textFieldSegundoApellido;

    @FXML
    private ToggleGroup tipoUsuario;


    @FXML
    void registrarse(ActionEvent event) {
        //Recogemos los datos
        String nombreUsuario = textFieldNombre.getText();
        String primerApellido = textFieldPrimerApellido.getText();
        String segundoApellido = textFieldSegundoApellido.getText();
        String email = textFieldCorreoElectronico.getText();
        String contrasenia = textFieldContrasenia.getText();
        String nombreEmpresa = textFieldNombreEmpresa.getText();

        if (comprobarCamposObligatorios(nombreUsuario, email, contrasenia)) {
            aniadirUsuario(nombreUsuario, primerApellido, segundoApellido, contrasenia, email, nombreEmpresa);
        }
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
            textFieldNombreEmpresa.setDisable(true);
        } else if (rbOfertante.isSelected()) {
            textFieldNombreEmpresa.setDisable(false);
        }
    }

    private boolean comprobarCamposObligatorios(String nombreUsuario, String email, String contrasenia) {
        //Comprueba primeramente el nombre de usuario
        if ((!nombreUsuario.isBlank() || !nombreUsuario.isEmpty()) && nombreUsuario.length() <= 45) {
            //Comprueba el email
            if ((!email.isBlank() || !email.isEmpty()) && email.length() <= 100) {
                //Comprueba la contraseña
                if ((!contrasenia.isBlank() || !contrasenia.isEmpty()) && contrasenia.length() <= 45) {
                    //Devuelve verdadero si está correcto
                    return true;
                } else {
                    //Mostramos el lbl
                    lblCambiar.setVisible(true);
                    //Cambiamos con el error
                    lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
                    lblCambiar.setText("La contraseña está vacia o es muy larga");
                }
            } else {
                //Mostramos el lbl
                lblCambiar.setVisible(true);
                //Cambiamos con el error
                lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
                lblCambiar.setText("El email está vacio o es muy largo");
            }
        } else {
            //Mostramos el lbl
            lblCambiar.setVisible(true);
            //Cambiamos con el error
            lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
            lblCambiar.setText("El nombre está vacio o es muy largo");
        }
        return false;
    }

    /**
     * Método que hace las comprobaciones necesarias antes de crear los usuarios
     * @param nombreUsuario
     * @param primerApellido
     * @param segundoApellido
     * @param contrasenia
     * @param email
     * @param nombreEmpresa
     */
    private void aniadirUsuario(String nombreUsuario, String primerApellido, String segundoApellido, String contrasenia, String email, String nombreEmpresa) {
        //Comprobamos si es un ofertante o un consumidor
        if (rbConsumidor.isSelected()) {
            //Añadimos al consumidor
            TransformadorConsumidor transformador = new TransformadorConsumidor(nombreUsuario, primerApellido, segundoApellido, contrasenia, email);
            if (transformador.enviarInformacionPost()) {
                //Mostramos el lbl
                lblCambiar.setVisible(true);
                //Cambiamos con el error
                lblCambiar.setTextFill(Paint.valueOf("#00bb22"));
                lblCambiar.setText("Consumidor creado satisfactoriamente");
            } else {
                //Mostramos el lbl
                lblCambiar.setVisible(true);
                //Cambiamos con el error
                lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
                lblCambiar.setText("Ha ocurrido algun error");
            }

        } else if (rbOfertante.isSelected()) {
            //Añadimos al ofertante
            if ((!nombreEmpresa.isBlank() || !nombreEmpresa.isEmpty()) && nombreEmpresa.length() <= 45) {
                //No hacemos nada
            } else if (nombreEmpresa.isEmpty() || nombreEmpresa.isBlank()) {
                //Cambiamos a null
                nombreEmpresa = null;
            }

            TransformadorOfertante transformador = new TransformadorOfertante(nombreUsuario, primerApellido, segundoApellido, contrasenia, nombreEmpresa, email);
            if (transformador.enviarInformacionPost()) {
                //Mostramos el lbl
                lblCambiar.setVisible(true);
                //Cambiamos con el error
                lblCambiar.setTextFill(Paint.valueOf("#00bb22"));
                lblCambiar.setText("Ofertante creado satisfactoriamente");
            } else {
                //Mostramos el lbl
                lblCambiar.setVisible(true);
                //Cambiamos con el error
                lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
                lblCambiar.setText("Ha ocurrido algun error");
            }
        }
    }

}
