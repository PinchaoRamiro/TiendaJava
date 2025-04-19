package com.tiendajava.ui.components;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.tiendajava.ui.MainFrame;
import com.tiendajava.ui.screens.DashboardScreen;
import com.tiendajava.ui.screens.LoginScreen;
import com.tiendajava.ui.utils.UITheme;

public class SidebarPanel extends JPanel {

    public SidebarPanel(MainFrame frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UITheme.getSecondaryColor());
        setPreferredSize(new Dimension(200, getHeight()));
        setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, UITheme.getTertiaryColor()));

        JButton dashboardBtn = ButtonFactory.createSecondaryButton("Dashboard", () -> frame.showScreen(new DashboardScreen()));
        dashboardBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        dashboardBtn.setMaximumSize(new Dimension(160, 40));

        JButton logoutBtn = ButtonFactory.createDangerButton("Logout", () -> {
            SwingUtilities.invokeLater(() -> {
                LoginScreen.start();
                frame.dispose();
            });
        });
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(160, 40));

        add(Box.createVerticalStrut(40));
        add(dashboardBtn);
        add(Box.createVerticalStrut(20));
        add(logoutBtn);
        add(Box.createVerticalGlue());
    }
}
