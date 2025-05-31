package com.tiendajava.ui.screens.admin.manageAdmins;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    private static final int TOP_PANEL_PADDING = 20;
    private static final int TOP_PANEL_HORIZONTAL_GAP = 15;
    private static final int TOP_PANEL_VERTICAL_GAP = 7;

    public ManageAdminsScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout(15,15));

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(UITheme.getPrimaryColor());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, TOP_PANEL_PADDING, 0, TOP_PANEL_PADDING));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(TOP_PANEL_VERTICAL_GAP, TOP_PANEL_HORIZONTAL_GAP, TOP_PANEL_VERTICAL_GAP, TOP_PANEL_HORIZONTAL_GAP);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("Manage Admins", AppIcons.ADMIN_ICON, SwingConstants.CENTER);
        title.setMaximumSize(new Dimension(20,10 ));
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(title, gbc);

        JPanel buttonSearchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, TOP_PANEL_HORIZONTAL_GAP, 0));
        buttonSearchPanel.setBackground(UITheme.getPrimaryColor());

        JButton createProductBtn = ButtonFactory.createPrimaryButton("Register Admin", AppIcons.ADMIN_ICON, this::registerAdmin);
        buttonSearchPanel.add(createProductBtn);

        searchBar = new SearchBar(e -> searchAdmin());

        buttonSearchPanel.add(searchBar);

        gbc.gridy = 1;
        topPanel.add(buttonSearchPanel, gbc);

        add(topPanel, BorderLayout.NORTH);

        adminsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(adminsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        getAdminDataBase();
        loadAdmins();
    }

    private void getAdminDataBase(){
        ApiResponse<List<User>> response = adminService.getAllAdmins();
        System.out.println("Response: " + response);
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

        JLabel infoBtn = ButtonFactory.createIconButton(AppIcons.USER_ICON, "Information User", () -> infoComplete(admin));
        infoBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        actionsPanel.add(infoBtn, BorderLayout.WEST);

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
        String keyword = searchBar.getText();

        if(keyword.isEmpty()){
            NotificationHandler.warning(this, "Please enter a search query.");
            return;
        }

        ApiResponse<List<User>> response = adminService.searchAdmins(keyword);
        List<User> adminFind = response.isSuccess() ? response.getData() : null;
        if(adminFind == null){
            NotificationHandler.error("No products found with the name: " + keyword);
            return;
        }
        adminsPanel.removeAll(); 
        printUserData(adminFind); 
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
