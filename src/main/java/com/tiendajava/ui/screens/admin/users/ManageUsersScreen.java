package com.tiendajava.ui.screens.admin.users;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.components.dialogs.DeleteConfirmDialog;
import com.tiendajava.ui.components.dialogs.ShowInfoDialog;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;
import static com.tiendajava.ui.utils.UIUtils.getRoundedBorder;

public class ManageUsersScreen extends JPanel {

    private final MainUI parent;
    private final AdminService adminService = new AdminService();
    // private final UserService userService = new UserService();
    private final JPanel usersPanel = new JPanel(new GridLayout(0, 2, 15, 15));

    public ManageUsersScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.getPrimaryColor());

        // === Título ===
        JLabel title = new JLabel("Manage Users", AppIcons.USERS_ICON, SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(UIUtils.getDefaultPadding());
        add(title, BorderLayout.NORTH);

        // === Panel de usuarios con scroll ===
        usersPanel.setBackground(UITheme.getPrimaryColor());
        usersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(usersPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        // === Cargar usuarios ===
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
        card.setBorder(getRoundedBorder());
        card.setPreferredSize(new Dimension(300, 240));

        String userInfoHtml = String.format(
            "<html>" +
                "<div style='padding: 10px; font-family: sans-serif;'>" +
                    "<div style='font-size: 14px; color: #999;'>Full Name: <div style='color:white; font-size: 16px; font-weight: bold;'>%s %s</div>  </div>" +
                    "<div style='margin-top: 8px; font-size: 14px; color: #999;'>Email:  <div style='color:white;  font-size: 14px;'>%s</div> </div>"+
                "</div>" +
            "</html>",
            user.getName(), user.getLastName(),
            user.getEmail()
        );

        JLabel userInfo = new JLabel(userInfoHtml);
        userInfo.setFont(Fonts.NORMAL_FONT);
        userInfo.setForeground(UITheme.getTextColor());
        card.add(userInfo, BorderLayout.CENTER);


        // Botones de acciones
        JPanel actionsPanel = new JPanel();
        actionsPanel.setBackground(UITheme.getSecondaryColor());
        ImageIcon deleteIcon = AppIcons.DELETE_ICON;
        ImageIcon iconDanger = UIUtils.tintImage(deleteIcon, UITheme.getDangerColor());
        JLabel deleteBtn = ButtonFactory.createIconButton(iconDanger, "Delete", () -> deleteUser(user.getId()));
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JLabel infoBtn = ButtonFactory.createIconButton(AppIcons.USER_ICON, "Information",  () -> infoComplete(user));
        infoBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        actionsPanel.add(infoBtn, BorderLayout.WEST);
        actionsPanel.add(deleteBtn, BorderLayout.WEST);


        card.add(actionsPanel, BorderLayout.SOUTH);

        return card;
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

    public MainUI getParentMU() {
        return parent;
    }
}
