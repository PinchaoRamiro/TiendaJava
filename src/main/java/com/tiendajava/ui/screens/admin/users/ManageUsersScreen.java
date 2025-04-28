package com.tiendajava.ui.screens.admin.users;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.User;
import com.tiendajava.service.AdminService;
import com.tiendajava.service.UserService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.dialogs.ConfirmationDialog;
import com.tiendajava.ui.components.dialogs.DeleteConfirmDialog;
import com.tiendajava.ui.components.dialogs.ShowInfoDialog;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class ManageUsersScreen extends JPanel {

    private final MainUI parent;
    private final AdminService adminService = new AdminService();
    private final UserService userService = new UserService();
    private final JPanel usersPanel = new JPanel(new GridLayout(0, 2, 15, 15));

    public ManageUsersScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        usersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel title = new JLabel("Manage Users", UIUtils.LoadIcon("/icons/users.png"), SwingConstants.CENTER);
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

        JLabel userInfo = new JLabel("<html><div style='text-align: left; margin-left: 20px'>" +
                "<strong>" + user.getName() + " " + user.getLastName()+ " </strong><br>" + user.getEmail() + "</small><br>" +
                "Role: " + user.getRole() + 
                "</div><br>" +
       " </html>");
        userInfo.setFont(Fonts.NORMAL_FONT);
        userInfo.setForeground(UITheme.getTextColor());
        card.add(userInfo, BorderLayout.CENTER);

        // Botones de acciones
        JPanel actionsPanel = new JPanel();
        actionsPanel.setBackground(UITheme.getSecondaryColor());

        JLabel editRoleBtn = ButtonFactory.createIconButton(UIUtils.LoadIcon("/icons/admin-alt.png"), () -> changeRole(user));
        //separación entre botones
        editRoleBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        ImageIcon deleteIcon = UIUtils.LoadIcon("/icons/trash.png");
        ImageIcon iconDanger = UIUtils.tintImage(deleteIcon, UITheme.getDangerColor());
        JLabel deleteBtn = ButtonFactory.createIconButton(iconDanger, () -> deleteUser(user.getId()));
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JLabel infoBtn = ButtonFactory.createIconButton(UIUtils.LoadIcon("/icons/user.png"), () -> infoComplete(user));
        infoBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JLabel statusBtn = ButtonFactory.createIconButton(UIUtils.LoadIcon("/icons/defuse.png"), () -> changeUserStatus(user));
        statusBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        actionsPanel.add(editRoleBtn, BorderLayout.WEST);
        actionsPanel.add(infoBtn, BorderLayout.WEST);
        actionsPanel.add(statusBtn, BorderLayout.WEST);
        actionsPanel.add(deleteBtn, BorderLayout.WEST);


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

    private void infoComplete(User user) {
        Map<String, String> userInfo = new LinkedHashMap<>();
        userInfo.put("Name", user.getName());
        userInfo.put("Last Name", user.getLastName());
        userInfo.put("Email", user.getEmail());
        userInfo.put("Document", user.getTypeDocument());
        userInfo.put("Number of Document", user.getNumDocument());
        userInfo.put("Phone", user.getPhone());
        userInfo.put("Address", user.getAddress());
        userInfo.put("Status", user.getStatus()? "Active" : "Inactive");
        userInfo.put("Role", user.getRole());

        ShowInfoDialog dialog = new ShowInfoDialog("User Details", userInfo);
        dialog.setVisible(true);
    }

    private void changeUserStatus(User user) {
        new ConfirmationDialog("Change Status", "Are you sure you want to change the status of " + user.getName() + "?", () -> {
            // Logic to change user status
            ApiResponse<Boolean> response = userService.setStatusUser(user, !user.getStatus());
            if (response.isSuccess()) {
                NotificationHandler.success("User status changed successfully for " + user.getName());
                loadUsers();
            } else {
                NotificationHandler.error("Failed to change user status: " + response.getMessage());
            }
        }).setVisible(true);
    }
}
