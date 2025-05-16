package com.tiendajava.ui.screens.admin.manageAdmins;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.components.SearchBar;
import com.tiendajava.ui.components.dialogs.ShowInfoDialog;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;
import static com.tiendajava.ui.utils.UIUtils.getRoundedBorder;

public class ManageAdminsScreen extends JPanel {

    private final MainUI parent;
    private final AdminService adminService = new AdminService();
    private final JPanel adminsPanel = new JPanel(new GridLayout(0, 2, 15, 15));
    private final SearchBar searchBar;
    private List<User> admins;

    public ManageAdminsScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout(15,15));
        setBackground(UITheme.getPrimaryColor());

        // === Panel superior (título + acciones) ===
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(UITheme.getPrimaryColor());

        JLabel title = new JLabel("Manage Admins", AppIcons.ADMIN_ICON, SwingConstants.CENTER);
        title.setMaximumSize(new Dimension(20,10 ));
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row1.setOpaque(false);
        row1.add(title);

        JButton createAdminBtn = ButtonFactory.createPrimaryButton("Add Admin", AppIcons.USER_PLUS_ICON, this::registerAdmin);
        createAdminBtn.setPreferredSize(new Dimension(150, 40));

        searchBar = new SearchBar(e -> searchAdmin());

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        row2.setOpaque(false);
        row2.add(createAdminBtn);
        row2.add(searchBar);

        topPanel.add(row1);
        topPanel.add(row2);
        add(topPanel, BorderLayout.NORTH);

        // === Panel de admins con scroll ===
        adminsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(adminsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        // === Carga de datos ===
        getAdminDataBase();
        loadAdmins();
    }

    private void getAdminDataBase(){
        ApiResponse<List<User>> response = adminService.getAllAdmins();
        admins = response.isSuccess() ? response.getData() : null;
    }

    private void loadAdmins() {
        adminsPanel.removeAll();
        printUserData(admins);
        adminsPanel.revalidate();
        adminsPanel.repaint();
    }

    private void printUserData(List<User> AdminData){
        if (AdminData != null && !AdminData.isEmpty()) {
            for (User admin : AdminData) {
                adminsPanel.add(createAdminCard(admin));
            }
        } else {
            JLabel noData = new JLabel("No admins found", SwingConstants.CENTER);
            noData.setFont(Fonts.NORMAL_FONT);
            noData.setForeground(UITheme.getTextColor());
            adminsPanel.add(noData);
        }
    }

    private void registerAdmin() {
        RegisterAdminDialog dialog = new RegisterAdminDialog();
        dialog.setOnAdminRegistered(newAdmin -> {
            admins.add(newAdmin);
            loadAdmins();
        });
        dialog.setVisible(true);
    }

    private JPanel createAdminCard(User admin) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.getSecondaryColor());
        card.setBorder(getRoundedBorder());
        card.setPreferredSize(new Dimension(300, 240));

        String AdminInfoHtml = String.format(
            "<html>" +
                "<div style='padding: 10px; font-family: sans-serif;'>" +
                    "<div style='font-size: 14px; color: #999;'>Full Name: <div style='color:white; font-size: 14px; font-weight: bold;'>%s %s</div>  </div>" +
                    "<div style='margin-top: 8px; font-size: 14px; color: #999;'>Email:  <div style='color:white;  font-size: 14px;'>%s</div> </div>"+
                "</div>" +
            "</html>",
            admin.getName(), admin.getLastName(),
            admin.getEmail()
        );

        JLabel adminInfo = new JLabel(AdminInfoHtml);
        adminInfo.setFont(Fonts.NORMAL_FONT);
        adminInfo.setForeground(UITheme.getTextColor());
        card.add(adminInfo, BorderLayout.CENTER);

        JPanel actionsPanel = new JPanel();
        actionsPanel.setBackground(UITheme.getSecondaryColor());

        // Botón para ver info admin
        JLabel infoBtn = ButtonFactory.createIconButton(AppIcons.USER_ICON, "Information User", () -> infoComplete(admin));
        infoBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        actionsPanel.add(infoBtn, BorderLayout.WEST);

        // Botón para eliminar admin  
        ImageIcon deleteIcon = AppIcons.DELETE_ICON;
        ImageIcon iconDanger = UIUtils.tintImage(deleteIcon, UITheme.getDangerColor());
        JLabel deleteBtn = ButtonFactory.createIconButton(iconDanger, "Delete", () -> {
            ApiResponse<String> response = adminService.deleteUser(admin.getId());
            if (response.isSuccess()) {
                NotificationHandler.success("Admin " + admin.getName() + " deleted successfully.");
                admins.remove(admin);
                loadAdmins();
            } else {
                NotificationHandler.error("Failed to delete: " + response.getMessage());
            }
        });
        deleteBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));   
        actionsPanel.add(deleteBtn);

        card.add(actionsPanel, BorderLayout.SOUTH);

        return card;
    }

    private void searchAdmin(){
        adminsPanel.removeAll(); 
        String keyword = searchBar.getText();
        ApiResponse<List<User>> response = adminService.searchAdmins(keyword);
        List<User> adminFind = response.isSuccess() ? response.getData() : null;
        if(adminFind == null){
            NotificationHandler.error("No products found with the name: " + keyword);
            loadAdmins();
            return;
        }
        printUserData(adminFind); // Mostrar los administradores filtrados
        adminsPanel.revalidate();
        adminsPanel.repaint();
        System.out.println("Searching: " + keyword);
    }

    public MainUI getParentMA() {
        return parent;
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
}
