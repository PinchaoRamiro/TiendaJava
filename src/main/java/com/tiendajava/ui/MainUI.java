package com.tiendajava.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.tiendajava.model.Cart;
import com.tiendajava.model.Session;
import com.tiendajava.ui.components.FooterPanel;
import com.tiendajava.ui.components.HeaderPanel;
import com.tiendajava.ui.components.SidebarPanel;
import com.tiendajava.ui.screens.AccountSettingsScreen;
import com.tiendajava.ui.screens.ChangePasswordScreen;
import com.tiendajava.ui.screens.LoginScreen;
import com.tiendajava.ui.screens.RegisterScreen;
import com.tiendajava.ui.screens.admin.AdminDashboardScreen;
import com.tiendajava.ui.screens.admin.PaymentsAdminScreen;
import com.tiendajava.ui.screens.admin.manageAdmins.ManageAdminsScreen;
import com.tiendajava.ui.screens.admin.products.ProductsAdminScreen;
import com.tiendajava.ui.screens.admin.users.ManageUsersScreen;
import com.tiendajava.ui.screens.user.DashboardUserScreen;
import com.tiendajava.ui.screens.user.cart.FunctionalCartScreen;
import com.tiendajava.ui.screens.user.order.OrderHistoryScreen;
import com.tiendajava.ui.screens.user.order.OrderScreen;
import com.tiendajava.ui.screens.user.products.ProductsUserScreen;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public final class MainUI extends JFrame {
    private final JPanel contentPanel = new JPanel(new CardLayout());
    private final JPanel mainContentPanel = new JPanel(new BorderLayout());
    private final HeaderPanel headerPanel = new HeaderPanel(this);
    private final FooterPanel footerPanel = new FooterPanel();
    private final SidebarPanel sidebarPanel = new SidebarPanel(this);
    private final Cart cart = new Cart();
    
    private final GlassPane glassPane = new GlassPane();
    private boolean isLoading = false;

    public MainUI() {
        UIUtils.applyDarkTheme();
        configureWindow();
        setupMainStructure();
        loadInitialScreens();
    }
    
    private void configureWindow() {
        setTitle("RP Store - Java");
        setMinimumSize(new Dimension(700, 500));
        setPreferredSize(new Dimension(1000, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setGlassPane(glassPane); 
    }
    
    private void setupMainStructure() {
        mainContentPanel.setBackground(UITheme.getPrimaryColor());
        
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.add(contentPanel, BorderLayout.CENTER);
        contentWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);
        mainContentPanel.add(sidebarPanel, BorderLayout.WEST);
        mainContentPanel.add(contentWrapper, BorderLayout.CENTER);
        mainContentPanel.add(footerPanel, BorderLayout.SOUTH);

        updateSidebarVisibility();
        
        add(mainContentPanel);
    }
    
    private void loadInitialScreens() {
        // Solo pantallas públicas iniciales
        contentPanel.add(new LoginScreen(this), "login");
        contentPanel.add(new RegisterScreen(this), "register");
        showScreen("login");
    }

    public void showScreen(String name) {
        // Mostrar loading optimizado
        showLoading("Loading " + getScreenDisplayName(name) + "...");
        
        // Usar SwingWorker para carga en segundo plano
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                prepareScreen(name);
                return null;
            }
            
            @Override
            protected void done() {
                performScreenChange(name);
                hideLoading();
            }
        }.execute();
    }
    
    private void prepareScreen(String name) {
        SwingUtilities.invokeLater(() -> {
            if (getScreen(name) == null && nameRequiresAuth(name)) {
                createScreen(name);
            }
        });
    }
    
    private void createScreen(String name) {
        switch (name) {
            case "cart-p" -> contentPanel.add(new FunctionalCartScreen(this, cart), "cart-p");
            case "dashboard" -> contentPanel.add(new DashboardUserScreen(this), "dashboard");
            case "products-user" -> contentPanel.add(new ProductsUserScreen(this), "products-user");
            case "payment" -> contentPanel.add(new OrderScreen(this, cart), "payment");
            case "order-history" -> contentPanel.add(new OrderHistoryScreen(this), "order-history");
            case "admin-dashboard" -> contentPanel.add(new AdminDashboardScreen(this), "admin-dashboard");
            case "manage-users" -> contentPanel.add(new ManageUsersScreen(this), "manage-users");
            case "add-user" -> contentPanel.add(new RegisterScreen(this), "add-user"); 
            case "manage-admins" -> contentPanel.add(new ManageAdminsScreen(this), "manage-admins");
            case "manage-products" -> contentPanel.add(new ProductsAdminScreen(this), "manage-products");
            case "manage-orders" -> contentPanel.add(new PaymentsAdminScreen(this), "manage-orders");
            case "change-password" -> contentPanel.add(new ChangePasswordScreen(this), "change-password");
            case "account-settings" -> contentPanel.add(new AccountSettingsScreen(this), "account-settings");
        }
    }
    
    private void performScreenChange(String name) {
        if (nameRequiresAuth(name) && Session.getInstance().getUser() == null) {
            System.err.println("Access denied: " + name);
            showScreen("login"); 
            return;
        }

        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
        updateUIState(); 
    }
    
    private void updateUIState() {
        boolean isLoggedIn = Session.getInstance().getUser() != null;
        headerPanel.setLoggedInState(isLoggedIn);
        
        updateSidebarVisibility(); 
        if (isLoggedIn) {
            sidebarPanel.clearAndAddButtons(); 
        }
        revalidate();
        repaint();
    }

    public void updateSidebarVisibility() { 
        boolean shouldShowSidebar = Session.getInstance().getUser() != null;
        sidebarPanel.setVisible(shouldShowSidebar);
        mainContentPanel.revalidate(); 
        mainContentPanel.repaint();
    }

    public void showLoading(String message) {
        if (!isLoading) {
            glassPane.setMessage(message);
            glassPane.setVisible(true);
            glassPane.startAnimation();
            isLoading = true;
        }
    }
    
    public void hideLoading() {
        if (isLoading) {
            glassPane.stopAnimation();
            glassPane.setVisible(false);
            isLoading = false;
        }
    }
    
    private static class GlassPane extends JComponent {
        private final JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        
        public GlassPane() {
            setOpaque(false);
            setLayout(new GridBagLayout());
            setBackground(new Color(0, 0, 0, 150));
            
            messageLabel.setFont(Fonts.TITLE_FONT);
            messageLabel.setForeground(Color.WHITE);
            
            JPanel content = new JPanel(new BorderLayout());
            content.setBackground(UITheme.getSecondaryColor());
            content.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
            content.add(messageLabel, BorderLayout.CENTER);
            
            add(content);
        }
        
        public void setMessage(String message) {
            messageLabel.setText(message);
        }
        
        public void startAnimation() {
        }
        
        public void stopAnimation() {
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
        }
    }
    
    private String getScreenDisplayName(String name) {
        return switch (name) {
            case "dashboard" -> "Dashboard";
            case "products-user" -> "Products";
            case "cart-p" -> "Cart";
            case "payment" -> "Payment";
            case "order-history" -> "Order History";
            case "admin-dashboard" -> "Admin Dashboard";
            case "manage-users" -> "Manage Users";
            case "add-user" -> "Add User"; 
            case "manage-admins" -> "Manage Admins";
            case "manage-products" -> "Manage Products";
            case "manage-orders" -> "Manage Orders";
            case "change-password" -> "Change Password";
            case "account-settings" -> "Account Settings";
            case "login" -> "Login"; 
            case "register" -> "Register"; 
            default -> "Unknown Screen: " + name;
        };
    }
    
    private boolean nameRequiresAuth(String name) {
        return switch (name) {
            case "dashboard", "products-user", "cart-p", "payment", "order-history", 
                 "admin-dashboard", "manage-users", "manage-admins", "manage-products", 
                 "manage-orders", "account-settings", "change-password", "add-user" -> true;
            default -> false;
        };
    }
    
    private Component getScreen(String name) {
        for (Component comp : contentPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().equals(name)) {
                return comp;
            }
        }
        return null;
    }
    
    public Cart getCart() {
        return cart;
    }
    
    public static void start() {
        SwingUtilities.invokeLater(() -> {
            MainUI mainUI = new MainUI();
            mainUI.pack();
            mainUI.setVisible(true);
        });
    }
}