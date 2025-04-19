package com.tiendajava.ui.screens;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.tiendajava.model.User;
import com.tiendajava.service.UserService;
import com.tiendajava.ui.MainFrame;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.utils.ErrorHandler;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UIUtils;

public class LoginScreen extends JFrame {
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final UserService userService = new UserService();

    public LoginScreen() {
        setTitle("Login - TiendaJava");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        UIUtils.applyDarkTheme();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = UIUtils.createTextField();
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = UIUtils.createPasswordField();
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        JButton loginBtn = ButtonFactory.createPrimaryButton("Login", this::login);
        panel.add(loginBtn, gbc);

        gbc.gridy++;
        JButton regisBtn = ButtonFactory.createSecondaryButton("Register", () -> {
            SwingUtilities.invokeLater(() -> {
                RegisterScreen.start();
                dispose();
            });
        });
        panel.add(regisBtn, gbc);
        add(panel, BorderLayout.CENTER);
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            ErrorHandler.showWarningMessage(this, "Warning", "Please complete the required fields");
            return;
        }

        boolean success = userService.login(email, password);
        if (success) {
            User user = userService.findUserByEmail(email);
            SwingUtilities.invokeLater(() -> {
                new MainFrame(user.getName(), user.getLastName()).setVisible(true);
                dispose();
            });
        } else {
            ErrorHandler.showErrorMessage(this, "Error", "Incorrect credentials");
        }
    }

    public static void start() {
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}