package com.tiendajava.ui.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Session;
import com.tiendajava.model.User;
import com.tiendajava.service.UserService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class ChangePasswordScreen extends JPanel {

    private final MainUI parent;
    private final JPasswordField currentPasswordField = new JPasswordField();
    private final JPasswordField newPasswordField = new JPasswordField();
    private final JPasswordField confirmPasswordField = new JPasswordField();

    public ChangePasswordScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());

        JLabel title = new JLabel("Change Password", SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(UIUtils.getDefaultPadding());
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(UITheme.getPrimaryColor());
        formPanel.setFont(Fonts.NORMAL_FONT);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(UIUtils.getDefaultPadding());

        // gap between fields
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        formPanel.add(UIUtils.createFormField("Current Password:", currentPasswordField));

        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(UIUtils.createFormField("New Password:", newPasswordField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(UIUtils.createFormField("Confirm New Password:", confirmPasswordField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> changePassword());
        saveButton.setBackground(UITheme.getButtonColor());
        saveButton.setForeground(UITheme.getTextColor());
        saveButton.setFont(Fonts.BUTTON_FONT);

        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(saveButton);

        add(formPanel, BorderLayout.CENTER);
    }

    private void changePassword() {
        String currentPassword = new String(currentPasswordField.getPassword()).trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            NotificationHandler.warning("Please fill all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            NotificationHandler.error("New passwords do not match.");
            return;
        }

        UserService userService = new UserService();
        ApiResponse<User> response = userService.changePassword( Session.getInstance().getUser(), currentPassword, newPassword);

        if (response.isSuccess()) {
            NotificationHandler.success("Password changed successfully.");
            parent.showScreen("dashboard");
        } else {
            NotificationHandler.error("Failed to change password: " + response.getMessage());
        }
    }
}
