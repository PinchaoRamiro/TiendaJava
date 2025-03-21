package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONObject;
import service.ApiService;

public class RegistroGUI extends JFrame {
    private final JTextField nombreField;
    private JTextField apellidoField, emailField, tipoDocumentoField, numeroDocumentoField, direccionField, telefonoField;
    private JPasswordField passwordField;

    public RegistroGUI() {
        setTitle("Registro de Usuario");
        setSize(350, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(20, 20, 100, 25);
        add(nombreLabel);
        nombreField = new JTextField();
        nombreField.setBounds(150, 20, 150, 25);
        add(nombreField);

        JLabel apellidoLabel = new JLabel("Apellido:");
        apellidoLabel.setBounds(20, 50, 100, 25);
        add(apellidoLabel);
        apellidoField = new JTextField();
        apellidoField.setBounds(150, 50, 150, 25);
        add(apellidoField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 80, 100, 25);
        add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(150, 80, 150, 25);
        add(emailField);

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setBounds(20, 110, 100, 25);
        add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 110, 150, 25);
        add(passwordField);

        JLabel tipoDocumentoLabel = new JLabel("Tipo Documento:");
        tipoDocumentoLabel.setBounds(20, 140, 120, 25);
        add(tipoDocumentoLabel);
        tipoDocumentoField = new JTextField();
        tipoDocumentoField.setBounds(150, 140, 150, 25);
        add(tipoDocumentoField);

        JLabel numeroDocumentoLabel = new JLabel("N° Documento:");
        numeroDocumentoLabel.setBounds(20, 170, 120, 25);
        add(numeroDocumentoLabel);
        numeroDocumentoField = new JTextField();
        numeroDocumentoField.setBounds(150, 170, 150, 25);
        add(numeroDocumentoField);

        JLabel direccionLabel = new JLabel("Dirección:");
        direccionLabel.setBounds(20, 200, 100, 25);
        add(direccionLabel);
        direccionField = new JTextField();
        direccionField.setBounds(150, 200, 150, 25);
        add(direccionField);

        JLabel telefonoLabel = new JLabel("Teléfono:");
        telefonoLabel.setBounds(20, 230, 100, 25);
        add(telefonoLabel);
        telefonoField = new JTextField();
        telefonoField.setBounds(150, 230, 150, 25);
        add(telefonoField);

        JButton registerButton = new JButton("Registrar");
        registerButton.setBounds(100, 270, 120, 30);
        add(registerButton);

        JButton backButton = new JButton("Volver");
        backButton.setBounds(100, 310, 120, 30);
        add(backButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });

        backButton.addActionListener(e -> {
            new LoginGUI().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    private void registrarUsuario() {
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String tipoDocumento = tipoDocumentoField.getText();
        String numeroDocumento = numeroDocumentoField.getText();
        String direccion = direccionField.getText();
        String telefono = telefonoField.getText();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty() ||
            tipoDocumento.isEmpty() || numeroDocumento.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String response = ApiService.register(nombre, apellido, email, password, tipoDocumento, numeroDocumento, direccion, telefono);

        if (response.contains("Usuario registrado correctamente")) {
            JOptionPane.showMessageDialog(this, "Usuario registrado con éxito", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            new LoginGUI().setVisible(true);
            dispose();
        } else {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                String errorMsg = jsonResponse.optString("msg", "Error en el registro");
                JOptionPane.showMessageDialog(this, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
