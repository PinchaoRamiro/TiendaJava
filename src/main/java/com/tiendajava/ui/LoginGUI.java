package com.tiendajava.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.tiendajava.model.User;
import com.tiendajava.service.UserService;
import com.tiendajava.ui.utils.UIUtils;

public class LoginGUI extends JFrame {
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final UserService userService = new UserService();

    public LoginGUI() {
        setTitle("Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        UIUtils.applyDarkTheme();

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
        emailField = UIUtils.createTextField();
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = UIUtils.createPasswordField();
        panel.add(passwordField, gbc);

        JPanel buttonPanel = UIUtils.createBasePanel(new FlowLayout());

        buttonPanel.add(UIUtils.createButton("Login", this::login));
        buttonPanel.add(UIUtils.createButton("Register", () -> {
            new RegistroGUI().setVisible(true);
            dispose();
        }));

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            UIUtils.showError(this, "Complete all fields");
            return;
        }

        boolean response = userService.login(email, password);

        if (!response) {
            UIUtils.showError(this, "Incorrect email or password");
        } else {
            User user = userService.findUserByEmail(email);
            if (user != null) {
                new MainWindow(user.getName(), user.getLastName()).setVisible(true);
                dispose();
            } else {
                UIUtils.showError(this, "User not found");
            }
        }
    }

    public static void start() {
        LoginGUI login = new LoginGUI();
        login.setVisible(true);
    }
}
