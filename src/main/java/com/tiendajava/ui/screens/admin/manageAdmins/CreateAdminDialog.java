package com.tiendajava.ui.screens.admin.manageAdmins;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tiendajava.model.User;
import com.tiendajava.service.AdminService;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class CreateAdminDialog extends JDialog {

    private final JTextField usernameField = new JTextField(20);
    private final JTextField lastNameField = new JTextField(20);
    private final JTextField passwordField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final AdminService adminService = new AdminService();
    private final Runnable onAdminCreated;

    public CreateAdminDialog(Runnable onAdminCreated) {
        this.onAdminCreated = onAdminCreated;
        setTitle("Create New Admin");
        setModal(true);
        setSize(400, 300); // Ajusté el tamaño para que se vea mejor el formulario
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.getPrimaryColor());

        buildForm();
    }

    private void buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(UIUtils.getDefaultPadding());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Para que el botón ocupe dos columnas

        int row = 0;

        addLabelAndField(panel, gbc, row++, "Username", usernameField);
        addLabelAndField(panel, gbc, row++, "Password", passwordField);
        addLabelAndField(panel, gbc, row++, "Email", emailField);
        addLabelAndField(panel, gbc, row++, "Last Name", lastNameField); // Agregué el campo Last Name al formulario

        JButton createButton = ButtonFactory.createPrimaryButton("Create Admin", null, this::createAdmin);
        gbc.gridy = row++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(createButton, gbc);

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText);
        label.setFont(Fonts.SUBTITLE_FONT);
        label.setForeground(UITheme.getTextColor());
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1; // Volver a una columna para las etiquetas
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(textField, gbc);
    }

    private void createAdmin() {
        String username = usernameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            NotificationHandler.error("All fields are required.");
            return;
        }

        User admin = new User();
        admin.setName(username);
        admin.setLastName(lastName);
        admin.setPassword(password);
        admin.setEmail(email);
        admin.setRole("admin");

        try {
            adminService.registerAdmin(admin);
            JOptionPane.showMessageDialog(this, "Admin created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            onAdminCreated.run();
            dispose();
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Failed to create admin: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}