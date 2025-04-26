package com.tiendajava.ui.components;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MediaTracker;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.tiendajava.model.Session;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.UITheme;

public class SidebarPanel extends JPanel {

    // private final UserService userService = new UserService();

    public SidebarPanel(MainUI frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UITheme.getSecondaryColor());
        setPreferredSize(new Dimension(200, getHeight()));
        setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, UITheme.getTertiaryColor()));

        add(Box.createVerticalStrut(40)); // spacing

        String role = Session.getInstance().getRole();

        if ("admin".equalsIgnoreCase(role)) {
            addSidebarButton("Admin Dashboard", "/icons/user.png", () -> frame.showScreen("admin-dashboard"));
            addSidebarButton("Users", "/icons/users.png", () -> frame.showScreen("manage-users"));
            addSidebarButton("Admins", "/icons/admin-alt.png", () -> frame.showScreen("manage-admins"));
            addSidebarButton("Products", "/icons/box.png", () -> frame.showScreen("manage-products"));
        } else {
            addSidebarButton("Dashboard", "/icons/store.png", () -> frame.showScreen("dashboard"));
            addSidebarButton("Products", "/icons/selling.png", () -> frame.showScreen("products-user"));
            addSidebarButton("Cart", "/icons/Cart.png", () -> frame.showScreen("orders"));
        }

        add(Box.createVerticalGlue());
    }

    private void addSidebarButton(String label, String iconPath, Runnable action) {
        ImageIcon icon = loadIcon(iconPath);
        JButton button = ButtonFactory.createSecondaryButton(label, icon, action);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(160, 40));
        add(Box.createVerticalStrut(10));
        add(button);
    }

    private ImageIcon loadIcon(String resourcePath) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(resourcePath));
            if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.err.println("Error loading icon: " + resourcePath);
                return null;
            }
            return icon;
        } catch (Exception e) {
            System.err.println("Error loading icon: " + resourcePath);
            return null;
        }
    }
}
