package com.tiendajava.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.tiendajava.model.Session;
import com.tiendajava.ui.components.FooterPanel;
import com.tiendajava.ui.components.HeaderPanel;
import com.tiendajava.ui.components.SidebarPanel;
import com.tiendajava.ui.screens.AccountSettingsScreen;
import com.tiendajava.ui.screens.ChangePasswordScreen;
import com.tiendajava.ui.screens.LoginScreen;
import com.tiendajava.ui.screens.RegisterScreen;
import com.tiendajava.ui.screens.admin.AdminDashboardScreen;
import com.tiendajava.ui.screens.admin.manageAdmins.ManageAdminsScreen;
import com.tiendajava.ui.screens.admin.products.ProductsAdminScreen;
import com.tiendajava.ui.screens.admin.users.ManageUsersScreen;
import com.tiendajava.ui.screens.user.DashboardUserScreen;
import com.tiendajava.ui.screens.user.ProductsUserScreen;
import com.tiendajava.ui.utils.UIUtils;

public final class MainUI extends JFrame {
    private final JPanel contentPanel = new JPanel(new CardLayout());
    private final JPanel sidebarContainer = new JPanel(new BorderLayout());
    private final HeaderPanel headerPanel = new HeaderPanel(this);
    private final FooterPanel footerPanel = new FooterPanel();

    public MainUI() {
        UIUtils.applyDarkTheme();

        setTitle("RP Store - Java");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(headerPanel, BorderLayout.NORTH);
        add(footerPanel, BorderLayout.SOUTH);
        add(sidebarContainer, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Solo agregamos pantallas públicas al inicio
        contentPanel.add(new LoginScreen(this), "login");
        contentPanel.add(new RegisterScreen(this), "register");

        showScreen("login");
    }

    public void showScreen(String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();

        // Evita cargar pantallas que requieren sesión si no hay usuario
        if (nameRequiresAuth(name) && Session.getInstance().getUser() == null) {
            System.err.println("Blocked navigation to '" + name + "': user not logged in.");
            return;
        }

        // Carga la pantalla solo si no se ha creado aún
        if (getScreen(name) == null) {
            switch (name) {
                case "dashboard" -> contentPanel.add(new DashboardUserScreen(this), "dashboard");
                case "products-user" -> contentPanel.add(new ProductsUserScreen(this), "products-user");
                // case "orders" -> contentPanel.add(new OrdersScreen(this), "orders");
                case "admin-dashboard" -> contentPanel.add(new AdminDashboardScreen(this), "admin-dashboard");
                case "manage-users" -> contentPanel.add(new ManageUsersScreen(this), "manage-users");
                case "manage-admins" -> contentPanel.add(new ManageAdminsScreen(this), "manage-admins");
                case "manage-products" -> contentPanel.add(new ProductsAdminScreen(this), "manage-products");
                case "change-password" -> contentPanel.add(new ChangePasswordScreen(this), "change-password");
                case "account-settings" -> contentPanel.add(new AccountSettingsScreen(this), "account-settings");
            }
        }

        cl.show(contentPanel, name);

        if (nameRequiresAuth(name)) loggedScreen();
        else logoutScreen();
    }

    private boolean nameRequiresAuth(String name) {
        return switch (name) {
            case "dashboard", "products-user", "orders", "admin-dashboard", "manage-users", "manage-admins", "manage-products"
            , "account-settings", "change-password"  -> true;
            default -> false;
        };
    }

    private Component getScreen(String name) {
        for (Component c : contentPanel.getComponents()) {
            if (name.equals(contentPanel.getLayout().toString())) {
                return c;
            }
        }
        return null;
    }

    private void loggedScreen() {
        headerPanel.userLogged();
        sidebarContainer.removeAll();
        sidebarContainer.add(new SidebarPanel(this), BorderLayout.CENTER);
        sidebarContainer.revalidate();
        sidebarContainer.repaint();
    }

    private void logoutScreen() {
        headerPanel.deleteWelcomeMessage();
        sidebarContainer.removeAll();
        sidebarContainer.revalidate();
        sidebarContainer.repaint();
    }

    public static void start() {
        SwingUtilities.invokeLater(() -> new MainUI().setVisible(true));
    }
}
