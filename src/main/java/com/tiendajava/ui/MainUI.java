package com.tiendajava.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.tiendajava.ui.components.FooterPanel;
import com.tiendajava.ui.components.HeaderPanel;
import com.tiendajava.ui.components.SidebarPanel;
import com.tiendajava.ui.screens.DashboardScreen;
import com.tiendajava.ui.screens.LoginScreen;
import com.tiendajava.ui.screens.RegisterScreen;
import com.tiendajava.ui.utils.UIUtils;

public class MainUI extends JFrame {
    private final JPanel contentPanel = new JPanel(new CardLayout());
    private final JPanel sidebarContainer = new JPanel(new BorderLayout());
    private final HeaderPanel headerPanel = new HeaderPanel(this);
    private final FooterPanel footerPanel = new FooterPanel();

    public MainUI() {

        UIUtils.applyDarkTheme();

        setTitle("TiendaJava");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(headerPanel, BorderLayout.NORTH);
        add(footerPanel, BorderLayout.SOUTH);
        add(sidebarContainer, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        contentPanel.add(new LoginScreen(this), "login");
        contentPanel.add(new RegisterScreen(this), "register");
        contentPanel.add(new DashboardScreen(this), "dashboard");

        showScreen("login");
    }

    public final void showScreen(String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);

        if(name.equals("dashboard")) loggedScreen();
        else if(name.equals("login")) logoutScreen();

    }

    private void loggedScreen(){
        headerPanel.userLogged();
        sidebarContainer.removeAll();
        sidebarContainer.add(new SidebarPanel(this), BorderLayout.CENTER);
        sidebarContainer.revalidate();
        sidebarContainer.repaint();
    }

    private void logoutScreen(){
        headerPanel.deleteWelcomeMessage();
        sidebarContainer.removeAll();
        sidebarContainer.revalidate();
        sidebarContainer.repaint();
    }

    public static void start() {
        SwingUtilities.invokeLater(() -> new MainUI().setVisible(true));
    }
}