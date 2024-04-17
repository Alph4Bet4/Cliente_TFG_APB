package TransformadorJSON.Sugerencia;

import Modelos.ActividadModel;
import Modelos.ConsumidorModel;
import Modelos.SugerenciaActividadModel;
import TransformadorJSON.Actividad.TransformadorActividad;
import TransformadorJSON.Consumidor.TransformadorConsumidor;
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

public class TransformadorSugerencia {

    private int id_consumidor;

    private int id_actividad;

    private String esqueletoSugerencia = "{\n" +
            "    \"consumidor\": {\n" +
            "        \"id_consumidor\":" + "\"" + id_consumidor + "\"" + "\n" +
            "    },\n" +
            "    \"actividad\": {\n" +
            "        \"id_actividad\":" + "\"" + id_actividad + "\"" + "\n" +
            "    }\n" +
            "}";

    private final String urlAConectarse = "http://localhost:8080/sugerencia";

    public TransformadorSugerencia(int id_consumidor, int id_actividad) {
        this.id_consumidor = id_consumidor;
        this.id_actividad = id_actividad;
    }

    public TransformadorSugerencia() {
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
                byte[] datosPost = esqueletoSugerencia.getBytes(StandardCharsets.UTF_16);
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

    public ArrayList<SugerenciaActividadModel> recibirInformacionGet() {
        ArrayList<SugerenciaActividadModel> listaSugerencias = new ArrayList<>();

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

            listaSugerencias = sacarInformacionLista(respuesta.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return listaSugerencias;
    }


    /**
     * Método que recibe un id y muestra una sola sugerencia
     *
     * @param id
     * @return
     */
    public SugerenciaActividadModel recibirSugerenciaPorId(int id) {
        SugerenciaActividadModel sugerencia = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(this.urlAConectarse + "/" + id).openConnection();
            connection.setRequestMethod("GET");

            // Leer la respuesta de la API
            StringBuilder respuesta = new StringBuilder();

            try (BufferedReader entrada = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String lineaLeer;
                while ((lineaLeer = entrada.readLine()) != null) {
                    respuesta.append(lineaLeer);
                }

            }

            sugerencia = sacarInformacionIndividual(respuesta.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sugerencia;
    }


    private ArrayList<SugerenciaActividadModel> sacarInformacionLista(String datos) {
        ArrayList<SugerenciaActividadModel> listaSugerencias = new ArrayList<>();

        //Convertimos a un array de JSON
        JSONArray arrayJSON = new JSONArray(datos);

        //Recorremos el array
        for (int i = 0; i < arrayJSON.length(); i++) {
            //Lo convertimos en un objeto individual para capturar sus valores
            JSONObject datosJSON = arrayJSON.getJSONObject(i);

            int id_sugerencia = datosJSON.getInt("id_sugerencia");

            //Sacamos los datos del consumidor
            JSONObject datosConsumidor = datosJSON.getJSONObject("consumidor");
            int id_consumidor = datosConsumidor.getInt("id_consumidor");
            ConsumidorModel consumidor = new TransformadorConsumidor().recibirConsumidorPorId(id_consumidor);


            //Sacamos los datos de la actividad
            JSONObject datosActividad = datosJSON.getJSONObject("actividad");
            int id_actividad = datosActividad.getInt("id_actividad");
            ActividadModel actividad = new TransformadorActividad().recibirActividadPorId(id_actividad);


            SugerenciaActividadModel sugerencia = new SugerenciaActividadModel(id_sugerencia, consumidor, actividad);

            listaSugerencias.add(sugerencia);
        }


        return listaSugerencias;
    }

    public SugerenciaActividadModel sacarInformacionIndividual(String datos) {
        SugerenciaActividadModel sugerencia = null;

        JSONObject datosJSON = new JSONObject(datos);

        int id_sugerencia = datosJSON.getInt("id_sugerencia");

        //Sacamos los datos del consumidor
        JSONObject datosConsumidor = datosJSON.getJSONObject("consumidor");
        int id_consumidor = datosConsumidor.getInt("id_consumidor");
        ConsumidorModel consumidor = new TransformadorConsumidor().recibirConsumidorPorId(id_consumidor);


        //Sacamos los datos de la actividad
        JSONObject datosActividad = datosJSON.getJSONObject("actividad");
        int id_actividad = datosActividad.getInt("id_actividad");
        ActividadModel actividad = new TransformadorActividad().recibirActividadPorId(id_actividad);


        sugerencia = new SugerenciaActividadModel(id_sugerencia, consumidor, actividad);


        return sugerencia;
    }
}