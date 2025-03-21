package ui;

import javax.swing.*;
import java.awt.*;
import org.json.JSONObject;
import service.ApiService;

public class RegistroGUI extends JFrame {
    private final JTextField nombreField, apellidoField, emailField, tipoDocumentoField, numeroDocumentoField, direccionField, telefonoField;
    private final JPasswordField passwordField;

    public RegistroGUI() {
        // Configuración de la ventana
        setTitle("Registro de Usuario");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana
        setLayout(new BorderLayout());

        // Panel principal con GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Márgenes

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Función para agregar etiquetas y campos de texto al panel
        nombreField = createInputField(panel, gbc, "Nombre:", 0);
        apellidoField = createInputField(panel, gbc, "Apellido:", 1);
        emailField = createInputField(panel, gbc, "Email:", 2);
        passwordField = createPasswordField(panel, gbc, "Contraseña:", 3);
        tipoDocumentoField = createInputField(panel, gbc, "Tipo Documento:", 4);
        numeroDocumentoField = createInputField(panel, gbc, "N° Documento:", 5);
        direccionField = createInputField(panel, gbc, "Dirección:", 6);
        telefonoField = createInputField(panel, gbc, "Teléfono:", 7);

        // Panel para los botones
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton registerButton = new JButton("Registrar");
        registerButton.addActionListener(e -> registrarUsuario());
        buttonPanel.add(registerButton);

        JButton backButton = new JButton("Volver");
        backButton.addActionListener(e -> {
            new LoginGUI().setVisible(true);
            dispose();
        });
        buttonPanel.add(backButton);

        // Agregar componentes a la ventana
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    private JTextField createInputField(JPanel panel, GridBagConstraints gbc, String labelText, int yPos) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        JTextField textField = new JTextField(15);
        panel.add(textField, gbc);

        return textField;
    }

    private JPasswordField createPasswordField(JPanel panel, GridBagConstraints gbc, String labelText, int yPos) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        return passwordField;
    }

    private void registrarUsuario() {
        String nombre = nombreField.getText().trim();
        String apellido = apellidoField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String tipoDocumento = tipoDocumentoField.getText().trim();
        String numeroDocumento = numeroDocumentoField.getText().trim();
        String direccion = direccionField.getText().trim();
        String telefono = telefonoField.getText().trim();

        // Validación de campos vacíos
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() ||
            tipoDocumento.isEmpty() || numeroDocumento.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String response = ApiService.register(nombre, apellido, email, password, tipoDocumento, numeroDocumento, direccion, telefono);

            if (response.contains("Usuario registrado correctamente")) {
                JOptionPane.showMessageDialog(this, "Usuario registrado con éxito", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                new LoginGUI().setVisible(true);
                dispose();
            } else {
                JSONObject jsonResponse = new JSONObject(response);
                String errorMsg = jsonResponse.optString("msg", "Error en el registro");
                JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
