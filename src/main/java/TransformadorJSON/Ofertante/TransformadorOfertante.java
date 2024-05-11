package TransformadorJSON.Ofertante;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import Contenedor.ContenedorDatos;
import Modelos.OfertanteModel;
import org.json.*;

public class TransformadorOfertante {

    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String contrasenia;
    private String nombreEmpresa;
    private String email_ofertante;
    private final String urlAConectarse = "http://" + ContenedorDatos.URLACONEXION + ":8080/ofertante";

    private String esqueletoOfertante;

    public TransformadorOfertante(String nombre, String primerApellido, String segundoApellido, String contrasenia, String nombreEmpresa, String email_ofertante) {
        this.nombre = nombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.contrasenia = contrasenia;
        this.nombreEmpresa = nombreEmpresa;
        this.email_ofertante = email_ofertante;
        this.esqueletoOfertante = "{\n" +
                "    \"nombreOfertante\": " + "\"" + nombre + "\"" + ",\n" +
                "    \"primerApellidoOfertante\": " + "\"" + primerApellido + "\"" + ",\n" +
                "    \"segundoApellidoOfertante\": " + "\"" + segundoApellido + "\"" + ",\n" +
                "    \"contrasenia\": " + "\"" + contrasenia + "\"" + ",\n" +
                "    \"nombreEmpresa\": " + "\"" + nombreEmpresa + "\"" + ",\n" +
                "    \"email_ofertante\": " + "\"" + email_ofertante + "\"" + ",\n" +
                "    \"is_administrador\": " + "\"" + false + "\"" + "\n" +
                "}";
    }

    public TransformadorOfertante() {
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
                byte[] datosPost = esqueletoOfertante.getBytes(StandardCharsets.UTF_16);
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

    public OfertanteModel recibirOfertantePorDatos() {
        HttpURLConnection conexion = null;
        OfertanteModel ofertante = null;

        try {
            // Abrir conexión
            conexion = (HttpURLConnection) new URL(this.urlAConectarse + "/login").openConnection();

            // Configurar la conexión para una solicitud POST
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // Escribir los datos en el cuerpo de la solicitud
            try (OutputStream escritor = conexion.getOutputStream()) {
                byte[] datosPost = esqueletoOfertante.getBytes(StandardCharsets.UTF_16);
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
                ofertante = sacarInformacionIndividual(respuesta);
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

        return ofertante;
    }

    public ArrayList<OfertanteModel> recibirInformacionGet() {
        ArrayList<OfertanteModel> listaOfertantes = new ArrayList<>();
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

            listaOfertantes = sacarInformacionLista(respuesta.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return listaOfertantes;
    }

    /**
     * Metodo que actualiza los datos mediante la id
     * @param id
     */
    public OfertanteModel actualizarDatosPorId(int id) {
        HttpURLConnection conexion = null;
        OfertanteModel ofertante = null;

        try {
            // Abrir conexión
            conexion = (HttpURLConnection) new URL(this.urlAConectarse + "/" + String.valueOf(id)).openConnection();

            // Configurar la conexión para una solicitud POST
            conexion.setRequestMethod("PUT");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // Escribir los datos en el cuerpo de la solicitud
            try (OutputStream escritor = conexion.getOutputStream()) {
                byte[] datosPost = esqueletoOfertante.getBytes(StandardCharsets.UTF_16);
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
                ofertante = sacarInformacionIndividual(respuesta);
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

        return ofertante;
    }

    /**
     * Metodo que busca solo un ofertante y lo recibe
     */
    public OfertanteModel recibirOfertantePorId(int id) {
        OfertanteModel ofertante = null;

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

            ofertante = sacarInformacionIndividual(respuesta.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ofertante;
    }

    public ArrayList<OfertanteModel> sacarInformacionLista(String datos) {
        ArrayList<OfertanteModel> listaOfertantes = new ArrayList<>();

        //Convertimos a un array de JSON
        JSONArray arrayJSON = new JSONArray(datos);

        //Recorremos el array
        for (int i = 0; i < arrayJSON.length(); i++) {
            //Lo convertimos en un objeto individual para capturar sus valores
            JSONObject datosJSON = arrayJSON.getJSONObject(i);

            int id_ofertante = datosJSON.getInt("id_ofertante");
            String nombreOfertante = datosJSON.getString("nombreOfertante");
            String primerApellidoOfertante = datosJSON.getString("primerApellidoOfertante");
            String segundoApellidoOfertante = datosJSON.getString("segundoApellidoOfertante");
            String contrasenia = datosJSON.getString("contrasenia");
            String nombreEmpresa = String.valueOf(datosJSON.get("nombreEmpresa"));
            String email_ofertante = datosJSON.getString("email_ofertante");
            boolean is_administrador = datosJSON.getBoolean("is_administrador");

            OfertanteModel ofertante = new OfertanteModel(id_ofertante, nombreOfertante, primerApellidoOfertante, segundoApellidoOfertante, contrasenia, nombreEmpresa, email_ofertante, is_administrador);

            listaOfertantes.add(ofertante);
        }

        return listaOfertantes;
    }

    private OfertanteModel sacarInformacionIndividual(String datos) {
        OfertanteModel ofertante = null;

        JSONObject datosJSON = new JSONObject(datos);

        int id_ofertante = datosJSON.getInt("id_ofertante");
        String nombreOfertante = datosJSON.getString("nombreOfertante");
        String primerApellidoOfertante = datosJSON.getString("primerApellidoOfertante");
        String segundoApellidoOfertante = datosJSON.getString("segundoApellidoOfertante");
        String contrasenia = null;
        if (!datosJSON.isNull("contrasenia")) {
            contrasenia = datosJSON.getString("contrasenia");
        }
        String nombreEmpresa = String.valueOf(datosJSON.get("nombreEmpresa"));
        String email_ofertante = datosJSON.getString("email_ofertante");
        boolean is_administrador = datosJSON.getBoolean("is_administrador");

        ofertante = new OfertanteModel(id_ofertante, nombreOfertante, primerApellidoOfertante, segundoApellidoOfertante, contrasenia, nombreEmpresa, email_ofertante, is_administrador);

        return ofertante;
    }
}