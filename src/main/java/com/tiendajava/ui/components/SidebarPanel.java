package com.tiendajava.ui.components;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.tiendajava.service.UserService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.UITheme;

public class SidebarPanel extends JPanel {

    private final UserService userservise = new UserService();

    public SidebarPanel(MainUI frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UITheme.getSecondaryColor());
        setPreferredSize(new Dimension(200, getHeight()));
        setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, UITheme.getTertiaryColor()));

        ImageIcon dashboardIcon = new ImageIcon(getClass().getResource("/icons/store.png"));
        ImageIcon logoutIcon =  new ImageIcon(getClass().getResource("/icons/exit.png")); 

        JButton dashboardBtn = ButtonFactory.createSecondaryButton("Dashboard", dashboardIcon, () -> frame.showScreen("dashboard"));
        dashboardBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        dashboardBtn.setMaximumSize(new Dimension(160, 40));

        JButton logoutBtn = ButtonFactory.createDangerButton("Logout", logoutIcon, () -> {
            SwingUtilities.invokeLater(() -> {           
                userservise.Logout();
                frame.showScreen("login");
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
