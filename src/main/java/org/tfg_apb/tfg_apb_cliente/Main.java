package org.tfg_apb.tfg_apb_cliente;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Scene escena;

    @Override
    public void start(Stage stage) throws IOException {
        escena = new Scene(cargarFXML("Login"));
        stage.setScene(escena);
        stage.setTitle("O&F");
        stage.setResizable(false);

        stage.show();
    }

    public static void setRaiz(String fxml) throws IOException {
        escena.setRoot(cargarFXML(fxml));
    }

    private static Parent cargarFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Vistas/" + fxml + ".fxml"));

        return fxmlLoader.load();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}