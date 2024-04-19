package org.tfg_apb.tfg_apb_cliente;

import Contenedor.ContenedorDatos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Scene escena;
    private static ContenedorDatos datos = new ContenedorDatos();

    @Override
    public void start(Stage stage) throws IOException {
        escena = new Scene(cargarFXML("Login"));
        stage.setScene(escena);
        stage.setTitle("O&F");
        stage.setResizable(false);

        stage.show();
    }



    public static void main(String[] args) throws IOException {
        launch();
    }

    /**
     * Metodo que agrega a la clase contenedora el usuario que ha iniciado sesion para posteriormente poder acceder a sus datos
     *
     * @param usuario
     */
    public static void iniciarSesionUsuario(Object usuario) {
        datos.setUsuario(usuario);
    }

    /**
     * Metodo que devuelve los datos del usuario
     * @return
     */
    public static Object recibirDatosUsuario() {
        Object usuario = datos.getUsuario();
        return usuario;
    }

    public static void setRaiz(String fxml) throws IOException {
        escena.setRoot(cargarFXML(fxml));
    }

    private static Parent cargarFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Vistas/" + fxml + ".fxml"));

        return fxmlLoader.load();
    }

    /**
     * Metodo que cambia el tamaño a la ventana
     * @param anchura
     * @param altura
     */
    public static void cambiarTamVentana(double anchura, double altura) {
        escena.getWindow().setHeight(altura);
        escena.getWindow().setWidth(anchura);
    }



}