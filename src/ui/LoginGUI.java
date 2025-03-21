package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONObject;
import service.ApiService;

public class LoginGUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginGUI() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 20, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 20, 150, 25);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 50, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 50, 150, 25);
        add(passwordField);

        JButton loginButton = new JButton("Ingresar");
        loginButton.setBounds(100, 90, 100, 25);
        add(loginButton);

        JButton registerButton = new JButton("Registrarse");
        registerButton.setBounds(100, 120, 100, 25);
        add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                String response = ApiService.login(email, password);

                if (response.contains("token")) {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONObject user = jsonResponse.getJSONObject("user");

                    JOptionPane.showMessageDialog(null, "Bienvenido " + user.getString("nombre") + " " + user.getString("apellido"));
                    dispose();
                    new MainGUI(user.getString("nombre")).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas.");
                }
            }
        });

        registerButton.addActionListener(e -> {
            new RegistroGUI().setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}

