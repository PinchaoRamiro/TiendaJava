package probeDataBase;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import org.json.JSONObject;

public class ApiService {
    public static String registerUser(
            String nombre, String apellido, String email, String password,
            String tipoDocumento, String numeroDocumento, String direccion, String telefono) {

        try {
            // Definir la URL del backend
            URL url = new URI("http://localhost:5000/api/auth/register").toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Configurar la petición HTTP
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Crear JSON con los datos del usuario
            JSONObject json = new JSONObject();
            json.put("nombre", nombre);
            json.put("apellido", apellido);
            json.put("email", email);
            json.put("password", password);
            json.put("tipoDocumento", tipoDocumento);
            json.put("numeroDocumento", numeroDocumento);
            json.put("direccion", direccion);
            json.put("telefono", telefono);

            // Enviar JSON en la solicitud
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leer la respuesta del backend
            int responseCode = conn.getResponseCode();
            InputStream responseStream = (responseCode == HttpURLConnection.HTTP_CREATED) 
                ? conn.getInputStream() 
                : conn.getErrorStream(); // Capturar errores

            // Procesar respuesta
            try (BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // Si la respuesta es 201 (CREATED), devolver JSON de éxito
                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    return "✅ Usuario registrado: " + response.toString();
                } else {
                    return "⚠️ Error " + responseCode + ": " + response;
                }
            }
        } catch (IOException | URISyntaxException e) {
            return "❌ Error de conexión: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        // Probar el registro de usuario con datos ficticios
        String response = registerUser(
            "Ramiro", "Pinchao", "ramiro@pinchao.com", "12345678",
            "CC", "123456789", "Calle Falsa 123", "3201234567"
        );

        System.out.println(response);
    }
}
