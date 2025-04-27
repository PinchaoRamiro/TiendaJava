package com.tiendajava.ui.screens.admin.manageAdmins;

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
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class ManageAdminsScreen extends JPanel {

    private final MainUI parent;
    private final AdminService adminService = new AdminService();
    private final JPanel adminsPanel = new JPanel(new GridLayout(0, 2, 15, 15));

    public ManageAdminsScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBackground(UITheme.getPrimaryColor());
        adminsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel title = new JLabel("Manage Admins", new ImageIcon(getClass().getResource("/icons/admin-alt.png")), SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(UIUtils.getDefaultPadding());
        add(title, BorderLayout.NORTH);

        // Botón para agregar admin
        JButton createAdminBtn = ButtonFactory.createPrimaryButton("Add Admin", new ImageIcon(getClass().getResource("/icons/user-add.png")), this::registerAdmin);
        createAdminBtn.setPreferredSize(new java.awt.Dimension(150, 40));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(UITheme.getPrimaryColor());
        topPanel.add(createAdminBtn);
        add(topPanel, BorderLayout.PAGE_START);


        // Scroll para admins
        JScrollPane scrollPane = new JScrollPane(adminsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        loadAdmins();
    }

    private void loadAdmins() {
        adminsPanel.removeAll();
        ApiResponse<List<User>> response = adminService.getAllAdmins();
        List<User> admins = response.isSuccess() ? response.getData() : null;

        if (admins != null && !admins.isEmpty()) {
            for (User admin : admins) {
                adminsPanel.add(createAdminCard(admin));
            }
        } else {
            JLabel noData = new JLabel("No admins found", SwingConstants.CENTER);
            noData.setFont(Fonts.NORMAL_FONT);
            noData.setForeground(UITheme.getTextColor());
            adminsPanel.add(noData);
        }

        adminsPanel.revalidate();
        adminsPanel.repaint();
    }

    
    private void registerAdmin() {
        RegisterAdminDialog dialog = new RegisterAdminDialog(this::loadAdmins);
        dialog.setVisible(true);
    }

    private JPanel createAdminCard(User admin) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.getSecondaryColor());
        card.setBorder(UIUtils.getRoundedBorder());
        card.setPreferredSize(new java.awt.Dimension(250, 120));

        JLabel adminInfo = new JLabel("<html><strong>" + admin.getName() + " " + admin.getLastName() + "</strong><br/>" +
                "Email: " + admin.getEmail() + "<br/>Role: " + admin.getRole() + "</html>");
        adminInfo.setFont(Fonts.NORMAL_FONT);
        adminInfo.setForeground(UITheme.getTextColor());
        card.add(adminInfo, BorderLayout.CENTER);

        // Botón para degradar admin a user
        JPanel actionsPanel = new JPanel();
        actionsPanel.setBackground(UITheme.getSecondaryColor());

        JButton demoteBtn = ButtonFactory.createDangerButton("Demote to User", null, () -> demoteAdmin(admin));
        actionsPanel.add(demoteBtn);

        card.add(actionsPanel, BorderLayout.SOUTH);

        return card;
    }

    private void demoteAdmin(User admin) {
        ApiResponse<User> response = adminService.updateUserRole(admin.getId(), "user");
        if (response.isSuccess()) {
            NotificationHandler.success("Admin " + response.getData().getName() + " demoted to User.");
            loadAdmins();
        } else {
            NotificationHandler.error("Failed to demote: " + response.getMessage());
        }
    }
}
