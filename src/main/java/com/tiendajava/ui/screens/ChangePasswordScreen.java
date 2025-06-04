package com.tiendajava.ui.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.tiendajava.model.Session;
import com.tiendajava.model.User;
import com.tiendajava.service.UserService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.utils.ApiResponse;

public class ChangePasswordScreen extends JPanel {

    private final MainUI parent;
    private final JPasswordField currentPasswordField = new JPasswordField();
    private final JPasswordField newPasswordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();
    private final UserService userService = new UserService();

    public ChangePasswordScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBorder(new EmptyBorder(30, 30, 30, 30));

        initializeUI();
    }

    private void initializeUI() {
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Change Password", AppIcons.KEYS_ICON, SwingConstants.LEFT);
        title.setFont(Fonts.TITLE_FONT.deriveFont(Font.BOLD, 24));
        title.setForeground(UITheme.getTextColor());

        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleContainer.setOpaque(false);
        titleContainer.add(title);

        titlePanel.add(titleContainer, BorderLayout.WEST);
        return titlePanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;

        gbc.gridy = 0;
        formPanel.add(createPasswordField("Current Password", currentPasswordField), gbc);

        gbc.gridy++;
        formPanel.add(createPasswordField("New Password", newPasswordField), gbc);

        gbc.gridy++;
        formPanel.add(createPasswordField("Confirm New Password", confirmPasswordField), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(25, 0, 0, 0);
        JButton saveButton = ButtonFactory.createPrimaryButton("Save Changes",null, this::changePassword);
        saveButton.setPreferredSize(new Dimension(80, 30));
        formPanel.add(saveButton, gbc);

        return formPanel;
    }

    private JPanel createPasswordField(String labelText, JPasswordField passwordField) {
        JPanel fieldPanel = new JPanel(new BorderLayout(5, 5));
        fieldPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(Fonts.NORMAL_FONT);
        label.setForeground(UITheme.getTextColor());

        passwordField.setFont(Fonts.NORMAL_FONT);
        passwordField.setForeground(UITheme.getTextColor());
        passwordField.setBackground(UITheme.getBackgroundContrast());
        passwordField.setBorder(new MatteBorder(0, 0, 1, 0, UITheme.getBorderColor()));
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setCaretColor(UITheme.getTextColor());

        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setBorder(new MatteBorder(0, 0, 1, 0, UITheme.getFocusColor()));
            }

            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setBorder(new MatteBorder(0, 0, 1, 0, UITheme.getBorderColor()));
            }
        });

        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(passwordField, BorderLayout.CENTER);

        return fieldPanel;
    }

    private void changePassword() {
        String currentPassword = new String(currentPasswordField.getPassword()).trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            NotificationHandler.warning("Please fill all fields.");
            return;
        }

        if (newPassword.length() < 8) {
            NotificationHandler.warning("New password must be at least 8 characters long.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            NotificationHandler.error("New passwords do not match.");
            return;
        }

        ApiResponse<User> response = userService.changePassword(
            Session.getInstance().getUser(),
            currentPassword,
            newPassword
        );

        if (response.isSuccess()) {
            NotificationHandler.success("Password changed successfully.");
            parent.showScreen("dashboard");
        } else {
            NotificationHandler.error("Failed to change password: " + response.getMessage());
        }
    }
}
