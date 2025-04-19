package com.tiendajava.ui.screens;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
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

public class RegisterScreen extends JFrame {

    private final JTextField nameField = UIUtils.createTextField();
    private final JTextField lastNameField = UIUtils.createTextField();
    private final JTextField emailField = UIUtils.createTextField();
    private final JPasswordField passwordField = UIUtils.createPasswordField();
    private final JPasswordField passwordConfirmField = UIUtils.createPasswordField();
    private final JTextField typeDocumentField = UIUtils.createTextField();
    private final JTextField numDocumentField = UIUtils.createTextField();
    private final JTextField addressField = UIUtils.createTextField();
    private final JTextField phoneField = UIUtils.createTextField();
    private final UserService userService = new UserService();

    public RegisterScreen() {
        setTitle("Register - TiendaJava");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    
        UIUtils.applyDarkTheme();
    
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        int y = 0;
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 4;
        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        panel.add(title, gbc);

        y = 1;
    
        gbc.gridwidth = 1;
        addField(panel, gbc, ++y, 0, "Name:", nameField);
        addField(panel, gbc, y, 2, "Lastname:", lastNameField);
    
        addField(panel, gbc, ++y, 0, "Email:", emailField);
        addField(panel, gbc, y, 2, "Cel:", phoneField);
    
        addField(panel, gbc, ++y, 0, "Password:", passwordField);
        addField(panel, gbc, y, 2, "Confirm Password:", passwordConfirmField);
    
        addField(panel, gbc, ++y, 0, "Type of Document:", typeDocumentField);
        addField(panel, gbc, y, 2, "N° Document:", numDocumentField);
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        addField(panel, gbc, ++y, 0, "Address:", addressField);
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        panel.add(addressField, gbc);

        gbc.gridwidth = 1;


        gbc.gridx = 1;
        gbc.gridy = y + 2;
        JButton registerBtn = ButtonFactory.createPrimaryButton("Register", this::register);
        panel.add(registerBtn, gbc);
    
        gbc.gridx = 2;
        JButton backBtn = ButtonFactory.createSecondaryButton("Back", () -> {
            SwingUtilities.invokeLater(() -> {
                LoginScreen.start();
                dispose();
            });
        });
        panel.add(backBtn, gbc);
    
        add(panel, BorderLayout.CENTER);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int y, int x, String label, JComponent field) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = x + 1;
        panel.add(field, gbc);
    }
    
    private void register() {
        String name = nameField.getText().trim();
        String lastname = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(passwordConfirmField.getPassword()).trim();
        String typeDocument = typeDocumentField.getText().trim();
        String numDocument = numDocumentField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            ErrorHandler.showWarningMessage(this, "Warning", "Please complete the required fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            ErrorHandler.showWarningMessage(this, "Warning", "Passwords do not match");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setPassword(password);
        user.setTypeDocument(typeDocument.isEmpty() ? "" : typeDocument);
        user.setNumDocument(numDocument.isEmpty() ? "" : numDocument);
        user.setAddress(address.isEmpty() ? "" : address);
        user.setPhone(phone.isEmpty() ? "" : phone);

        User userR = userService.Register(user);

        boolean success = userR != null;
        if (success) {
            ErrorHandler.showSuccessMessage(this, "User created", "Successfully registered user");
            SwingUtilities.invokeLater(() -> {
                new MainFrame(user.getName(), user.getLastName()).setVisible(true);
                dispose();
            });
        } else {
            ErrorHandler.showErrorMessage(this, "Error", "Error registering user");
        }
    }

    public static void start() {
        SwingUtilities.invokeLater(() -> new RegisterScreen().setVisible(true));
    }
}
