package TransformadorJSON.Recursos;

import Modelos.ActividadModel;
import Modelos.OfertanteModel;
import Modelos.ParticipacionModel;
import Modelos.RecursosModel;
import TransformadorJSON.Actividad.TransformadorActividad;
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

public class TransformadorRecursos {

    private int actividad;
    private String nombre_recurso;
    private String descripcion;
    private int cantidad;
    private boolean is_ofertada_por_ofertante;

    private String esqueletoRecurso;
    private final String urlAConectarse = "http://localhost:8080/recurso";

    public TransformadorRecursos(int actividad, String nombre_recurso, String descripcion, int cantidad, boolean is_ofertada_por_ofertante) {
        this.actividad = actividad;
        this.nombre_recurso = nombre_recurso;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.is_ofertada_por_ofertante = is_ofertada_por_ofertante;
        this.esqueletoRecurso = "{\n" +
                "    \"actividad\": {\n" +
                "        \"id_actividad\":" + "\"" + actividad + "\"" + "\n" +
                "    },\n" +
                "    \"nombre_recurso\":" + "\"" + nombre_recurso + "\"" + ",\n" +
                "    \"descripcion\":" + "\"" + descripcion + "\"" + ",\n" +
                "    \"cantidad\":" + "\"" + cantidad + "\"" + ",\n" +
                "    \"is_ofertada_por_ofertante\":" + "\"" + is_ofertada_por_ofertante + "\"" + "\n" +
                "}";
    }

    public TransformadorRecursos() {
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
                byte[] datosPost = esqueletoRecurso.getBytes(StandardCharsets.UTF_16);
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

    /**
     * Metodo que recibe un recurso en especifico
     */
    public RecursosModel recibirRecursoPorId(int id) {
        RecursosModel recurso = null;

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

            recurso = sacarInformacionIndividual(respuesta.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return recurso;
    }

    /**
     * Metodo que hace una peticion DELETE y borra por el id del recurso
     * @param id
     * @return
     */
    public boolean borrarPorId(int id) {
        HttpURLConnection conexion = null;

        try {
            // Abrir conexión
            conexion = (HttpURLConnection) new URL(this.urlAConectarse + "/" + id).openConnection();

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

    public ArrayList<RecursosModel> recibirInformacionGet() {
        ArrayList<RecursosModel> listaRecursos = new ArrayList<>();

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

            listaRecursos = sacarInformacionLista(respuesta.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return listaRecursos;
    }

    public ArrayList<RecursosModel> recibirListaRecursosPorIdActividad(ActividadModel actividad) {
        HttpURLConnection conexion = null;
        ArrayList<RecursosModel> listaRecursos = null;

        try {
            // Abrir conexión
            conexion = (HttpURLConnection) new URL(this.urlAConectarse + "/porIdActividad").openConnection();

            // Configurar la conexión para una solicitud POST
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            //Creamos un esqueleto de la actividad provisional
            String esqueletoProvisionalActividad = "{\n" +
                    "    \"id_actividad\":" + "\"" + actividad.getId_actividad() + "\"" + "\n" +
                    "}";

            // Escribir los datos en el cuerpo de la solicitud
            try (OutputStream escritor = conexion.getOutputStream()) {
                byte[] datosPost = esqueletoProvisionalActividad.getBytes(StandardCharsets.UTF_16);
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
                listaRecursos = sacarInformacionLista(respuesta);
            } else if (conexion.getResponseCode() == 401) {
                //Devuelve 401 si hay algun error
                return null;
            } else {
                return null;
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

        return listaRecursos;

    }


    public ArrayList<RecursosModel> sacarInformacionLista(String datos) {
        ArrayList<RecursosModel> listaRecursos = new ArrayList<>();

        //Convertimos a un array de JSON
        JSONArray arrayJSON = new JSONArray(datos);

        //Recorremos el array
        for (int i = 0; i < arrayJSON.length(); i++) {
            //Lo convertimos en un objeto individual para capturar sus valores
            JSONObject datosJSON = arrayJSON.getJSONObject(i);

            int id_recurso = datosJSON.getInt("id_recurso");

            //Convertimos las actividades en un objeto
            JSONObject datosActividadesJSON = datosJSON.getJSONObject("actividad");
            //Sacamos el id de la actividad
            int id_actividad = datosActividadesJSON.getInt("id_actividad");
            //La pasamos para que nos busque por el id

            //Capturamos el valor
            ActividadModel actividadDelRecurso = new TransformadorActividad().recibirActividadPorId(id_actividad);

            String nombre_recurso = datosJSON.getString("nombre_recurso");
            String descripcion = datosJSON.getString("descripcion");
            int cantidad = datosJSON.getInt("cantidad");
            boolean is_ofertada_por_ofertante = datosJSON.getBoolean("is_ofertada_por_ofertante");

            RecursosModel recursos = new RecursosModel(id_recurso, actividadDelRecurso, nombre_recurso, descripcion, cantidad, is_ofertada_por_ofertante);

            listaRecursos.add(recursos);
        }

        return listaRecursos;
    }

    private RecursosModel sacarInformacionIndividual(String datos) {
        RecursosModel recurso = null;

        JSONObject datosJSON = new JSONObject(datos);

        int id_recurso = datosJSON.getInt("id_recurso");

        //Convertimos las actividades en un objeto
        JSONObject datosActividadesJSON = datosJSON.getJSONObject("actividad");
        //Sacamos el id de la actividad
        int id_actividad = datosActividadesJSON.getInt("id_actividad");
        //La pasamos para que nos busque por el id

        //Capturamos el valor
        ActividadModel actividadDelRecurso = new TransformadorActividad().recibirActividadPorId(id_actividad);

        String nombre_recurso = datosJSON.getString("nombre_recurso");
        String descripcion = datosJSON.getString("descripcion");
        int cantidad = datosJSON.getInt("cantidad");
        boolean is_ofertada_por_ofertante = datosJSON.getBoolean("is_ofertada_por_ofertante");

        recurso = new RecursosModel(id_recurso, actividadDelRecurso, nombre_recurso, descripcion, cantidad, is_ofertada_por_ofertante);


        return recurso;
    }
}
