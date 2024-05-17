package Controladores;

import Modelos.*;
import TransformadorJSON.Actividad.TransformadorActividad;
import TransformadorJSON.Consumidor.TransformadorConsumidor;
import TransformadorJSON.Ofertante.TransformadorOfertante;
import TransformadorJSON.Participacion.TransformadorParticipacion;
import TransformadorJSON.Sugerencia.TransformadorSugerencia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import org.tfg_apb.tfg_apb_cliente.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VistaUsuarioController implements Initializable {

    @FXML
    private Button btnBorrar;

    @FXML
    private Button btnActualizar;

    @FXML
    private Label lblCambiar;

    @FXML
    private Label lblNombreEmpresa;

    @FXML
    private TextField txtFEmail;

    @FXML
    private TextField txtFNombre;

    @FXML
    private TextField txtFNombreEmpresa;

    @FXML
    private PasswordField txtFPass;

    @FXML
    private TextField txtFPrimerApellido;

    @FXML
    private TextField txtFSegundoApellido;

    @FXML
    void actualizarDatos(ActionEvent event) {
        //Validamos los datos
        if (validarDatos()) {
            //Comprobamos si es un consumidor o ofertante
            if (Main.recibirDatosUsuario() instanceof ConsumidorModel consumidorModel) {
                enviarDatosConsumidor(consumidorModel);
            } else if (Main.recibirDatosUsuario() instanceof OfertanteModel ofertanteModel) {
                enviarDatosOfertante(ofertanteModel);
            }
        }

    }


    @FXML
    void volverAtras(MouseEvent event) {
        try {
            //Volvemos la actividad que esta viendo como null por si acaso
            Main.enviarrActividad(null);
            Main.setRaiz("VistaPrincipal");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void borrarDatos(ActionEvent event) {
        if (borrarUsuario()) {
            try {
                Main.enviarrActividad(null);
                Main.setUsuario(null);
                Main.guardarDatosPass(null);
                Main.cambiarTamVentana(800, 570);
                Main.setRaiz("Login");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            lblCambiar.setVisible(true);
            lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
            lblCambiar.setText("Ha ocurrido algun borrando la cuenta");
        }
    }

    private boolean borrarUsuario() {
        //Comprobamos si es un ofertante o un consumidor
        if (Main.recibirDatosUsuario() instanceof ConsumidorModel consumidorModel) {
            //Comprobamos todos las participaciones de un Consumidor
            TransformadorParticipacion transformadorParticipacion = new TransformadorParticipacion();
            //Recibimos todas las participaciones
            ArrayList<ParticipacionModel> listaParticipacion = transformadorParticipacion.recibirParticipacionPorIdUsuario(consumidorModel.getId_consumidor());

            if (!listaParticipacion.isEmpty()) {
                //Borra todos las participaciones
                for (ParticipacionModel participacionModel : listaParticipacion) {
                    //Si ocurre algun error devuelve falso
                    if (!transformadorParticipacion.borrarPorId(participacionModel.getId_participacion_actividades())) {
                        return false;
                    }
                }
            }

            //Recibimos los datos de las sugerencias
            TransformadorSugerencia transformadorSugerencia = new TransformadorSugerencia();
            ArrayList<SugerenciaActividadModel> listaSugerencia = transformadorSugerencia.recibirInformacionGetPorIdConsumidor(consumidorModel.getId_consumidor());

            if (!listaSugerencia.isEmpty()) {
                //Borra todas las sugerencias
                for (SugerenciaActividadModel sugerenciaActividadModel : listaSugerencia) {
                    //Si ocurre algun error devuelve falso
                    if (!transformadorSugerencia.borrarPorId(sugerenciaActividadModel.getId_sugerencia())) {
                        return false;
                    }
                }
            }

            //Una vez borrado se borra a si mismo
            TransformadorConsumidor transformadorConsumidor = new TransformadorConsumidor();
            //Devuelve falso si ha ocurrido algun error
            return transformadorConsumidor.borrarConsumidor(consumidorModel.getId_consumidor());
        } else if (Main.recibirDatosUsuario() instanceof OfertanteModel ofertanteModel) {
            //Capturamos todas sus actividades
            TransformadorActividad transformadorActividad = new TransformadorActividad();
            ArrayList<ActividadModel> listaActividades = transformadorActividad.recibirActividadPorIdOfertante(ofertanteModel.getId_ofertante());

            //Si la lista no esta vacia entramos y actualizamos los datos para poner que no pertenece esa actividad a nadie
            if (!listaActividades.isEmpty()) {
                for (ActividadModel actividadModel : listaActividades) {
                    String tipoActividad = actividadModel.getTipoActividad();
                    String descripcionActividad = actividadModel.getDescripcionActividad();
                    String direccion = actividadModel.getDireccion();
                    Date fecha = actividadModel.getFecha();
                    Time hora_inicio = actividadModel.getHora_inicio();
                    Time hora_fin = actividadModel.getHora_fin();
                    int cantidad_max_personas = actividadModel.getCantidad_max_personas();
                    int cantidad_actual_personas = actividadModel.getCantidad_actual_personas();
                    ActividadModel.tipoEstado estado = actividadModel.getEstado();

                    TransformadorActividad transformadorActividadParaActualizar = new TransformadorActividad(tipoActividad, descripcionActividad, direccion, fecha, hora_inicio, hora_fin, cantidad_max_personas, cantidad_actual_personas, estado);

                    transformadorActividadParaActualizar.enviarInformacionPut(actividadModel.getId_actividad());
                }
            }

            return new TransformadorOfertante().borrarOfertante(ofertanteModel.getId_ofertante());
        }
        //Por defecto devuelve false
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cambiarTamScene();
        //Rellenamos los datos
        rellenarDatos();
    }

    public void cambiarTamScene() {
        Main.cambiarTamVentana(770, 820);
    }

    private void rellenarDatos() {
        //Comprobamos si es un consumidor
        if (Main.recibirDatosUsuario() instanceof ConsumidorModel consumidor) {
            txtFNombre.setText(consumidor.getNombreConsumidor());
            txtFPrimerApellido.setText(consumidor.getPrimerApellidoConsumidor());
            txtFSegundoApellido.setText(consumidor.getSegundoApellidoConsumidor());
            txtFPass.setText(Main.recibirDatosPass());
            txtFEmail.setText(consumidor.getEmail_consumidor());
        }

        //Comprobamos si es un administrador, si lo es, no puede cambiar sus datos ni borrarlos
        if (Main.recibirDatosUsuario() instanceof OfertanteModel ofertante) {
            if (((OfertanteModel) Main.recibirDatosUsuario()).isIs_administrador()) {
                txtFNombre.setEditable(false);
                txtFPrimerApellido.setEditable(false);
                txtFSegundoApellido.setEditable(false);
                txtFPass.setEditable(false);
                txtFNombreEmpresa.setEditable(false);
                btnActualizar.setDisable(true);
                btnBorrar.setDisable(true);
            }
            //Mostramos los botones ocultos
            lblNombreEmpresa.setVisible(true);
            txtFNombreEmpresa.setVisible(true);
            //Cambiamos los datos
            txtFNombre.setText(ofertante.getNombreOfertante());
            txtFPrimerApellido.setText(ofertante.getPrimerApellidoOfertante());
            txtFSegundoApellido.setText(ofertante.getSegundoApellidoOfertante());
            txtFPass.setText(Main.recibirDatosPass());
            txtFEmail.setText(ofertante.getEmail_ofertante());
            //Comprobamos si tiene nombre de empresa
            if (ofertante.getNombreEmpresa() != null && !ofertante.getNombreEmpresa().equals("null")) {
                txtFNombreEmpresa.setText(ofertante.getNombreEmpresa());
            } else {
                //No hacemos nada
                txtFNombreEmpresa.clear();
            }

        }
    }

    private boolean validarDatos() {
        //Sacamos todos los valores
        String nombre = txtFNombre.getText().trim();
        String primerApellido = txtFPrimerApellido.getText().trim();
        String segundoApellido = txtFSegundoApellido.getText().trim();
        String pass = txtFPass.getText().trim();
        String email = txtFEmail.getText().trim();
        String nombreEmpresa = txtFNombreEmpresa.getText().trim();


        //Comprobamos la contraseña
        if (!validarString(pass, 45)) {
            lblCambiar.setVisible(true);
            lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
            lblCambiar.setText("Ha ocurrido algun error con la contraseña");
            return false;
        }

        //Comprobamos el nombre
        if (!validarString(nombre, 45)) {
            lblCambiar.setVisible(true);
            lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
            lblCambiar.setText("Ha ocurrido algun error con el nombre");
            return false;
        }

        //Comprobamos solo si el primer apellido o el segundo cumple con la longitud maxima
        if (primerApellido.length() > 45 || segundoApellido.length() > 45) {
            lblCambiar.setVisible(true);
            lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
            lblCambiar.setText("Alguno de los apellidos supera la longitud maxima");
            return false;
        }

        if (!validarString(email, 100)) {
            lblCambiar.setVisible(true);
            lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
            lblCambiar.setText("Ha ocurrido un error con el email");
            return false;
        }


        if (nombreEmpresa.length() > 45) {
            lblCambiar.setVisible(true);
            lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
            lblCambiar.setText("El nombre de la empresa supera la longitud maxima");
            return false;
        }

        return true;
    }

    /**
     * Metodo que envia los datos para actualizar siendo un consumidor
     */
    private void enviarDatosConsumidor(ConsumidorModel consumidorAActualizar) {
        //Sacamos todos los valores
        String nombre = txtFNombre.getText().trim();
        String primerApellido = txtFPrimerApellido.getText().trim();
        String segundoApellido = txtFSegundoApellido.getText().trim();
        String pass = txtFPass.getText().trim();

        //Si la contraseña es igual a la anterior tomamos un camino diferente donde no cambiamos la contraseña
        if (pass.equals(Main.recibirDatosPass())) {
            TransformadorConsumidor transformadorConsumidor = new TransformadorConsumidor(nombre, primerApellido, segundoApellido, Main.recibirDatosPass(), consumidorAActualizar.getEmail_consumidor());

            //Actualizamos los datos
            ConsumidorModel consumidorActualizado = transformadorConsumidor.actualizarDatosPorId(consumidorAActualizar.getId_consumidor());
            //Escribimos los nuevos datos en memoria
            if (consumidorActualizado != null) {
                Main.setUsuario(consumidorActualizado);
                lblCambiar.setVisible(true);
                lblCambiar.setTextFill(Paint.valueOf("#00bb22"));
                lblCambiar.setText("Actualizado satisfactoriamente");
            } else {
                //Mostramos un mensaje de error
                lblCambiar.setVisible(true);
                lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
                lblCambiar.setText("Ha ocurrido un error actualizando los datos");
            }
        } else {
            TransformadorConsumidor transformadorConsumidor = new TransformadorConsumidor(nombre, primerApellido, segundoApellido, pass, consumidorAActualizar.getEmail_consumidor());

            //Actualizamos los datos
            ConsumidorModel consumidorActualizado = transformadorConsumidor.actualizarDatosPorId(consumidorAActualizar.getId_consumidor());
            //Escribimos los nuevos datos en memoria
            if (consumidorActualizado != null) {
                Main.setUsuario(consumidorActualizado);
                Main.guardarDatosPass(pass);
                lblCambiar.setVisible(true);
                lblCambiar.setTextFill(Paint.valueOf("#00bb22"));
                lblCambiar.setText("Actualizado satisfactoriamente");
            } else {
                //Mostramos un mensaje de error
                lblCambiar.setVisible(true);
                lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
                lblCambiar.setText("Ha ocurrido un error actualizando los datos");
            }
        }


    }

    /**
     * Metodo que envia los datos para actualizar siendo un ofertante
     */
    private void enviarDatosOfertante(OfertanteModel ofertanteAActualizar) {
        //Sacamos todos los valores
        String nombre = txtFNombre.getText().trim();
        String primerApellido = txtFPrimerApellido.getText().trim();
        String segundoApellido = txtFSegundoApellido.getText().trim();
        String pass = txtFPass.getText().trim();
        String nombreEmpresa = txtFNombreEmpresa.getText().trim();


        //Si la contraseña es es igual a la anterior tomamos un camino diferente donde no cambiamos la contraseña
        if (pass.equals(Main.recibirDatosPass())) {
            TransformadorOfertante transformadorOfertante = new TransformadorOfertante(nombre, primerApellido, segundoApellido, Main.recibirDatosPass(), nombreEmpresa, ofertanteAActualizar.getEmail_ofertante());

            //Actualizamos los datos
            OfertanteModel ofertanteActualizado = transformadorOfertante.actualizarDatosPorId(ofertanteAActualizar.getId_ofertante());
            //Escribimos los nuevos datos en memoria
            if (ofertanteActualizado != null) {
                Main.setUsuario(ofertanteActualizado);
                lblCambiar.setVisible(true);
                lblCambiar.setTextFill(Paint.valueOf("#00bb22"));
                lblCambiar.setText("Actualizado satisfactoriamente");
            } else {
                //Mostramos un mensaje de error
                lblCambiar.setVisible(true);
                lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
                lblCambiar.setText("Ha ocurrido un error actualizando los datos");
            }
        } else {
            TransformadorOfertante transformadorOfertante = new TransformadorOfertante(nombre, primerApellido, segundoApellido, pass, nombreEmpresa, ofertanteAActualizar.getEmail_ofertante());

            //Actualizamos los datos
            OfertanteModel ofertanteActualizado = transformadorOfertante.actualizarDatosPorId(ofertanteAActualizar.getId_ofertante());
            //Escribimos los nuevos datos en memoria
            if (ofertanteActualizado != null) {
                Main.setUsuario(ofertanteActualizado);
                Main.guardarDatosPass(pass);
                lblCambiar.setVisible(true);
                lblCambiar.setTextFill(Paint.valueOf("#00bb22"));
                lblCambiar.setText("Actualizado satisfactoriamente");
            } else {
                //Mostramos un mensaje de error
                lblCambiar.setVisible(true);
                lblCambiar.setTextFill(Paint.valueOf("#ff0000"));
                lblCambiar.setText("Ha ocurrido un error actualizando los datos");
            }
        }
    }

    /**
     * Metodo que valida las cadenas segun su longitud y que no estén vacias
     *
     * @param cadena
     * @param longitud
     * @return devuelve verdadero si no esta vacia la cadena y no supera su longitud
     */
    public boolean validarString(String cadena, int longitud) {
        return !cadena.trim().isBlank() && !cadena.trim().isEmpty() && cadena.length() <= longitud;
    }

}
