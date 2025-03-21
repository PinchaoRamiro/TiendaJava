package service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.json.JSONObject;

public class ApiService {
    private static final String BASE_URL = "http://localhost:5000/api/auth/";

    // Método para iniciar sesión
    public static String login(String email, String password) {
        return sendRequest("login", email, password);
    }

    // Método para registrar usuario
    public static String register(String nombre, String apellido, String email, String password, String tipoDocumento, String numeroDocumento, String direccion, String telefono) {
        try {
            JSONObject json = new JSONObject();
            json.put("nombre", nombre);
            json.put("apellido", apellido);
            json.put("email", email);
            json.put("password", password);
            json.put("tipoDocumento", tipoDocumento);
            json.put("numeroDocumento", numeroDocumento);
            json.put("direccion", direccion);
            json.put("telefono", telefono);

            return sendPostRequest("register", json);
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    // Método genérico para enviar peticiones POST
    private static String sendRequest(String endpoint, String email, String password) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);

            return sendPostRequest(endpoint, json);
        } catch (Exception e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    private static String sendPostRequest(String endpoint, JSONObject json) throws IOException {
        URL url;
        try {
            url = new URI(BASE_URL + endpoint).toURL();

        } catch (URISyntaxException e) {
            throw new IOException("Invalid URL", e);
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        InputStream responseStream = (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED)
            ? conn.getInputStream() : conn.getErrorStream();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            return response.toString();
        }
    }
}
