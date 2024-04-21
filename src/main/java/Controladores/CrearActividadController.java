package Controladores;

import Modelos.ActividadModel;
import Modelos.ConsumidorModel;
import Modelos.OfertanteModel;
import Modelos.RecursosModel;
import TransformadorJSON.Actividad.TransformadorActividad;
import TransformadorJSON.Recursos.TransformadorRecursos;
import TransformadorJSON.Sugerencia.TransformadorSugerencia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class CrearActividadController implements Initializable {

    LocalDate fechaCorrecta;
    LocalTime horaInicio;
    LocalTime horaFin;
    ArrayList<RecursosModel> listaRecursos = new ArrayList<>();
    ActividadModel actividadSugerida;

    @FXML
    private Button btnAniadirRecurso;

    @FXML
    private Button btnCrearActividad;

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
    private TextField txtFieldTipoAct;

    @FXML
    void addRecursoNuevo(ActionEvent event) {
        //TODO
    }

    @FXML
    void crearActividad(ActionEvent event) {
        //Comprobamos todos los campos
        if (comprobacionesCampos()) {
            //Nos aseguramos que el usuario que sea un ofertante para capturar su id, sino no lo hacemos y lo convertimos a una actividad sugerida
            if (Main.recibirDatosUsuario() instanceof OfertanteModel) {
                enviarActividad();
            } else if (Main.recibirDatosUsuario() instanceof ConsumidorModel) {
                enviarActividad();
            }
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
     * Metodo que actualiza la vista de los recursos
     */
    public void aniadirRecurso() {
        //TODO
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
            //TODO hacer un put actualizando la informacion
            rellenarDatos();
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
            txtFieldHoraInicio.setText(actividadSugerida.getHora_inicio().toString());
            txtFieldHoraFin.setText(actividadSugerida.getHora_fin().toString());
            txtCantidadPersonas.setText(String.valueOf(actividadSugerida.getCantidad_max_personas()));
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
        Main.cambiarTamVentana(760, 840);
    }
}
