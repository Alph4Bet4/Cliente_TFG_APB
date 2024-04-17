package Controladores;

import Modelos.*;
import TransformadorJSON.Actividad.TransformadorActividad;
import TransformadorJSON.Consumidor.TransformadorConsumidor;
import TransformadorJSON.Ofertante.TransformadorOfertante;
import TransformadorJSON.Participacion.TransformadorParticipacion;
import TransformadorJSON.Recursos.TransformadorRecursos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.tfg_apb.tfg_apb_cliente.Main;

import java.io.IOException;
import java.util.ArrayList;

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
        /*
        Thread hiloListarOfertantes = new Thread(() -> {
            ArrayList<OfertanteModel> listaOfertantes = new TransformadorOfertante().recibirInformacionGet();

            for (OfertanteModel ofertante : listaOfertantes ) {
                System.out.println(ofertante);
            }
        });
        hiloListarOfertantes.start();

        Thread hiloListarConsumidores = new Thread(() -> {
            ArrayList<ConsumidorModel> listaConsumidores = new TransformadorConsumidor().recibirInformacionGet();

            for (ConsumidorModel consumidor : listaConsumidores) {
                System.out.println(consumidor);
            }
        });
        hiloListarConsumidores.start();

        Thread hiloListarActividades = new Thread(() -> {
           ArrayList<ActividadModel> listaActividades = new TransformadorActividad().recibirInformacionGet();

           for (ActividadModel actividad : listaActividades) {
               System.out.println(actividad);
           }
        });
        hiloListarActividades.start();

        Thread hiloListarRecursos = new Thread(() -> {
            ArrayList<RecursosModel> listaRecursos = new TransformadorRecursos().recibirInformacionGet();


            for (RecursosModel recurso : listaRecursos) {
                System.out.println(recurso);
            }

        });
        hiloListarRecursos.start();

         */

        Thread hiloListaParticipacion = new Thread(() -> {
            ArrayList<ParticipacionModel> listaParticipacion = new TransformadorParticipacion().recibirInformacionGet();

            for (ParticipacionModel participacion : listaParticipacion) {
                System.out.println(participacion);
            }
        });
        hiloListaParticipacion.start();
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
