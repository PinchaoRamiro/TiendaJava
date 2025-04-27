package com.tiendajava.ui.screens.admin.users;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.User;
import com.tiendajava.service.AdminService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.dialogs.ConfirmationDialog;
import com.tiendajava.ui.components.dialogs.DeleteConfirmDialog;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class ManageUsersScreen extends JPanel {

    private final MainUI parent;
    private final AdminService adminService = new AdminService();
    private final JPanel usersPanel = new JPanel(new GridLayout(0, 2, 15, 15));

    public ManageUsersScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        usersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel title = new JLabel("Manage Users", new ImageIcon(getClass().getResource("/icons/users.png")), SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(UIUtils.getDefaultPadding());
        add(title, BorderLayout.NORTH);

        // Scroll para usuarios
        JScrollPane scrollPane = new JScrollPane(usersPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());

        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        loadUsers();
    }

    private void loadUsers() {
        usersPanel.removeAll();
        ApiResponse<List<User>> response = adminService.getAllUsers();
        List<User> users = response.isSuccess() ? response.getData() : null;

        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                usersPanel.add(createUserCard(user));
            }
        } else {
            JLabel noData = new JLabel("No users found", SwingConstants.CENTER);
            noData.setFont(Fonts.NORMAL_FONT);
            noData.setForeground(UITheme.getTextColor());
            usersPanel.add(noData);
        }

        usersPanel.revalidate();
        usersPanel.repaint();
    }

    private JPanel createUserCard(User user) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.getSecondaryColor());
        card.setBorder(UIUtils.getRoundedBorder());
        card.setPreferredSize(new java.awt.Dimension(250, 120));

        JLabel userInfo = new JLabel("<html><strong>" + user.getName() + " " + user.getLastName() + "</strong><br/>" +
                "Email: " + user.getEmail() + "<br/>Role: " + user.getRole() + "</html>");
        userInfo.setFont(Fonts.NORMAL_FONT);
        userInfo.setForeground(UITheme.getTextColor());
        card.add(userInfo, BorderLayout.CENTER);

        // Botones de acciones
        JPanel actionsPanel = new JPanel();
        actionsPanel.setBackground(UITheme.getSecondaryColor());

        JButton editRoleBtn = ButtonFactory.createSecondaryButton("Change Role", null, () -> changeRole(user));
        JButton deleteBtn = ButtonFactory.createDangerButton("Delete", null, () -> deleteUser(user.getId()));

        actionsPanel.add(editRoleBtn);
        actionsPanel.add(deleteBtn);

        card.add(actionsPanel, BorderLayout.SOUTH);

        return card;
    }

    private void changeRole(User user) {
      new ConfirmationDialog("Change Role", "Are you sure you want to change the role of " + user.getName() + "?", () -> {
            // Logic to change user role
            ApiResponse<User> response = adminService.updateUserRole(user.getId(), user.getRole().equals("user") ? "admin" : "user");
            if (response.isSuccess()) {
                NotificationHandler.success("User role changed successfully for " + response.getData().getName());
                loadUsers();
            } else {
                NotificationHandler.error("Failed to change user role: " + response.getMessage());
            }
        }).setVisible(true);
    }

    private void deleteUser(int userId) {
      new DeleteConfirmDialog("User", () -> {
            ApiResponse<String> response = adminService.deleteUser(userId);
            if (response.isSuccess()) {
                NotificationHandler.success("User deleted successfully!");
                loadUsers();
            } else {
                NotificationHandler.error("Failed to delete user: " + response.getMessage());
            }
        }).setVisible(true);
    }
}
