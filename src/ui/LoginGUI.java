package ui;

import javax.swing.*;
import java.awt.*;
import org.json.JSONObject;
import service.ApiService;

public class LoginGUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginGUI() {
        setTitle("Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta y campo de Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(15);
        panel.add(emailField, gbc);

        // Etiqueta y campo de Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        // Panel de botones con FlowLayout para mejor diseño
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton loginButton = new JButton("Ingresar");
        loginButton.addActionListener(e -> login());
        buttonPanel.add(loginButton);

        JButton registerButton = new JButton("Registrarse");
        registerButton.addActionListener(e -> {
            new RegistroGUI().setVisible(true);
            dispose();
        });
        buttonPanel.add(registerButton);

        // Agregar los paneles a la ventana
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Validación de campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String response = ApiService.login(email, password);

            if (response.contains("token")) {
                JSONObject jsonResponse = new JSONObject(response);
                JSONObject user = jsonResponse.getJSONObject("user");

                dispose(); // Cerrar la ventana actual
                new MainGUI(user.getString("nombre")).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión con el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginGUI::new);
    }
}
