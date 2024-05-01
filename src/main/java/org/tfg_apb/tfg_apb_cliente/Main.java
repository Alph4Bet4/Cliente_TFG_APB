package org.tfg_apb.tfg_apb_cliente;

import Contenedor.ContenedorDatos;
import Modelos.ActividadModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        Image imagen = new Image(Main.class.getResourceAsStream("/Imagenes/iconoAplicacionTFG.png"));
        stage.getIcons().add(imagen);
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
    public static void setUsuario(Object usuario) {
        datos.setUsuario(usuario);
    }

    /**
     * Metodo que almacena en los datos la actividad abierta que se esté viendo
     *
     * @param actividad
     */
    public static void enviarrActividad(ActividadModel actividad) {
        datos.setActividadAbierta(actividad);
    }

    /**
     * Metodo que devuelve los datos del usuario
     *
     * @return
     */
    public static Object recibirDatosUsuario() {
        Object usuario = datos.getUsuario();
        return usuario;
    }

    /**
     * Metodo que devuelve los datos de la actividad abierta
     *
     * @return
     */
    public static ActividadModel recibirDatosActividad() {
        ActividadModel actividad = datos.getActividadAbierta();
        return actividad;
    }

    /**
     * Metodo que almacena los datos de la contraseña sin hashear
     * @param pass
     */
    public static void guardarDatosPass(String pass) {
        datos.setContraseniaSinHash(pass);
    }

    /**
     * Metodo que devuelve los datos de la contrasenia sin hashear
     * @return
     */
    public static String recibirDatosPass() {
        return datos.getContraseniaSinHash();
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
     *
     * @param anchura
     * @param altura
     */
    public static void cambiarTamVentana(double anchura, double altura) {
        escena.getWindow().setHeight(altura);
        escena.getWindow().setWidth(anchura);
    }


}