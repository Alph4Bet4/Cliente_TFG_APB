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
    private ActividadModel.tipoEstado estado;

    private String esqueletoActividad;

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
     * @param estado
     */
    public TransformadorActividad(String tipoActividad, String descripcionActividad, String direccion, Date fecha, Time hora_inicio, Time hora_fin, int cantidad_max_personas, int cantidad_actual_personas, int id_creador_ofertante, ActividadModel.tipoEstado estado) {
        this.tipoActividad = tipoActividad;
        this.descripcionActividad = descripcionActividad;
        this.direccion = direccion;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.cantidad_max_personas = cantidad_max_personas;
        this.cantidad_actual_personas = cantidad_actual_personas;
        this.id_creador_ofertante = id_creador_ofertante;
        this.estado = estado;
        this.esqueletoActividad = "{\n" +
                "    \"tipoActividad\":" + "\"" + tipoActividad + "\"" + ",\n" +
                "    \"descripcionActividad\":" + "\"" + descripcionActividad + "\"" + ",\n" +
                "    \"direccion\":" + "\"" + direccion + "\"" + ",\n" +
                "    \"fecha\":" + "\"" + fecha + "\"" + ",\n" +
                "    \"hora_inicio\":" + "\"" + hora_inicio + "\"" + ",\n" +
                "    \"hora_fin\":" + "\"" + hora_fin + "\"" + ",\n" +
                "    \"cantidad_max_personas\":" + "\"" + cantidad_max_personas + "\"" + ",\n" +
                "    \"cantidad_actual_personas\":" + "\"" + cantidad_actual_personas + "\"" + ",\n" +
                "    \"estadoActividad\":" + "\"" + estado + "\"" + ",\n" +
                "    \"creador_ofertante\": {\n" +
                "            \"id_ofertante\":" + "\"" + id_creador_ofertante + "\"" +
                "        }\n" +
                "}";
    }

    /**
     * Constructor sin estado ni cantidad actual de personas para crear actividades siendo ofertante
     *
     * @param tipoActividad
     * @param descripcionActividad
     * @param direccion
     * @param fecha
     * @param hora_inicio
     * @param hora_fin
     * @param cantidad_max_personas
     * @param id_creador_ofertante
     */
    public TransformadorActividad(String tipoActividad, String descripcionActividad, String direccion, Date fecha, Time hora_inicio, Time hora_fin, int cantidad_max_personas, int id_creador_ofertante) {
        this.tipoActividad = tipoActividad;
        this.descripcionActividad = descripcionActividad;
        this.direccion = direccion;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.cantidad_max_personas = cantidad_max_personas;
        this.id_creador_ofertante = id_creador_ofertante;
        this.esqueletoActividad = "{\n" +
                "    \"tipoActividad\":" + "\"" + tipoActividad + "\"" + ",\n" +
                "    \"descripcionActividad\":" + "\"" + descripcionActividad + "\"" + ",\n" +
                "    \"direccion\":" + "\"" + direccion + "\"" + ",\n" +
                "    \"fecha\":" + "\"" + fecha + "\"" + ",\n" +
                "    \"hora_inicio\":" + "\"" + hora_inicio + "\"" + ",\n" +
                "    \"hora_fin\":" + "\"" + hora_fin + "\"" + ",\n" +
                "    \"cantidad_max_personas\":" + "\"" + cantidad_max_personas + "\"" + ",\n" +
                "    \"estadoActividad\":" + "\"" + ActividadModel.tipoEstado.Disponible + "\"" + ",\n" +
                "    \"creador_ofertante\": {\n" +
                "            \"id_ofertante\":" + "\"" + id_creador_ofertante + "\"" +
                "        }\n" +
                "}";
    }

    /**
     * Constructor sin estado para actualizar actividades siendo ofertante
     *
     * @param tipoActividad
     * @param descripcionActividad
     * @param direccion
     * @param fecha
     * @param hora_inicio
     * @param hora_fin
     * @param cantidad_max_personas
     * @param id_creador_ofertante
     */
    public TransformadorActividad(String tipoActividad, String descripcionActividad, String direccion, Date fecha, Time hora_inicio, Time hora_fin, int cantidad_actual_personas, int cantidad_max_personas, int id_creador_ofertante) {
        this.tipoActividad = tipoActividad;
        this.descripcionActividad = descripcionActividad;
        this.direccion = direccion;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.cantidad_max_personas = cantidad_max_personas;
        this.cantidad_actual_personas = cantidad_actual_personas;
        this.id_creador_ofertante = id_creador_ofertante;
        this.esqueletoActividad = "{\n" +
                "    \"tipoActividad\":" + "\"" + tipoActividad + "\"" + ",\n" +
                "    \"descripcionActividad\":" + "\"" + descripcionActividad + "\"" + ",\n" +
                "    \"direccion\":" + "\"" + direccion + "\"" + ",\n" +
                "    \"fecha\":" + "\"" + fecha + "\"" + ",\n" +
                "    \"hora_inicio\":" + "\"" + hora_inicio + "\"" + ",\n" +
                "    \"hora_fin\":" + "\"" + hora_fin + "\"" + ",\n" +
                "    \"cantidad_max_personas\":" + "\"" + cantidad_max_personas + "\"" + ",\n" +
                "    \"cantidad_actual_personas\":" + "\"" + cantidad_actual_personas + "\"" + ",\n" +
                "    \"estadoActividad\":" + "\"" + ActividadModel.tipoEstado.Disponible + "\"" + ",\n" +
                "    \"creador_ofertante\": {\n" +
                "            \"id_ofertante\":" + "\"" + id_creador_ofertante + "\"" +
                "        }\n" +
                "}";
    }

    /**
     * Constructor sin estado ni cantidad actual de personas para crear actividades siendo consumidor
     *
     * @param tipoActividad
     * @param descripcionActividad
     * @param direccion
     * @param fecha
     * @param hora_inicio
     * @param hora_fin
     * @param cantidad_max_personas
     */
    public TransformadorActividad(String tipoActividad, String descripcionActividad, String direccion, Date fecha, Time hora_inicio, Time hora_fin, int cantidad_max_personas) {
        this.tipoActividad = tipoActividad;
        this.descripcionActividad = descripcionActividad;
        this.direccion = direccion;
        this.fecha = fecha;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.cantidad_max_personas = cantidad_max_personas;
        this.esqueletoActividad = "{\n" +
                "    \"tipoActividad\":" + "\"" + tipoActividad + "\"" + ",\n" +
                "    \"descripcionActividad\":" + "\"" + descripcionActividad + "\"" + ",\n" +
                "    \"direccion\":" + "\"" + direccion + "\"" + ",\n" +
                "    \"fecha\":" + "\"" + fecha + "\"" + ",\n" +
                "    \"hora_inicio\":" + "\"" + hora_inicio + "\"" + ",\n" +
                "    \"hora_fin\":" + "\"" + hora_fin + "\"" + ",\n" +
                "    \"cantidad_max_personas\":" + "\"" + cantidad_max_personas + "\"" + ",\n" +
                "    \"estadoActividad\":" + "\"" + ActividadModel.tipoEstado.Disponible + "\"" + "\n" +
                "}";
    }

    /**
     * Constructor sin parametros
     */
    public TransformadorActividad() {

    }

    public boolean enviarInformacionPost() {
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

            String respuesta;
            // Leer la respuesta de la API
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Respuesta de la API: " + response.toString());
                respuesta = response.toString();
            }

            if (conexion.getResponseCode() == 200 && (!respuesta.isEmpty() || !respuesta.isBlank())) {
                //Devuelve 200 si esta correcto
                return true;
            } else if (conexion.getResponseCode() == 401) {
                //Devuelve 401 si hay algun error
                return false;
            } else {
                return false;
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

    public ActividadModel enviarInformacionPut(int id) {
        HttpURLConnection conexion = null;
        ActividadModel actividad = null;
        try {
            // Abrir conexión
            conexion = (HttpURLConnection) new URL(this.urlAConectarse + "/" + id).openConnection();

            // Configurar la conexión para una solicitud POST
            conexion.setRequestMethod("PUT");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // Escribir los datos en el cuerpo de la solicitud
            try (OutputStream escritor = conexion.getOutputStream()) {
                byte[] datosPost = esqueletoActividad.getBytes(StandardCharsets.UTF_16);
                escritor.write(datosPost, 0, datosPost.length);

            }

            String respuesta;
            // Leer la respuesta de la API
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Respuesta de la API: " + response.toString());
                respuesta = response.toString();
            }

            if (conexion.getResponseCode() == 200 && (!respuesta.isEmpty() || !respuesta.isBlank())) {
                //Devuelve 200 si esta correcto
                actividad = sacarInformacionIndividual(respuesta);
                return actividad;
            } else if (conexion.getResponseCode() == 401) {
                //Devuelve 401 si hay algun error
                return actividad;
            } else {
                return actividad;
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
     * Metodo que borra una actividad por el metodo DELETE
     *
     * @param idActividad
     * @return
     */
    public boolean borrarPorId(int idActividad) {
        HttpURLConnection conexion = null;

        try {
            // Abrir conexión
            conexion = (HttpURLConnection) new URL(this.urlAConectarse + "/" + idActividad).openConnection();

            // Configurar la conexión para una solicitud POST
            conexion.setRequestMethod("DELETE");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            String respuesta;
            // Leer la respuesta de la API
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Respuesta de la API: " + response.toString());
                respuesta = response.toString();
            }

            if (conexion.getResponseCode() == 200 && (!respuesta.isEmpty() || !respuesta.isBlank())) {
                //Devuelve 200 si esta correcto
                return true;
            } else if (conexion.getResponseCode() == 401) {
                //Devuelve 401 si hay algun error
                return false;
            } else {
                return false;
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
     * Metodo que crea una actividad y devuelve los datos para posteriormente poder añadirle recursos
     *
     * @return
     */
    public ActividadModel enviarInformacionPostYRecibirDatos() {
        HttpURLConnection conexion = null;
        ActividadModel actividad = null;
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

            String respuesta;
            // Leer la respuesta de la API
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Respuesta de la API: " + response.toString());
                respuesta = response.toString();
            }

            if (conexion.getResponseCode() == 200 && (!respuesta.isEmpty() || !respuesta.isBlank())) {
                //Devuelve 200 si esta correcto
                actividad = sacarInformacionIndividual(respuesta);
                return actividad;
            } else if (conexion.getResponseCode() == 401) {
                //Devuelve 401 si hay algun error
                return actividad;
            } else {
                return actividad;
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
            HttpURLConnection conexion = (HttpURLConnection) new URL(this.urlAConectarse + "/" + id).openConnection();
            conexion.setRequestMethod("GET");

            // Leer la respuesta de la API
            StringBuilder respuesta = new StringBuilder();

            try (BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()))) {
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
            HttpURLConnection conexion = (HttpURLConnection) new URL(this.urlAConectarse).openConnection();
            conexion.setRequestMethod("GET");

            // Leer la respuesta de la API
            StringBuilder respuesta = new StringBuilder();

            try (BufferedReader entrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()))) {
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
            String direccion = datosJSON.getString("direccion");
            Date fecha = Date.valueOf(datosJSON.getString("fecha"));
            Time hora_inicio = Time.valueOf(datosJSON.getString("hora_inicio"));
            Time hora_fin = Time.valueOf(datosJSON.getString("hora_fin"));
            int cantidad_max_personas = datosJSON.getInt("cantidad_max_personas");
            int cantidad_actual_personas = datosJSON.getInt("cantidad_actual_personas");
            ActividadModel.tipoEstado estado = datosJSON.getEnum(ActividadModel.tipoEstado.class, "estadoActividad");

            OfertanteModel Creador_ofertante = null;
            JSONObject datosOfertanteJSON = null;

            //Comprobamos que el valor que vamos a capturar no sea null
            if (!datosJSON.isNull("creador_ofertante")) {
                //Convertimos al ofertante en un objeto individual para capturar sus valores
                datosOfertanteJSON = datosJSON.getJSONObject("creador_ofertante");
            }

            //Si esta no esta vacio recogemos los datos
            if (datosOfertanteJSON != null && !datosOfertanteJSON.isEmpty()) {
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

            ActividadModel actividad = new ActividadModel(id_actividad, tipoActividad, descripcionActividad, direccion, fecha, hora_inicio, hora_fin, cantidad_max_personas, cantidad_actual_personas, estado, Creador_ofertante);

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
        String direccion = datosJSON.getString("direccion");
        Date fecha = Date.valueOf(datosJSON.getString("fecha"));
        Time hora_inicio = Time.valueOf(datosJSON.getString("hora_inicio"));
        Time hora_fin = Time.valueOf(datosJSON.getString("hora_fin"));
        int cantidad_max_personas = datosJSON.getInt("cantidad_max_personas");
        int cantidad_actual_personas = datosJSON.getInt("cantidad_actual_personas");
        ActividadModel.tipoEstado estado = datosJSON.getEnum(ActividadModel.tipoEstado.class, "estadoActividad");

        OfertanteModel Creador_ofertante = null;

        JSONObject datosOfertanteJSON = null;

        //Comprobamos que el valor que vamos a capturar no sea null
        if (!datosJSON.isNull("creador_ofertante")) {
            //Convertimos al ofertante en un objeto individual para capturar sus valores
            datosOfertanteJSON = datosJSON.getJSONObject("creador_ofertante");
        }

        //Si esta no esta vacio recogemos los datos
        if (datosOfertanteJSON != null && !datosOfertanteJSON.isEmpty()) {
            int id_ofertante = datosOfertanteJSON.getInt("id_ofertante");
            String nombreOfertante = null;
            String primerApellidoOfertante = null;
            String segundoApellidoOfertante = null;
            String contrasenia = null;
            String nombreEmpresa = null;
            String email_ofertante = null;
            boolean is_administrador = false;
            if (!datosJSON.isNull("nombreOfertante")) {
                nombreOfertante = datosOfertanteJSON.getString("nombreOfertante");
            }
            if (!datosJSON.isNull("primerApellidoOfertante")) {
                primerApellidoOfertante = datosOfertanteJSON.getString("primerApellidoOfertante");
            }
            if (!datosJSON.isNull("segundoApellidoOfertante")) {
                segundoApellidoOfertante = datosOfertanteJSON.getString("segundoApellidoOfertante");
            }
            if (!datosJSON.isNull("contrasenia")) {
                contrasenia = datosOfertanteJSON.getString("contrasenia");
            }
            if (!datosJSON.isNull("nombreEmpresa")) {
                nombreEmpresa = String.valueOf(datosOfertanteJSON.get("nombreEmpresa"));
            }
            if (!datosJSON.isNull("email_ofertante")) {
                email_ofertante = datosOfertanteJSON.getString("email_ofertante");
            }
            if (!datosJSON.isNull("is_administrador")) {
                is_administrador = datosOfertanteJSON.getBoolean("is_administrador");
            }


            Creador_ofertante = new OfertanteModel(id_ofertante, nombreOfertante, primerApellidoOfertante, segundoApellidoOfertante, contrasenia, nombreEmpresa, email_ofertante, is_administrador);
        }

        actividad = new ActividadModel(id_actividad, tipoActividad, descripcionActividad, direccion, fecha, hora_inicio, hora_fin, cantidad_max_personas, cantidad_actual_personas, estado, Creador_ofertante);

        return actividad;
    }


}
