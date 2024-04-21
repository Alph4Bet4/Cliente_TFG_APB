package Controladores;

import Modelos.*;
import TransformadorJSON.Actividad.TransformadorActividad;
import TransformadorJSON.Recursos.TransformadorRecursos;
import TransformadorJSON.Sugerencia.TransformadorSugerencia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.StringConverter;
import org.tfg_apb.tfg_apb_cliente.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.control.ToggleGroup;

public class CrearActividadController implements Initializable {

    LocalDate fechaCorrecta;
    LocalTime horaInicio;
    LocalTime horaFin;
    ArrayList<RecursosModel> listaRecursos = new ArrayList<>();
    ActividadModel actividadSugerida;
    private int id_sugerencia;

    @FXML
    private Button btnAniadirRecurso;

    @FXML
    private Button btnCrearActividad;

    @FXML
    private Button btnEliminarActividad;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblMensajeError;

    @FXML
    private Label lblRecursos;

    @FXML
    private VBox recursosLayout;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextArea txtAreaDescripcionCambiar;

    @FXML
    private TextField txtCantidadPersonas;

    @FXML
    private TextField txtFieldHoraFin;

    @FXML
    private TextField txtFieldHoraInicio;

    @FXML
    private TextField txtFieldLocalizacion;

    @FXML
    private ToggleGroup grupoDadaPorOfertante;

    @FXML
    private TextField txtFieldTipoAct;

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
    void addRecursoNuevo(ActionEvent event) {
        if (comprobarDatosRecurso()) {
            //Añadimos los recursos a la lista
            aniadirRecursosLista();
            //Lo mostramos en pantalla
            aniadirRecurso();
        }
    }

