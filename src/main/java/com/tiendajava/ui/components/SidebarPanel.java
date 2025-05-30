package com.tiendajava.ui.components;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.tiendajava.model.Session;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.UITheme;

public class SidebarPanel extends JPanel {

    // private final UserService userService = new UserService();
    private final MainUI frame;

    public SidebarPanel(MainUI frame) {
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UITheme.getSecondaryColor());
        setPreferredSize(new Dimension(200, getHeight()));
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 1, UITheme.getTertiaryColor()));

        add(Box.createVerticalStrut(40));

        String role = Session.getInstance().getRole();

        if ("admin".equalsIgnoreCase(role)) {
            addSidebarButton("Admin Dashboard", null, () -> frame.showScreen("admin-dashboard"));
            addSidebarButton("Users", AppIcons.USERS_ICON, () -> frame.showScreen("manage-users"));
            addSidebarButton("Admins", AppIcons.ADMIN_ICON, () -> frame.showScreen("manage-admins"));
            addSidebarButton("Products", AppIcons.PRODUCTS_ICON, () -> frame.showScreen("manage-products"));
            addSidebarButton("Orders", null, () -> frame.showScreen("manage-orders"));
            
        } else {
            addSidebarButton("Dashboard", AppIcons.APP_ICON, () -> frame.showScreen("dashboard"));
            addSidebarButton("Products", AppIcons.PRODUCTS_ICON, () -> frame.showScreen("products-user"));
            addSidebarButton("Cart", AppIcons.CART_ICON, () -> frame.showScreen("cart-p"));
            addSidebarButton("My Orders", null, () -> frame.showScreen("order-history"));
        }

        add(Box.createVerticalGlue());
    }

    private void addSidebarButton(String label, ImageIcon icon, Runnable action) {
        JButton button = ButtonFactory.createSecondaryButton(label, icon, action);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(160, 40));
        add(Box.createVerticalStrut(10));
        add(button);
    }

    public MainUI getFrame() {
        return frame;
    }
}
