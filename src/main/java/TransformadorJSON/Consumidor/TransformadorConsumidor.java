package TransformadorJSON.Consumidor;

import Contenedor.ContenedorDatos;
import Modelos.ConsumidorModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TransformadorConsumidor {

    private String nombreConsumidor;
    private String primerApellidoConsumidor;
    private String segundoApellidoConsumidor;
    private String contrasenia;
    private String email_consumidor;

    private String esqueletoConsumidor;

    private final String urlAConectarse = "http://" + ContenedorDatos.URLACONEXION + ":8080/consumidor";

    /**
     * Constructor por defecto de consumidor
     *
     * @param nombreConsumidor
     * @param primerApellidoConsumidor
     * @param segundoApellidoConsumidor
     * @param contrasenia
     * @param email_consumidor
     */
    public TransformadorConsumidor(String nombreConsumidor, String primerApellidoConsumidor, String segundoApellidoConsumidor, String contrasenia, String email_consumidor) {
        this.nombreConsumidor = nombreConsumidor;
        this.primerApellidoConsumidor = primerApellidoConsumidor;
        this.segundoApellidoConsumidor = segundoApellidoConsumidor;
        this.contrasenia = contrasenia;
        this.email_consumidor = email_consumidor;
        this.esqueletoConsumidor = "{\n" +
                "    \"nombreConsumidor\": " + "\"" + nombreConsumidor + "\"" + ",\n" +
                "    \"primerApellidoConsumidor\": " + "\"" + primerApellidoConsumidor + "\"" + ",\n" +
                "    \"segundoApellidoConsumidor\": " + "\"" + segundoApellidoConsumidor + "\"" + ",\n" +
                "    \"contrasenia\": " + "\"" + contrasenia + "\"" + ",\n" +
                "    \"email_consumidor\": " + "\"" + email_consumidor + "\"" + "\n" +
                "}";
    }

    /**
     * Constructor sin parametros de consumidor
     */
    public TransformadorConsumidor() {

    }

    public void rellenarEsqueleto() {

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
                byte[] datosPost = esqueletoConsumidor.getBytes(StandardCharsets.UTF_16);
                escritor.write(datosPost, 0, datosPost.length);

            }

            System.out.println("Esqueleto: " + esqueletoConsumidor);

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
     * Metodo que busca solo un consumidor por el id y lo recibe
     */
    public ConsumidorModel recibirConsumidorPorId(int id) {
        ConsumidorModel consumidor = null;

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

            consumidor = sacarInformacionIndividual(respuesta.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return consumidor;
    }

    public ConsumidorModel recibirConsumidorPorDatos() {
        HttpURLConnection conexion = null;
        ConsumidorModel consumidor = null;

        try {
            // Abrir conexión
            conexion = (HttpURLConnection) new URL(this.urlAConectarse + "/login").openConnection();

            // Configurar la conexión para una solicitud POST
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // Escribir los datos en el cuerpo de la solicitud
            try (OutputStream escritor = conexion.getOutputStream()) {
                byte[] datosPost = esqueletoConsumidor.getBytes(StandardCharsets.UTF_16);
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
                consumidor = sacarInformacionIndividual(respuesta);
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

        return consumidor;
    }

    public ArrayList<ConsumidorModel> recibirInformacionGet() {
        ArrayList<ConsumidorModel> listaConsumidor = new ArrayList<>();

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

            listaConsumidor = sacarInformacionLista(respuesta.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return listaConsumidor;
    }

    /**
     * Metodo que actualiza los datos mediante la id
     * @param id
     */
    public ConsumidorModel actualizarDatosPorId(int id) {
        HttpURLConnection conexion = null;
        ConsumidorModel consumidor = null;

        try {
            // Abrir conexión
            conexion = (HttpURLConnection) new URL(this.urlAConectarse + "/" + String.valueOf(id)).openConnection();

            // Configurar la conexión para una solicitud POST
            conexion.setRequestMethod("PUT");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // Escribir los datos en el cuerpo de la solicitud
            try (OutputStream escritor = conexion.getOutputStream()) {
                byte[] datosPost = esqueletoConsumidor.getBytes(StandardCharsets.UTF_16);
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
                consumidor = sacarInformacionIndividual(respuesta);
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

        return consumidor;
    }

    /**
     * Método que borra al consumidor por el ID
     * @param idConsumidor
     * @return
     */
    public boolean borrarConsumidor(int idConsumidor) {
        HttpURLConnection conexion = null;

        try {
            // Abrir conexión
            conexion = (HttpURLConnection) new URL(this.urlAConectarse + "/" + idConsumidor).openConnection();

            // Configurar la conexión para una solicitud DELETE
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


    public ArrayList<ConsumidorModel> sacarInformacionLista(String datos) {
        ArrayList<ConsumidorModel> listaConsumidor = new ArrayList<>();

        //Convertimos a un array de JSON
        JSONArray arrayJSON = new JSONArray(datos);

        //Recorremos el array
        for (int i = 0; i < arrayJSON.length(); i++) {
            //Lo convertimos en un objeto individual para capturar sus valores
            JSONObject datosJSON = arrayJSON.getJSONObject(i);

            int id_consumidor = datosJSON.getInt("id_consumidor");
            String nombreConsumidor = datosJSON.getString("nombreConsumidor");
            String primerApellidoConsumidor = datosJSON.getString("primerApellidoConsumidor");
            String segundoApellidoConsumidor = datosJSON.getString("segundoApellidoConsumidor");
            String contrasenia = datosJSON.getString("contrasenia");
            String email_consumidor = datosJSON.getString("email_consumidor");

            ConsumidorModel consumidor = new ConsumidorModel(id_consumidor, nombreConsumidor, primerApellidoConsumidor, segundoApellidoConsumidor, contrasenia, email_consumidor);

            listaConsumidor.add(consumidor);
        }

        return listaConsumidor;
    }

    private ConsumidorModel sacarInformacionIndividual(String datos) {
        ConsumidorModel consumidor = null;

        //Lo convertimos en un objeto individual para capturar sus valores
        JSONObject datosJSON = new JSONObject(datos);

        int id_consumidor = datosJSON.getInt("id_consumidor");
        String nombreConsumidor = datosJSON.getString("nombreConsumidor");
        String primerApellidoConsumidor = datosJSON.getString("primerApellidoConsumidor");
        String segundoApellidoConsumidor = datosJSON.getString("segundoApellidoConsumidor");
        String contrasenia = null;
        if (!datosJSON.isNull("contrasenia")) {
            contrasenia = datosJSON.getString("contrasenia");
        }
        String email_consumidor = datosJSON.getString("email_consumidor");

        consumidor = new ConsumidorModel(id_consumidor, nombreConsumidor, primerApellidoConsumidor, segundoApellidoConsumidor, contrasenia, email_consumidor);

        return consumidor;
    }


}