    @FXML
    void eliminarActividadActual(ActionEvent event) {
        //Comprobamos si es una actividad anteriormente sugerida
        if (actividadSugerida != null) {
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
                            if (transformadorActividad.borrarPorId(actividadSugerida.getId_actividad())) {
                                //Volvemos a la pagina principal
                                try {
                                    Main.enviarrActividad(null);
                                    Main.setRaiz("VistaPrincipal");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    @FXML
    void crearActividad(ActionEvent event) {
        //Comprobamos todos los campos
        if (comprobacionesCampos()) {
            //Si la actividad sugerida es null significa que la actividad es nueva por lo tanto entramos
            if (this.actividadSugerida == null) {
                //Nos aseguramos que el usuario que sea un ofertante para capturar su id, sino no lo hacemos y lo convertimos a una actividad sugerida
                if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
                    enviarActividad();
                } else if (Main.recibirDatosUsuario() instanceof ConsumidorModel) {
                    enviarActividad();
                }
            } else {
                //Si entra aqui significa que la actividad es sugerida y por lo tanto vamos a actualizar los datos
                //Forzamos que sea un ofertante
                if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
                    actualizarYEnviarActividad();
                }
            }

        }
    }

    /**
     * Metodo que envia la actividad anteriormente sugerida siendo solo capaz de acceder este metodo si eres un ofertante
     */
    private void actualizarYEnviarActividad() {
        //Capturamos los valores
        String tipoActividad = txtFieldTipoAct.getText();
        String descripcion = txtAreaDescripcionCambiar.getText();
        String localizacion = txtFieldLocalizacion.getText();
        int cantidadPersonas = Integer.parseInt(txtCantidadPersonas.getText());
        //Creamos el transformador
        TransformadorActividad transformadorActividad = new TransformadorActividad(tipoActividad, descripcion, localizacion, Date.valueOf(this.fechaCorrecta), Time.valueOf(this.horaInicio), Time.valueOf(this.horaFin), cantidadPersonas, ((OfertanteModel) Main.recibirDatosUsuario()).getId_ofertante());

        //Enviamos la actualizacion y capturamos los valores de la actividad
        ActividadModel actividad = transformadorActividad.enviarInformacionPut(actividadSugerida.getId_actividad());

        if (actividad != null) {
            //Cuando la lista de recursos no esta vacia entra
            if (!listaRecursos.isEmpty()) {
                boolean isRecursosAdd = isRecursosAdd(actividad);
                //Si se han añadido los recursos junto a la actividad mostramos un mensaje personalizado
                if (isRecursosAdd) {
                    lblMensajeError.setVisible(true);
                    lblMensajeError.setTextFill(Paint.valueOf("#00bb22"));
                    lblMensajeError.setText("Se ha creado la actividad junto con los recursos");
                } else {
                    lblMensajeError.setVisible(true);
                    lblMensajeError.setTextFill(Paint.valueOf("#00bb22"));
                    lblMensajeError.setText("Se ha creado la actividad");
                }
            } else {
                lblMensajeError.setVisible(true);
                lblMensajeError.setTextFill(Paint.valueOf("#00bb22"));
                lblMensajeError.setText("Se ha creado la actividad");
            }
        } else {
            //Mostramos un error generico
            lblMensajeError.setVisible(true);
            lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
            lblMensajeError.setText("Ha ocurrido un error");
        }

    }

    /**
     * Metodo que envia la actividad ya sea siendo un ofertante o un consumidor
     */
    private void enviarActividad() {
        //Capturamos los valores
        String tipoActividad = txtFieldTipoAct.getText();
        String descripcion = txtAreaDescripcionCambiar.getText();
        String localizacion = txtFieldLocalizacion.getText();
        int cantidadPersonas = Integer.parseInt(txtCantidadPersonas.getText());
        TransformadorActividad transformadorActividad = null;
        //Comprobamos si es un ofertante o un consumidor
        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            //Si es un ofertante usa el metodo normal
            //Creamos el transformador
            transformadorActividad = new TransformadorActividad(tipoActividad, descripcion, localizacion, Date.valueOf(this.fechaCorrecta), Time.valueOf(this.horaInicio), Time.valueOf(this.horaFin), cantidadPersonas, ((OfertanteModel) Main.recibirDatosUsuario()).getId_ofertante());
        } else if (Main.recibirDatosUsuario() instanceof ConsumidorModel) {
            //Si es un consumidor el otro metodo
            //Creamos el transformador
            transformadorActividad = new TransformadorActividad(tipoActividad, descripcion, localizacion, Date.valueOf(this.fechaCorrecta), Time.valueOf(this.horaInicio), Time.valueOf(this.horaFin), cantidadPersonas);

        }
        if (transformadorActividad != null) {
            //Creamos la actividad
            ActividadModel actividad = transformadorActividad.enviarInformacionPostYRecibirDatos();
            //Si se ha creado la actividad será distinta de null
            if (actividad != null) {
                //Cuando la lista de recursos no esta vacia entra
                if (!listaRecursos.isEmpty()) {
                    boolean isRecursosAdd = isRecursosAdd(actividad);
                    //Si se han añadido los recursos junto a la actividad mostramos un mensaje personalizado
                    if (isRecursosAdd) {
                        lblMensajeError.setVisible(true);
                        lblMensajeError.setTextFill(Paint.valueOf("#00bb22"));
                        lblMensajeError.setText("Se ha creado la actividad junto con los recursos");
                    } else {
                        lblMensajeError.setVisible(true);
                        lblMensajeError.setTextFill(Paint.valueOf("#00bb22"));
                        lblMensajeError.setText("Se ha creado la actividad");
                    }
                } else {
                    //Comprobamos si es un consumidor para crear la sugerencia a posteriori
                    if (Main.recibirDatosUsuario() instanceof ConsumidorModel) {
                        TransformadorSugerencia transformadorSugerencia = new TransformadorSugerencia(((ConsumidorModel) Main.recibirDatosUsuario()).getId_consumidor(), actividad.getId_actividad());
                        transformadorSugerencia.enviarInformacionPost();
                    }
                    lblMensajeError.setVisible(true);
                    lblMensajeError.setTextFill(Paint.valueOf("#00bb22"));
                    lblMensajeError.setText("Se ha creado la actividad");
                }
            } else {
                //Mostramos un error generico
                lblMensajeError.setVisible(true);
                lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
                lblMensajeError.setText("Ha ocurrido un error");
            }
        }

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

    @FXML
    void volverAtras(ActionEvent event) {
        try {
            Main.enviarrActividad(null);
            Main.setRaiz("VistaPrincipal");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
     * Metodo que comprueba que todos los campos esten correctos
     *
     * @return
     */
    public boolean comprobacionesCampos() {
        String tipoActividad = txtFieldTipoAct.getText();
        //Validamos el tipo de actividad
        if (!validarString(tipoActividad, 45)) {
            //Si no cumple los requisitos devuelve falso
            lblMensajeError.setVisible(true);
            lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
            lblMensajeError.setText("Ha ocurrido un error con el tipo de actividad o es muy largo");
            return false;
        }
        //Validamos la descripcion
        String descripcion = txtAreaDescripcionCambiar.getText();
        if (!validarString(descripcion, 255)) {
            //Si no cumple los requisitos devuelve falso
            lblMensajeError.setVisible(true);
            lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
            lblMensajeError.setText("Ha ocurrido algun error con la descripcion o es muy larga");
            return false;
        }
        //Validamos la localizacion
        String localizacion = txtFieldLocalizacion.getText();
        if (!validarString(localizacion, 45)) {
            //Si no cumple los requisitos devuelve falso
            lblMensajeError.setVisible(true);
            lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
            lblMensajeError.setText("Ha ocurrido un error con la localizacion o es muy larga");
            return false;
        }
        //Convertimos la fecha en la de la base de datos
        StringConverter<LocalDate> convertidor = convertidorDeFecha();
        datePicker.setConverter(convertidor);
        //Capturamos su valor
        LocalDate fecha = datePicker.getValue();

        if (fecha == null) {
            //Mostramos el error
            lblMensajeError.setVisible(true);
            lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
            lblMensajeError.setText("No se ha elegido fecha");
            return false;
        }

        //Lo volcamos en una variable local
        this.fechaCorrecta = fecha;

        //Capturamos las horas
        String strHoraInicio = txtFieldHoraInicio.getText();
        String strHoraFin = txtFieldHoraFin.getText();
        LocalTime horaInicio;
        LocalTime horaFin;
        //Las validamos
        //Si no son horas validas muestra un error y devuelve falso
        if (!esHoraValida(strHoraInicio) || !esHoraValida(strHoraFin)) {
            lblMensajeError.setVisible(true);
            lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
            lblMensajeError.setText("Una o las dos horas están incorrectas, por favor asegurese de que el formato es correcto: \"HH:mm\"");
            return false;
        } else {
            //Si no estan incorrectas las formateamos y las guardamos en una variable local
            horaInicio = convertirAHora(strHoraInicio);
            horaFin = convertirAHora(strHoraFin);

            this.horaFin = horaFin;
            this.horaInicio = horaInicio;

        }
        //Capturamos la cantidad de personas asegurandonos que sea un numero
        int cantidadPersonas;
        try {
            cantidadPersonas = Integer.parseInt(txtCantidadPersonas.getText());

            //Comprobamos que no sea negativo
            if (cantidadPersonas <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            lblMensajeError.setVisible(true);
            lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
            lblMensajeError.setText("La cantidad de personas maximas insertadas no es un numero o es un valor incorrecto");
            return false;
        }

        //No capturamos los recursos ya que no son obligatorios

        //Si no ha salido false anteriormente es decir que esta bien
        return true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        actividadSugerida = Main.recibirDatosActividad();
        cambiarTamScene();

        //Comprobamos que el usuario que lo esté viendo sea un ofertante
        if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
            rellenarDatos();

            txtFNombreRecurso.setEditable(true);
            txtADescripcionRecurso.setEditable(true);
            txtFCantidadRecurso.setEditable(true);
            rbSi.setDisable(false);
            rbNo.setDisable(false);
        } else {
            //Se pondrá la vista de sugerir actividad ocultando lo innecesario
            recursosLayout.setDisable(true);
            recursosLayout.setVisible(false);
            scrollPane.setDisable(true);
            scrollPane.setVisible(false);
            btnAniadirRecurso.setDisable(true);
            btnAniadirRecurso.setVisible(false);
            btnCrearActividad.setText("Sugerir actividad");
        }

    }


    /**
     * Metodo que solo se llama si se va a crear una actividad sugerida
     */
    public void rellenarDatos() {
        //Si la actividad sugerida existe, es decir se ha entrado mediante una sugerencia, modificamos los datos
        if (actividadSugerida != null) {
            txtFieldTipoAct.setText(actividadSugerida.getTipoActividad());
            txtAreaDescripcionCambiar.setText(actividadSugerida.getDescripcionActividad());
            txtFieldLocalizacion.setText(actividadSugerida.getDireccion());
            datePicker.setValue(actividadSugerida.getFecha().toLocalDate());
            txtFieldHoraInicio.setText(actividadSugerida.getHora_inicio().toString().substring(0, 5));
            txtFieldHoraFin.setText(actividadSugerida.getHora_fin().toString().substring(0, 5));
            txtCantidadPersonas.setText(String.valueOf(actividadSugerida.getCantidad_max_personas()));

            //Si eres un administrador podrás borrar la actividad
            if (((OfertanteModel) Main.recibirDatosUsuario()).isIs_administrador()) {
                btnEliminarActividad.setDisable(false);
            }
            //No rellenamos los recursos porque no se pueden sugerir
        }
    }

    /**
     * Metodo que formatea con la fecha de la base de datos
     *
     * @return
     */
    public StringConverter<LocalDate> convertidorDeFecha() {
        return new StringConverter<LocalDate>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
    }


    /**
     * Comprueba si la hora pasada es valida
     *
     * @param hora
     * @return
     */
    public boolean esHoraValida(String hora) {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("HH:mm");
        try {
            formateador.parse(hora);
            return true;
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metodo que convierte un string a una hora
     *
     * @param hora
     * @return
     */
    public LocalTime convertirAHora(String hora) {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("HH:mm");
        try {
            return LocalTime.parse(hora, formateador);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metodo que valida las cadenas segun su longitud y que no estén vacias
     *
     * @param cadena
     * @param longitud
     * @return devuelve verdadero si no esta vacia la cadena y no supera su longitud
     */
    public boolean validarString(String cadena, int longitud) {
        return !cadena.isBlank() && !cadena.isEmpty() && cadena.length() <= longitud;
    }

    public void cambiarTamScene() {
        Main.cambiarTamVentana(1050, 840);
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
            lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
            lblMensajeError.setText("Ha ocurrido un error con el nombre del recurso o es muy largo");
            return false;
        }
        //Capturamos el valor de la descripcion
        String descripcion = txtADescripcionRecurso.getText();
        if (!validarString(descripcion, 200)) {
            //Si no cumple los requisitos devuelve falso
            lblMensajeError.setVisible(true);
            lblMensajeError.setTextFill(Paint.valueOf("#ff0000"));
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

}
