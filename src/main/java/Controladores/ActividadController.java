package Controladores;

import Modelos.*;
import TransformadorJSON.Actividad.TransformadorActividad;
import TransformadorJSON.Participacion.TransformadorParticipacion;
import TransformadorJSON.Recursos.TransformadorRecursos;
import TransformadorJSON.Sugerencia.TransformadorSugerencia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.tfg_apb.tfg_apb_cliente.Main;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ActividadController implements Initializable {

    ActividadModel actividad;
    ArrayList<ParticipacionModel> listaParticipantes;
    ArrayList<RecursosModel> listaRecursos;

    @FXML
    private Button btnAniadirRecurso;

    @FXML
    private ToggleGroup grupoDadaPorOfertante;

    @FXML
    private RadioButton rbNo;

    @FXML
    private RadioButton rbSi;

    @FXML
    private TextArea txtADescripcionRecurso;

    @FXML
    private TextField txtFNombreRecurso;

    @FXML
    private TextField txtFCantidadRecurso;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminarActividad;

    @FXML
    private Button btnInscribir;

    @FXML
    private Button btnQuitarse;

    @FXML
    private Label lblCantidadPersonasAct;

    @FXML
    private Label lblCantidadPersonasMax;

    @FXML
    private Label lblEmailUsuario;

    @FXML
    private Label lblFecha;

    @FXML
    private Label lblHoraFin;

    @FXML
    private Label lblHoraInicio;

    @FXML
    private Label lblNombreEmpresa;

    @FXML
    private Label lblNombreUsuario;

    @FXML
    private Label lblMensajeError;

    @FXML
    private VBox participantesLayout;

    @FXML
    private VBox recursosLayout;

    @FXML
    private TextArea txtAreaDescripcionCambiar;

    @FXML
    private TextField txtFieldLocalizacion;

    @FXML
    private TextField txtFieldTipoAct;

    @FXML
    private WebView wViewMapa;
    WebEngine webEngine;

    @FXML
    void addRecursoNuevo(ActionEvent event) {
        //Comprobamos que sea un ofertante
        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            //Comprobamos que sea el creador o un administrador
            if (((OfertanteModel) Main.recibirDatosUsuario()).isIs_administrador() || ((OfertanteModel) Main.recibirDatosUsuario()).getId_ofertante() == actividad.getCreador_ofertante().getId_ofertante()) {
                if (comprobarDatosRecurso()) {
                    //Añadimos los recursos a la lista
                    aniadirRecursosLista();
                    //Lo mostramos en pantalla
                    aniadirRecurso();
                }
            }
        }
    }

    @FXML
    void editarActividad(ActionEvent event) {
        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            //Comprobamos que sea el creador o un administrador
            if (((OfertanteModel) Main.recibirDatosUsuario()).isIs_administrador() || ((OfertanteModel) Main.recibirDatosUsuario()).getId_ofertante() == actividad.getCreador_ofertante().getId_ofertante()) {
                //Creamos un transformador con los datos actuales y los cambiables

                String tipoActividad = txtFieldTipoAct.getText();
                String descripcion = txtAreaDescripcionCambiar.getText();
                String direccion = txtFieldLocalizacion.getText();

                //Si validan los 3 continuamos
                if (validarString(tipoActividad, 45) && validarString(descripcion, 255) && validarString(direccion, 45)) {
                    LocalDate fecha = LocalDate.parse(lblFecha.getText());
                    LocalTime horaInicio = LocalTime.parse(lblHoraInicio.getText());
                    LocalTime horaFin = LocalTime.parse(lblHoraFin.getText());

                    //Actualizamos la actividad
                    TransformadorActividad transformadorActividad = new TransformadorActividad(tipoActividad, descripcion, direccion, Date.valueOf(fecha), Time.valueOf(horaInicio), Time.valueOf(horaFin), Integer.parseInt(lblCantidadPersonasAct.getText()), Integer.parseInt(lblCantidadPersonasMax.getText()), actividad.getCreador_ofertante().getId_ofertante());
                    transformadorActividad.enviarInformacionPut(actividad.getId_actividad());

                    //Actualizamos los recursos si no esta vacio
                    if (!listaRecursos.isEmpty()) {
                        boolean isRecursosAdd = isRecursosAdd(actividad);
                        //Si se han añadido los recursos junto a la actividad mostramos un mensaje personalizado
                        if (isRecursosAdd) {
                            lblMensajeError.setVisible(true);
                            lblMensajeError.setTextFill(Paint.valueOf("#00bb22"));
                            lblMensajeError.setText("Se ha actualizado la actividad junto con los recursos");
                        } else {
                            lblMensajeError.setVisible(true);
                            lblMensajeError.setTextFill(Paint.valueOf("#00bb22"));
                            lblMensajeError.setText("Se ha actualizado la actividad");
                        }
                    } else {
                        lblMensajeError.setVisible(true);
                        lblMensajeError.setTextFill(Paint.valueOf("#00bb22"));
                        lblMensajeError.setText("Se ha actualizado la actividad");
                    }
                } else {
                    //Mostramos un error generico
                    lblMensajeError.setVisible(true);
                    lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
                    lblMensajeError.setText("Ha ocurrido un error");
                }

            }
        }
    }

    @FXML
    void eliminarActividadActual(ActionEvent event) {
        //Comprobamos si es una actividad anteriormente sugerida
        if (actividad != null) {
            //Comprobamos que sea un ofertante
            if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
                //Comprobamos que ese ofertante sea un administrador
                if (((OfertanteModel) Main.recibirDatosUsuario()).isIs_administrador()) {
                    //Nos traemos los valores de la sugerencia
                    TransformadorSugerencia transformadorSugerencia = new TransformadorSugerencia();
                    //Si se consigue borrar entra

                    SugerenciaActividadModel sugerencia = transformadorSugerencia.recibirSugerenciaPorIdActividad(Main.recibirDatosActividad().getId_actividad());
                    //Comprobamos que no sea null
                    if (sugerencia != null) {
                        if (transformadorSugerencia.borrarPorId(sugerencia.getId_sugerencia())) {
                            borrarFinalActividad();
                        }
                    } else {
                        borrarFinalActividad();
                    }
                }
            }
        }
    }

    /**
     * Metodo que se llama para borrar la actividad, tenga o no sugerencia
     */
    private void borrarFinalActividad() {
        //Comprobamos que si tiene algun recurso
        TransformadorRecursos transformadorRecursos = new TransformadorRecursos();
        ArrayList<RecursosModel> listaRecursos = transformadorRecursos.recibirListaRecursosPorIdActividad(Main.recibirDatosActividad());
        //Si la lista no esta vacia borramos los recursos
        if (!listaRecursos.isEmpty()) {
            for (RecursosModel recurso : listaRecursos) {
                transformadorRecursos.borrarPorId(recurso.getId_recurso());
            }
        }
        //Borramos luego la actividad
        TransformadorActividad transformadorActividad = new TransformadorActividad();
        if (transformadorActividad.borrarPorId(actividad.getId_actividad())) {
            //Volvemos a la pagina principal
            try {
                Main.enviarrActividad(null);
                Main.setRaiz("VistaPrincipal");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void eliminarseDeLaLista(ActionEvent event) {
        //Creamos una copia de la lista para posteriormente poder limpiar a una nueva
        ArrayList<ParticipacionModel> copiaListaParticipacion = new ArrayList<>(listaParticipantes);

        //Comprobamos si el usuario es un consumidor
        if (Main.recibirDatosUsuario() instanceof ConsumidorModel) {
            for (ParticipacionModel participacion : copiaListaParticipacion) {
                ConsumidorModel consumidorParticipante = participacion.getConsumidor();
                if (consumidorParticipante.getId_consumidor() == ((ConsumidorModel) Main.recibirDatosUsuario()).getId_consumidor()) {
                    TransformadorParticipacion transformadorParticipacion = new TransformadorParticipacion();
                    //Si lo borra actualiza la vista
                    lblMensajeError.setVisible(true);
                    if (transformadorParticipacion.borrarPorId(participacion.getId_participacion_actividades())) {
                        lblMensajeError.setTextFill(Paint.valueOf("#00bb22"));
                        lblMensajeError.setText("Te has eliminado de la lista satisfactoriamente");
                        //Cambiamos los botones
                        btnQuitarse.setDisable(true);
                        btnInscribir.setDisable(false);

                        //Actualizamos la lista
                        limpiarLayoutParticipacion();

                        //Disminuimos el contador
                        TransformadorActividad transformadorActividad = new TransformadorActividad();

                        ActividadModel actividadCambiarCantidad = transformadorActividad.recibirActividadPorId(actividad.getId_actividad());

                        lblCantidadPersonasAct.setText(String.valueOf(actividadCambiarCantidad.getCantidad_actual_personas()));
                    } else {
                        //Mostramos un error generico
                        lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
                        lblMensajeError.setText("Ha ocurrido un error borrandote de la lista");
                    }
                }

            }
        }
    }

    @FXML
    void inscribirseActividad(ActionEvent event) {
        //Comprobamos la cantidad de personas actuales pero directamente de una nueva llamada para asegurarse que no haya problemas
        if (Integer.parseInt(lblCantidadPersonasAct.getText()) < Integer.parseInt(lblCantidadPersonasMax.getText())) {
            TransformadorActividad transformadorActividad = new TransformadorActividad();
            ActividadModel actividadNueva = transformadorActividad.recibirActividadPorId(actividad.getId_actividad());
            if (actividadNueva.getCantidad_actual_personas() < actividadNueva.getCantidad_max_personas()) {
                //Nos aseguramos de que sea un consumidor y no un ofertante
                if (Main.recibirDatosUsuario() instanceof ConsumidorModel) {
                    //Desactivamos el boton
                    btnInscribir.setDisable(true);
                    //Activamos para poder quitarnos
                    btnQuitarse.setDisable(false);
                    //Enviamos los datos de la actividad y del usuario a la api para inscribirnos en la actividad
                    TransformadorParticipacion transformadorParticipacion = new TransformadorParticipacion(actividad.getId_actividad(), ((ConsumidorModel) Main.recibirDatosUsuario()).getId_consumidor());
                    transformadorParticipacion.enviarInformacionPost();

                    //Actualizamos la lista
                    limpiarLayoutParticipacion();

                    //Aumentamos la cantidad de personas
                    actividadNueva = transformadorActividad.recibirActividadPorId(actividad.getId_actividad());
                    lblCantidadPersonasAct.setText(String.valueOf(actividadNueva.getCantidad_actual_personas()));
                }
            }
        } else {
            //No hacemos nada
        }
    }

    private void limpiarLayoutParticipacion() {
        //Actualizamos la lista
        try {
            //Limpiamos la lista de participantes
            if (!listaParticipantes.isEmpty()) {
                listaParticipantes.clear();
                participantesLayout.getChildren().clear();
            }
            listaParticipantes = llenarListaParticipacion();
            rellenarLayout(listaParticipantes, participantesLayout);
        } catch (IOException e) {
            //Mostramos un error generico
            lblMensajeError.setVisible(true);
            lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
            lblMensajeError.setText("Ha ocurrido un actualizando la lista de participantes");
        }
    }

    @FXML
    void volverAtras(MouseEvent event) {
        try {
            //Volvemos la actividad que esta viendo como null
            Main.enviarrActividad(null);
            Main.setRaiz("VistaPrincipal");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void enviarCorreo(MouseEvent event) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                URI uri = new URI("https://mail.google.com/mail/?view=cm&fs=1&to=" + lblEmailUsuario.getText());
                desktop.browse(uri);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cambiarTamScene();

        this.listaParticipantes = llenarListaParticipacion();
        this.listaRecursos = llenarListaRecursos();

        cargarDatos(Main.recibirDatosActividad());

        try {
            //Rellenamos los scroll pane
            rellenarLayout(listaParticipantes, participantesLayout);
            rellenarLayout(listaRecursos, recursosLayout);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Comprobamos que no haya ningun campo vacio
        if (!txtFieldLocalizacion.getText().isBlank() || !txtFieldLocalizacion.getText().isEmpty()) {
            // Lo hacemos lo ultimo para que no haya ningun problema
            webEngine = wViewMapa.getEngine();
            wViewMapa.setZoom(0.6);
            webEngine.load("https://www.google.com/maps/search/?api=1&query=" + txtFieldLocalizacion.getText());
        }
    }


    private void cargarDatos(ActividadModel actividadActual) {
        //Guardamos la actividad
        this.actividad = actividadActual;

        //Comprobamos si el usuario es un administrador o es el creador de la actividad para darle permisos para cambiar ciertos campos
        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            //Nos aseguramos que exista creador
            if (actividadActual.getCreador_ofertante() != null) {
                //Si es administrador le damos permisos, si es el creador de la actividad tambien
                if (((OfertanteModel) Main.recibirDatosUsuario()).isIs_administrador() || ((OfertanteModel) Main.recibirDatosUsuario()).getId_ofertante() == actividadActual.getCreador_ofertante().getId_ofertante()) {
                    //Activamos las opciones que el resto no puede usar
                    txtFieldLocalizacion.setEditable(true);
                    txtAreaDescripcionCambiar.setEditable(true);
                    txtFieldTipoAct.setEditable(true);

                    //Activamos los botones
                    btnEditar.setDisable(false);
                    //Desactivamos eliminar actividad si no es un administrador
                    if (!((OfertanteModel) Main.recibirDatosUsuario()).isIs_administrador()) {
                        btnEliminarActividad.setDisable(true);
                    } else {
                        btnEliminarActividad.setDisable(false);
                    }
                    btnAniadirRecurso.setDisable(false);

                    //Activamos para los recursos
                    txtFNombreRecurso.setEditable(true);
                    txtADescripcionRecurso.setEditable(true);
                    txtFCantidadRecurso.setEditable(true);
                    rbNo.setDisable(false);
                    rbSi.setDisable(false);

                }
            }
        }

        //Rellenamos los campos
        txtFieldLocalizacion.setText(actividadActual.getDireccion());
        txtFieldTipoAct.setText(actividadActual.getTipoActividad());
        txtAreaDescripcionCambiar.setText(actividadActual.getDescripcionActividad());

        lblFecha.setText(actividadActual.getFecha().toString());
        lblHoraInicio.setText(String.valueOf(actividadActual.getHora_inicio()));
        lblHoraFin.setText(String.valueOf(actividadActual.getHora_fin()));

        lblCantidadPersonasAct.setText(String.valueOf(actividadActual.getCantidad_actual_personas()));
        lblCantidadPersonasMax.setText(String.valueOf(actividadActual.getCantidad_max_personas()));

        if (actividadActual.getCreador_ofertante() != null) {
            lblNombreUsuario.setText(actividadActual.getCreador_ofertante().getNombreOfertante());
            if (actividadActual.getCreador_ofertante().getNombreEmpresa() != null && !actividadActual.getCreador_ofertante().getNombreEmpresa().trim().equals("null")) {
                lblNombreEmpresa.setText(actividadActual.getCreador_ofertante().getNombreEmpresa());
                lblNombreEmpresa.setVisible(true);
            }
            lblEmailUsuario.setText(actividadActual.getCreador_ofertante().getEmail_ofertante());
        }


        //Comprobamos la cantidad de personas en total para activar o desactivar el boton de inscripcion
        if (actividadActual.getCantidad_actual_personas() < actividadActual.getCantidad_max_personas()) {
            btnInscribir.setDisable(false);
        } else {
            btnInscribir.setDisable(true);
        }

        //Comprobamos si el usuario esta dentro de la lista de inscritos
        for (ParticipacionModel participante : listaParticipantes) {
            //Primero comprobamos que sea un consumidor
            if (Main.recibirDatosUsuario() instanceof ConsumidorModel) {
                //Hacemos la comprobacion
                if (participante.getConsumidor().getId_consumidor() == ((ConsumidorModel) Main.recibirDatosUsuario()).getId_consumidor()) {
                    btnQuitarse.setDisable(false);
                    btnInscribir.setDisable(true);
                }
            }
        }

        //Desactivamos los botones de quitarse e inscribirse si el usuario que lo esta viendo es un ofertante
        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            btnInscribir.setDisable(true);
            btnQuitarse.setDisable(true);
        }

        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            //Comprobamos el estado de la actividad, si está finalizado todos los botones se bloquean solo si no eres administrador
            if (actividadActual.getEstado().name().equals("Finalizado") && !((OfertanteModel) Main.recibirDatosUsuario()).isIs_administrador()) {
                btnQuitarse.setDisable(true);
                btnInscribir.setDisable(true);
                btnEditar.setDisable(true);
                btnAniadirRecurso.setDisable(true);
                btnEliminarActividad.setDisable(true);

                txtFCantidadRecurso.setEditable(false);
                txtADescripcionRecurso.setEditable(false);
                txtFieldTipoAct.setEditable(false);
                txtFieldLocalizacion.setEditable(false);
                txtFNombreRecurso.setEditable(false);
                txtAreaDescripcionCambiar.setEditable(false);

            }
        }

        //Bloqueamos los botones si los estados son finalizado o EnCurso
        if (actividadActual.getEstado().name().equals("Finalizado") || actividadActual.getEstado().name().equals("EnCurso")) {
            btnInscribir.setDisable(true);
            btnQuitarse.setDisable(true);
        }

    }

    public void cambiarTamScene() {
        Main.cambiarTamVentana(1050, 840);
    }

    public ArrayList<ParticipacionModel> llenarListaParticipacion() {
        return new TransformadorParticipacion().recibirListaParticipantesPorIdActividad(Main.recibirDatosActividad());
    }

    public ArrayList<RecursosModel> llenarListaRecursos() {
        return new TransformadorRecursos().recibirListaRecursosPorIdActividad(Main.recibirDatosActividad());
    }

    /**
     * Metodo generico que rellena las listas
     *
     * @param lista
     * @param layoutRellenar
     * @throws IOException
     */
    private void rellenarLayout(List lista, VBox layoutRellenar) throws IOException {
        for (Object objeto : lista) {
            if (objeto instanceof ParticipacionModel) {
                //Cargamos la vista
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Vistas/ParticipacionIndividual.fxml"));
                //Actualizamos el panel
                HBox hbox = fxmlLoader.load();
                ParticipacionIndividualController participacionIndividualController = fxmlLoader.getController();
                participacionIndividualController.introducirDatos((ParticipacionModel) objeto);
                //Lo añadimos
                layoutRellenar.getChildren().add(hbox);
            } else if (objeto instanceof RecursosModel) {
                //Cargamos la vista
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Vistas/RecursoIndividual.fxml"));
                //Actualizamos el panel
                HBox hbox = fxmlLoader.load();
                RecursoIndividualController recursoIndividualController = fxmlLoader.getController();
                recursoIndividualController.introducirDatos((RecursosModel) objeto);
                //Lo añadimos
                layoutRellenar.getChildren().add(hbox);
            }
        }
    }

    /**
     * Metodo que añade a la lista de los recursos los recursos individualmente
     */
    public void aniadirRecursosLista() {
        String nombreRecurso = txtFNombreRecurso.getText();
        String descripcion = txtADescripcionRecurso.getText();
        int cantidadRecursos = Integer.parseInt(txtFCantidadRecurso.getText());
        boolean isProporcionada = false; //Por defecto, no
        //Capturamos los posibles valores
        if (rbSi.isSelected()) {
            isProporcionada = true;
        } else if (rbNo.isSelected()) {
            isProporcionada = false;
        }

        RecursosModel recurso = new RecursosModel(nombreRecurso, descripcion, cantidadRecursos, isProporcionada);

        listaRecursos.add(recurso);
    }

    /**
     * Metodo que limpia la vista de los recursos y actualiza con nuevos
     */
    public void aniadirRecurso() {
        recursosLayout.getChildren().clear();
        try {
            for (RecursosModel recurso : listaRecursos) {
                //Cargamos la vista
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Vistas/RecursoIndividual.fxml"));
                //Actualizamos el panel
                HBox hbox = fxmlLoader.load();
                RecursoIndividualController recursoIndividualController = fxmlLoader.getController();
                recursoIndividualController.introducirDatos(recurso);
                //Lo añadimos
                recursosLayout.getChildren().add(hbox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Borramos los datos de los recursos para poder luego añadir mas
        txtFNombreRecurso.clear();
        txtADescripcionRecurso.clear();
        txtFCantidadRecurso.clear();
        rbSi.setSelected(true);

    }

    /**
     * Metodo que comprueba todos los datos del recurso antes de añadirlo
     *
     * @return
     */
    public boolean comprobarDatosRecurso() {
        //Capturamos el valor de nombre
        String nombreRecurso = txtFNombreRecurso.getText();
        if (!validarString(nombreRecurso, 100)) {
            //Si no cumple los requisitos devuelve falso
            lblMensajeError.setVisible(true);
            lblMensajeError.setTextFill(javafx.scene.paint.Paint.valueOf("#ff0000"));
            lblMensajeError.setText("Ha ocurrido un error con el nombre del recurso o es muy largo");
            return false;
        }
        //Capturamos el valor de la descripcion
        String descripcion = txtADescripcionRecurso.getText();
        if (!validarString(descripcion, 200)) {
            //Si no cumple los requisitos devuelve falso
            lblMensajeError.setVisible(true);
            lblMensajeError.setTextFill(javafx.scene.paint.Paint.valueOf("#ff0000"));
            lblMensajeError.setText("Ha ocurrido un error la descripcion del recurso o es muy largo");
            return false;
        }
        //Capturamos el valor de la cantidad de recursos
        try {
            int cantidadRecursos = Integer.parseInt(txtFCantidadRecurso.getText());
            if (cantidadRecursos <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Si no cumple los requisitos devuelve falso
            lblMensajeError.setVisible(true);
            lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
            lblMensajeError.setText("La cantidad de recurso insertadas no es un numero o es un valor incorrecto");
            return false;
        }

        //Controlamos que alguno de los dos botones está selecionado, por si acaso
        if (!rbNo.isSelected() && !rbSi.isSelected()) {
            return false;
        }

        //Devuelve true si ha pasado por el resto de comprobaciones y no hay ningun problema
        return true;
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

    /**
     * Metodo que envia las peticiones POST y añade los recursos a la actividad
     *
     * @param actividad
     * @return
     */
    private boolean isRecursosAdd(ActividadModel actividad) {
        boolean isRecursosAdd = false;
        //Añadimos los recursos
        for (RecursosModel recurso : listaRecursos) {
            TransformadorRecursos transformadorRecursos = new TransformadorRecursos(actividad.getId_actividad(), recurso.getNombre_recurso(), recurso.getDescripcion(), recurso.getCantidad(), recurso.isIs_ofertada_por_ofertante());
            if (transformadorRecursos.enviarInformacionPost()) {
                isRecursosAdd = true;
            } else {
                isRecursosAdd = false;
            }
        }
        return isRecursosAdd;
    }
}
