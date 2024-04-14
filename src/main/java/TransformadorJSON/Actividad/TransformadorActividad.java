package TransformadorJSON.Actividad;

import Modelos.ActividadModel;
import Modelos.OfertanteModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class TransformadorActividad {

    private String tipoActividad;
    private String descripcionActividad;
    private String direccion;
    private Date fecha;
    private Time hora_inicio;
    private Time hora_fin;
    private int cantidad_max_personas;
    private int cantidad_actual_personas;
    private int id_creador_ofertante;

    private final String esqueletoActividad = "{\n" +
            "    \"tipoActividad\":" + "\"" + tipoActividad + "\"" + ",\n" +
            "    \"descripcionActividad\":" + "\"" + descripcionActividad + "\"" + ",\n" +
            "    \"direccion\":" + "\"" + direccion + "\"" + ",\n" +
            "    \"fecha\":" + "\"" + fecha + "\"" + ",\n" +
            "    \"hora_inicio\":" + "\"" + hora_fin + "\"" + ",\n" +
            "    \"hora_fin\":" + "\"" + hora_fin + "\"" + ",\n" +
            "    \"cantidad_max_personas\":" + "\"" + cantidad_max_personas + "\"" + ",\n" +
            "    \"cantidad_actual_personas\":" + "\"" + cantidad_actual_personas + "\"" + ",\n" +
            "    \"creador_ofertante\": {\n" +
            "            \"id_ofertante\":" + "\"" + id_creador_ofertante + "\"" +
            "        }\n" +
            "}";

    private final String urlAConectarse = "http://localhost:8080/actividad";

    /**
     * Metodo por defecto del transformador de actividad
     *
     * @param tipoActividad
     * @param descripcionActividad
     * @param direccion
     * @param fecha
     * @param hora_inicio
     * @param hora_fin
     * @param cantidad_max_personas
     * @param cantidad_actual_personas
     * @param id_creador_ofertante
     */
    public TransformadorActividad(String tipoActividad, String descripcionActividad, String direccion, Date fecha, Time hora_inicio, Time hora_fin, int cantidad_max_personas, int cantidad_actual_personas, int id_creador_ofertante) {
        this.tipoActividad = tipoActividad;
        this.descripcionActividad = descripcionActividad;
        this.direccion = direccion;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.cantidad_max_personas = cantidad_max_personas;
        this.cantidad_actual_personas = cantidad_actual_personas;
        this.id_creador_ofertante = id_creador_ofertante;
    }

    /**
     * Constructor sin parametros
     */
    public TransformadorActividad() {

    }

    public void enviarInformacionPost() {
        HttpURLConnection conexion = null;

        try {
            // Abrir conexión
            conexion = (HttpURLConnection) new URL(this.urlAConectarse).openConnection();

            // Configurar la conexión para una solicitud POST
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // Escribir los datos en el cuerpo de la solicitud
            try (OutputStream escritor = conexion.getOutputStream()) {
                byte[] datosPost = esqueletoActividad.getBytes(StandardCharsets.UTF_16);
                escritor.write(datosPost, 0, datosPost.length);

            }

            // Leer la respuesta de la API
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_16))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Respuesta de la API: " + response.toString());
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                // Cerrar la conexión
                if (conexion != null) {
                    conexion.disconnect();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Metodo que busca una sola actividad por el id y lo recibe
     *
     * @return
     */
    public ActividadModel recibirActividadPorId(int id) {
        ActividadModel actividad = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(this.urlAConectarse+ "/" + id).openConnection();
            connection.setRequestMethod("GET");

            // Leer la respuesta de la API
            StringBuilder respuesta = new StringBuilder();

            try (BufferedReader entrada = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String lineaLeer;
                while ((lineaLeer = entrada.readLine()) != null) {
                    respuesta.append(lineaLeer);
                }

            }

            actividad = sacarInformacionIndividual(respuesta.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return actividad;
    }

    public ArrayList<ActividadModel> recibirInformacionGet() {
        ArrayList<ActividadModel> listaActividades = new ArrayList<>();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(this.urlAConectarse).openConnection();
            connection.setRequestMethod("GET");

            // Leer la respuesta de la API
            StringBuilder respuesta = new StringBuilder();

            try (BufferedReader entrada = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String lineaLeer;
                while ((lineaLeer = entrada.readLine()) != null) {
                    respuesta.append(lineaLeer);
                }

            }

            listaActividades = sacarInformacionLista(respuesta.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return listaActividades;
    }

    public ArrayList<ActividadModel> sacarInformacionLista(String datos) {
        ArrayList<ActividadModel> listaActividades = new ArrayList<>();

        //Convertimos a un array de JSON
        JSONArray arrayJSON = new JSONArray(datos);

        //Recorremos el array
        for (int i = 0; i < arrayJSON.length(); i++) {
            //Lo convertimos en un objeto individual para capturar sus valores
            JSONObject datosJSON = arrayJSON.getJSONObject(i);

            int id_actividad = datosJSON.getInt("id_actividad");
            String tipoActividad = datosJSON.getString("tipoActividad");
            String descripcionActividad = datosJSON.getString("descripcionActividad");
            String direccion = datosJSON.getString("tipoActividad");
            Date fecha = Date.valueOf(datosJSON.getString("fecha"));
            Time hora_inicio = Time.valueOf(datosJSON.getString("hora_inicio"));
            Time hora_fin = Time.valueOf(datosJSON.getString("hora_fin"));
            int cantidad_max_personas = datosJSON.getInt("cantidad_max_personas");
            int cantidad_actual_personas = datosJSON.getInt("cantidad_actual_personas");
            OfertanteModel Creador_ofertante = null;

            //Convertimos al ofertante en un objeto individual para capturar sus valores
            JSONObject datosOfertanteJSON = datosJSON.getJSONObject("creador_ofertante");

            //Si esta no esta vacio recogemos los datos
            if (datosOfertanteJSON.isEmpty() == false) {
                int id_ofertante = datosOfertanteJSON.getInt("id_ofertante");
                String nombreOfertante = datosOfertanteJSON.getString("nombreOfertante");
                String primerApellidoOfertante = datosOfertanteJSON.getString("primerApellidoOfertante");
                String segundoApellidoOfertante = datosOfertanteJSON.getString("segundoApellidoOfertante");
                String contrasenia = datosOfertanteJSON.getString("contrasenia");
                String nombreEmpresa = String.valueOf(datosOfertanteJSON.get("nombreEmpresa"));
                String email_ofertante = datosOfertanteJSON.getString("email_ofertante");
                boolean is_administrador = datosOfertanteJSON.getBoolean("is_administrador");

                Creador_ofertante = new OfertanteModel(id_ofertante, nombreOfertante, primerApellidoOfertante, segundoApellidoOfertante, contrasenia, nombreEmpresa, email_ofertante, is_administrador);
            }

            ActividadModel actividad = new ActividadModel(id_actividad, tipoActividad, descripcionActividad, direccion, fecha, hora_inicio, hora_fin, cantidad_max_personas, cantidad_actual_personas, Creador_ofertante);

            listaActividades.add(actividad);
        }

        return listaActividades;
    }

    public ActividadModel sacarInformacionIndividual(String datos) {
        ActividadModel actividad = null;

        //Convertimos a un objeto de JSON
        JSONObject datosJSON = new JSONObject(datos);

        int id_actividad = datosJSON.getInt("id_actividad");
        String tipoActividad = datosJSON.getString("tipoActividad");
        String descripcionActividad = datosJSON.getString("descripcionActividad");
        String direccion = datosJSON.getString("tipoActividad");
        Date fecha = Date.valueOf(datosJSON.getString("fecha"));
        Time hora_inicio = Time.valueOf(datosJSON.getString("hora_inicio"));
        Time hora_fin = Time.valueOf(datosJSON.getString("hora_fin"));
        int cantidad_max_personas = datosJSON.getInt("cantidad_max_personas");
        int cantidad_actual_personas = datosJSON.getInt("cantidad_actual_personas");
        OfertanteModel Creador_ofertante = null;

        //Convertimos al ofertante en un objeto individual para capturar sus valores
        JSONObject datosOfertanteJSON = datosJSON.getJSONObject("creador_ofertante");

        //Si esta no esta vacio recogemos los datos
        if (datosOfertanteJSON.isEmpty() == false) {
            int id_ofertante = datosOfertanteJSON.getInt("id_ofertante");
            String nombreOfertante = datosOfertanteJSON.getString("nombreOfertante");
            String primerApellidoOfertante = datosOfertanteJSON.getString("primerApellidoOfertante");
            String segundoApellidoOfertante = datosOfertanteJSON.getString("segundoApellidoOfertante");
            String contrasenia = datosOfertanteJSON.getString("contrasenia");
            String nombreEmpresa = String.valueOf(datosOfertanteJSON.get("nombreEmpresa"));
            String email_ofertante = datosOfertanteJSON.getString("email_ofertante");
            boolean is_administrador = datosOfertanteJSON.getBoolean("is_administrador");

            Creador_ofertante = new OfertanteModel(id_ofertante, nombreOfertante, primerApellidoOfertante, segundoApellidoOfertante, contrasenia, nombreEmpresa, email_ofertante, is_administrador);
        }

        actividad = new ActividadModel(id_actividad, tipoActividad, descripcionActividad, direccion, fecha, hora_inicio, hora_fin, cantidad_max_personas, cantidad_actual_personas, Creador_ofertante);

        return actividad;
    }
}
