// Estructura base del nuevo JFrame principal con layout tipo dashboard
package com.tiendajava.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tiendajava.ui.components.HeaderPanel;
import com.tiendajava.ui.components.SidebarPanel;
import com.tiendajava.ui.screens.DashboardScreen;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public final class MainFrame extends JFrame {

    private final JPanel contentPanel;

    public MainFrame(String userName, String lastName) {
        setTitle("TiendaJava - Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        UIUtils.applyDarkTheme();

        SidebarPanel sidebar = new SidebarPanel(this);
        add(sidebar, BorderLayout.WEST);

        HeaderPanel header = new HeaderPanel(userName, lastName);
        add(header, BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UITheme.getPrimaryColor());
        add(contentPanel, BorderLayout.CENTER);

        showScreen(new DashboardScreen());
    }

    public void showScreen(JPanel screen) {
        contentPanel.removeAll();
        contentPanel.add(screen, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
