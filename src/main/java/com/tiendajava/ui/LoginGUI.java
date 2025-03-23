package com.tiendajava.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.tiendajava.model.User;
import com.tiendajava.service.UserService;

public class LoginGUI extends JFrame {
    private final JTextField emailField;
    private  final JPasswordField passwordField;
    private final UserService userService = new UserService();

    public LoginGUI() {
        setTitle("Login");
        setSize(400, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        UIManager.put("Panel.background", new Color(45, 45, 45));
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", new Color(60, 63, 65));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("PasswordField.background", new Color(60, 63, 65));
        UIManager.put("PasswordField.foreground", Color.WHITE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(45, 45, 45)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(15);
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(45, 45, 45));

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> login());
        buttonPanel.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            new RegistroGUI().setVisible(true);
            dispose();
        });
        buttonPanel.add(registerButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete all fields", "Error", JOptionPane.ERROR_MESSAGE);
        }
            boolean response = userService.login(email, password);

            if(!response) {
                JOptionPane.showMessageDialog(this, "Incorrect email or password", "Error", JOptionPane.ERROR_MESSAGE);
            }else{

                try{
                    User user = userService.findUserByEmail(email);

                    if(user != null){
                        new MainGUI(user.getName(), user.getLastName()).setVisible(true);
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }catch(Exception e){
                    System.err.println("Error: " + e.getMessage());
                }
            }
    }

    public static void Start() {
        new LoginGUI().setVisible(true);
    }
}
