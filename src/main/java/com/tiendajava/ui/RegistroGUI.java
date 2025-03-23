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
import com.tiendajava.repository.UserRepository;

public class RegistroGUI extends JFrame {
    private final JTextField nameField, lastnameField, emailField, TypeDocumentField, numDocumentField, adressField, phoneField;
    private final JPasswordField passwordField;
    private final UserRepository userRepository = new UserRepository();

    public RegistroGUI() {
        setTitle("User Registration");
        setSize(400, 500); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Aplicar tema oscuro
        UIManager.put("Panel.background", new Color(45, 45, 45));
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", new Color(60, 60, 60));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("PasswordField.background", new Color(60, 60, 60));
        UIManager.put("PasswordField.foreground", Color.WHITE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameField = createInputField(panel, gbc, "Name:", 0);
        lastnameField = createInputField(panel, gbc, "Lastname:", 1);
        emailField = createInputField(panel, gbc, "Email:", 2);
        passwordField = createPasswordField(panel, gbc, "Password:", 3);
        TypeDocumentField = createInputField(panel, gbc, "Document Type:", 4);
        numDocumentField = createInputField(panel, gbc, "N° Document:", 5);
        adressField = createInputField(panel, gbc, "Address:", 6);
        phoneField = createInputField(panel, gbc, "Phone:", 7);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(45, 45, 45)); 

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registrarUsuario());
        buttonPanel.add(registerButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            new LoginGUI().setVisible(true);
            dispose();
        });
        buttonPanel.add(backButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JTextField createInputField(JPanel panel, GridBagConstraints gbc, String labelText, int yPos) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        JLabel label = new JLabel(labelText);
        panel.add(label, gbc);

        gbc.gridx = 1;
        JTextField textField = new JTextField(15);
        panel.add(textField, gbc);

        return textField;
    }

    private JPasswordField createPasswordField(JPanel panel, GridBagConstraints gbc, String labelText, int yPos) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        JLabel label = new JLabel(labelText);
        panel.add(label, gbc);

        gbc.gridx = 1;
        JPasswordField passwordsField = new JPasswordField(15);
        panel.add(passwordsField, gbc);

        return passwordsField;
    }
    private void registrarUsuario() {
        String name = nameField.getText().trim();
        String lastname = lastnameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String TypeDocument = TypeDocumentField.getText().trim();
        String numDocument = numDocumentField.getText().trim();
        String adress = adressField.getText().trim();
        String phone = phoneField.getText().trim();

        if(name.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || TypeDocument.isEmpty() || numDocument.isEmpty() || adress.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User();
        user.setName(name);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setPassword(password);
        user.setTypeDocument(TypeDocument);
        user.setNumDocument(numDocument);
        user.setAddress(adress);
        user.setPhone(phone);

        try {    
            userRepository.createUser(user);
            dispose();
            new MainGUI(user.getName(), user.getLastName()).setVisible(true);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
