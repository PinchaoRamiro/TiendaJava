import org.json.JSONObject from lib/org.json.jar;

public class AuthService {
    public static void main(String[] args) {
        String response = ApiService.login("juan@mail.com", "123456");

        if (response.startsWith("Error")) {
            System.out.println(response);
        } else {
            // Convertir respuesta a JSON
            JSONObject jsonResponse = new JSONObject(response);

            // Extraer el token y datos del usuario
            String token = jsonResponse.getString("token");
            JSONObject user = jsonResponse.getJSONObject("user");

            String nombre = user.getString("nombre");
            String apellido = user.getString("apellido");
            String email = user.getString("email");

            System.out.println("Token: " + token);
            System.out.println("Usuario: " + nombre + " " + apellido + " (" + email + ")");
        }
    }
}
