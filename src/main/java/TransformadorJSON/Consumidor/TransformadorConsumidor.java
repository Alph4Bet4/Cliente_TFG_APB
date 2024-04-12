package TransformadorJSON.Consumidor;

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

    private final String esqueletoConsumidor = "{\n" +
            "    \"nombreConsumidor\": " + "\"" + nombreConsumidor + "\"" + ",\n" +
            "    \"primerApellidoConsumidor\": " + "\"" + primerApellidoConsumidor + "\"" + ",\n" +
            "    \"segundoApellidoConsumidor\": " + "\"" + segundoApellidoConsumidor + "\"" + ",\n" +
            "    \"contrasenia\": " + "\"" + contrasenia + "\"" + ",\n" +
            "    \"email_consumidor\": " + "\"" + email_consumidor + "\"" + ",\n" +
            "}";

    private final String urlAConectarse = "http://localhost:8080/consumidor";

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
    }

    /**
     * Constructor sin parametros de consumidor
     */
    public TransformadorConsumidor() {

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
                byte[] datosPost = esqueletoConsumidor.getBytes(StandardCharsets.UTF_16);
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
     * Metodo que busca solo un usuario y lo recibe
     */
    public void recibirUsuario() {

    }

    public ArrayList<ConsumidorModel> recibirInformacionGet() {
        ArrayList<ConsumidorModel> listaConsumidor = new ArrayList<>();

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

            listaConsumidor = sacarInformacionLista(respuesta.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return listaConsumidor;
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


}
