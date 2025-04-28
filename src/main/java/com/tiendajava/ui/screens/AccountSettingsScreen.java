package com.tiendajava.ui.screens;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Session;
import com.tiendajava.model.User;
import com.tiendajava.service.UserService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class AccountSettingsScreen extends JPanel {

    private final JTextField nameField = new JTextField(20);
    private final JTextField lastNameField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JTextField typeDocument = new JTextField(20);
    private final JTextField document = new JTextField(20);
    private final JTextField phone = new JTextField(20);
    private final JTextField address = new JTextField(20);
    private final JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Active", "Inactive"});

    private final UserService userService = new UserService();

    private static MainUI parent; // Static reference to the parent MainUI

    public AccountSettingsScreen( MainUI parent) {
        AccountSettingsScreen.parent = parent; // Assign the parent MainUI to the static field
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBorder(UIUtils.getDefaultPadding());

        JLabel title = new JLabel("Account Settings");
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());

        add(title, BorderLayout.NORTH);

        buildForm();
    }

    private void buildForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.getPrimaryColor());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        addLabelAndField(formPanel, gbc, row++, "Name", nameField);
        addLabelAndField(formPanel, gbc, row++, "Last Name", lastNameField);
        addLabelAndField(formPanel, gbc, row++, "Email", emailField);
        addLabelAndField(formPanel, gbc, row++, "Document Type", typeDocument);
        addLabelAndField(formPanel, gbc, row++, "Document Number", document);
        addLabelAndField(formPanel, gbc, row++, "Phone", phone);
        addLabelAndField(formPanel, gbc, row++, "Address", address);

        // addLabelAndField(formPanel, gbc, row++, "Status", statusComboBox);

        JLabel statusLabel = new JLabel("Status");
        gbc.gridx = 0;
        gbc.gridy++;
        
        formPanel.add(statusLabel, gbc);
        UIUtils.styleComboBox(statusComboBox);
        statusLabel.setForeground(UITheme.getTextColor());
        statusLabel.setFont(Fonts.NORMAL_FONT);
        gbc.gridx = 1;
        formPanel.add(statusComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy++;
        


        // Botón de guardar
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton saveBtn = ButtonFactory.createPrimaryButton("Save Changes", null, this::saveChanges);
        saveBtn.setPreferredSize(new java.awt.Dimension(100, 40));
        formPanel.add(saveBtn, gbc);

        add(formPanel, BorderLayout.CENTER);

        loadCurrentUser();
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel label = new JLabel(labelText);
        label.setForeground(UITheme.getTextColor());
        label.setFont(Fonts.NORMAL_FONT);
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void loadCurrentUser() {
        User user = Session.getInstance().getUser();
        if (user != null) {
            nameField.setText(user.getName());
            lastNameField.setText(user.getLastName());
            emailField.setText(user.getEmail());
            typeDocument.setText(user.getTypeDocument());
            document.setText(user.getNumDocument());
            phone.setText(user.getPhone());
            address.setText(user.getAddress());
            statusComboBox.setSelectedItem(user.getStatus() ? "Active" : "Inactive");
        } else {
            NotificationHandler.error("User not found in session.");
        }
    }

    private void saveChanges() {
        String name = nameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String typeDoc = typeDocument.getText().trim();
        String doc = document.getText().trim();
        String phoneNum = phone.getText().trim();
        String addressText = address.getText().trim();
        String status = (String) statusComboBox.getSelectedItem();

        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() ) {
            NotificationHandler.warning("All fields must be filled.");
            return;
        }

        User updatedUser = new User();
        updatedUser.setId(Session.getInstance().getUser().getId());
        updatedUser.setName(name);
        updatedUser.setLastName(lastName);
        updatedUser.setEmail(email);
        updatedUser.setTypeDocument(typeDoc);
        updatedUser.setNumDocument(doc);
        updatedUser.setPhone(phoneNum);
        updatedUser.setAddress(addressText);
        updatedUser.setStatus(status.equals("Active"));

        ApiResponse<User> response = userService.UpdateUser(updatedUser);

        if (response.isSuccess()) {
            NotificationHandler.success("Profile updated successfully!");
            Session.getInstance().setUser(updatedUser); 
        } else {
            System.out.println("Error: " + response.toString());
            NotificationHandler.error("Failed to update profile: " + response.getMessage());
        }
    }
}
