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
;


public class RegistroGUI extends JFrame {
    private final JTextField nameField, lastnameField, emailField, TypeDocumentField, numDocumentField, adressField, phoneField;
    private final JPasswordField passwordField, passwordFieldCon;
    private UserService userService = new UserService();

    public RegistroGUI() {
        setTitle("User Registration");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        UIUtils.applyDarkTheme();

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(45, 45, 45));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = createInputField(panel, gbc, "Name:", 0);
        lastnameField = createInputField(panel, gbc, "Lastname:", 1);
        emailField = createInputField(panel, gbc, "Email:", 2);
        passwordField = createPasswordField(panel, gbc, "Password:", 3);
        passwordFieldCon = createPasswordField(panel, gbc, "Confirm Password:", 4);
        TypeDocumentField = createInputField(panel, gbc, "Document Type:", 5);
        numDocumentField = createInputField(panel, gbc, "N° Document:", 6);
        adressField = createInputField(panel, gbc, "Address:", 7);
        phoneField = createInputField(panel, gbc, "Phone:", 8);

        JPanel buttonPanel = UIUtils.createBasePanel(new FlowLayout());

        buttonPanel.add(UIUtils.createButton("Register", this::registrarUsuario));
        buttonPanel.add(UIUtils.createButton("Back", () -> {
            new LoginGUI().setVisible(true);
            dispose();
        }));

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JTextField createInputField(JPanel panel, GridBagConstraints gbc, String labelText, int yPos) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        JTextField field = UIUtils.createTextField();
        panel.add(field, gbc);

        return field;
    }

    private JPasswordField createPasswordField(JPanel panel, GridBagConstraints gbc, String labelText, int yPos) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        JPasswordField field = UIUtils.createPasswordField();
        panel.add(field, gbc);

        return field;
    }

    private void registrarUsuario() {
        String name = nameField.getText().trim();
        String lastname = lastnameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String passwordCon = new String(passwordFieldCon.getPassword()).trim();
        String typeDocument = TypeDocumentField.getText().trim();
        String numDocument = numDocumentField.getText().trim();
        String address = adressField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordCon.isEmpty()) {
            UIUtils.showError(this, "Complete all fields");
            return;
        }

        if (!password.equals(passwordCon)) {
            UIUtils.showError(this, "Passwords do not match");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setPassword(password);
        user.setTypeDocument(typeDocument);
        user.setNumDocument(numDocument);
        user.setAddress(address);
        user.setPhone(phone);

        try {
            User userReg = userService.Register(user);
            if(userReg == null){
                UIUtils.showError(this, "Error to register user" );
                return;
            }
            
            dispose();
            new MainWindow(user.getName(), lastname).setVisible(true);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            UIUtils.showError(this, "Error: " + ex.getMessage());
        }
    }
}
