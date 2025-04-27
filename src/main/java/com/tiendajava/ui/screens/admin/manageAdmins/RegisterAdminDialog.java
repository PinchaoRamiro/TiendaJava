package com.tiendajava.ui.screens.admin.manageAdmins;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.User;
import com.tiendajava.service.AdminService;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class RegisterAdminDialog extends JDialog {

    private final JTextField nameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final AdminService adminService = new AdminService();
    private final Runnable onSuccess;

    public RegisterAdminDialog(Runnable onSuccess) {
        this.onSuccess = onSuccess;
        setTitle("Register New Admin");
        setModal(true);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.getPrimaryColor());

        initUI();
    }

    private void initUI() {
        JLabel titleLabel = new JLabel("Create Admin", new ImageIcon(getClass().getResource("/icons/admin-alt.png")), SwingConstants.CENTER);
        titleLabel.setFont(Fonts.TITLE_FONT);
        titleLabel.setForeground(UITheme.getTextColor());
        titleLabel.setBorder(UIUtils.getDefaultPadding());
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        formPanel.setBackground(UITheme.getPrimaryColor());
        formPanel.setBorder(UIUtils.getDefaultPadding());

        formPanel.add(new JLabel("First Name:", SwingConstants.LEFT));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Last Name:", SwingConstants.LEFT));
        formPanel.add(lastNameField);

        formPanel.add(new JLabel("Email:", SwingConstants.LEFT));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:", SwingConstants.LEFT));
        formPanel.add(passwordField);

        UIUtils.styleFormFields(formPanel);

        add(formPanel, BorderLayout.CENTER);

        JButton registerButton = ButtonFactory.createPrimaryButton("Register Admin", null, this::registerAdmin);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UITheme.getPrimaryColor());
        bottomPanel.add(registerButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void registerAdmin() {
        String name = nameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            NotificationHandler.warning(this, "Please complete all fields.");
            return;
        }

        User newAdmin = new User();
        newAdmin.setName(name);
        newAdmin.setLastName(lastName);
        newAdmin.setEmail(email);
        newAdmin.setPassword(password);

        ApiResponse<User> response = adminService.registerAdmin(newAdmin);
        if (response.isSuccess()) {
            NotificationHandler.success(this, "Admin registered successfully!");
            onSuccess.run();
            dispose();
        } else {
            NotificationHandler.error(this, "Failed to register admin: " + response.getMessage());
        }
    }
}
