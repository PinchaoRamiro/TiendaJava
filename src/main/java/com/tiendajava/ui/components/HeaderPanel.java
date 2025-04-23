package com.tiendajava.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MediaTracker;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import com.tiendajava.model.Session;
import com.tiendajava.service.UserService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class HeaderPanel extends JPanel {
    private final JLabel welcomeLabel;
    private final UserService userService = new UserService();
    private JButton settingsButton;
    private final MainUI parent;

    public HeaderPanel(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getSecondaryColor());
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, UITheme.getTertiaryColor()));
        setPreferredSize(new Dimension(0, 50));

        JLabel logoLabel = new JLabel(loadIcon("/icons/logo.png"));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(logoLabel, BorderLayout.WEST);

        welcomeLabel = new JLabel("Welcome", SwingConstants.CENTER);
        welcomeLabel.setForeground(UITheme.getTextColor());
        welcomeLabel.setFont(Fonts.TITLE_FONT);
        add(welcomeLabel, BorderLayout.CENTER);
    }

    public void userLogged() {
        welcomeLabel.setText("Welcome, " + Session.getInstance().getUser().getName() + " " + Session.getInstance().getUser().getLastName());

        if (settingsButton == null) {
            createSettingsMenu();
        }

        revalidate();
        repaint();
    }

    public void deleteWelcomeMessage() {
        welcomeLabel.setText("Welcome");
        if (settingsButton != null) {
            remove(settingsButton);
            settingsButton = null;
            revalidate();
            repaint();
        }
    }

    private void createSettingsMenu() {
        JPopupMenu settingsMenu = new JPopupMenu();

        settingsMenu.setFont(Fonts.BUTTON_FONT);
        settingsMenu.setBackground(UITheme.getButtonColor());
        settingsMenu.setForeground(UITheme.getTextColor());


        JMenuItem accountItem = new JMenuItem("Cuenta");
        accountItem.setBackground(UITheme.getButtonColor());
        accountItem.setIcon(loadIcon("/icons/user.png"));
        accountItem.addActionListener(e -> {
            System.out.println("Abrir configuración de cuenta");
            // Aquí puedes cambiar a la pantalla de configuración
        });

        JMenuItem passwordItem = new JMenuItem("Cambiar Contraseña");
        passwordItem.setBackground(UITheme.getButtonColor());
        passwordItem.setIcon(loadIcon("/icons/door-key.png"));
        passwordItem.addActionListener(e -> {
            System.out.println("Abrir cambio de contraseña");
        });

        JMenuItem logoutItem = new JMenuItem("Cerrar sesión");
        logoutItem.setBackground(UITheme.getDangerColor());
        logoutItem.setIcon(loadIcon("/icons/exit.png"));
        logoutItem.addActionListener(e -> {
            userService.Logout();
            parent.showScreen("login");
        });

        settingsMenu.add(accountItem);
        settingsMenu.addSeparator();
        settingsMenu.add(passwordItem);
        settingsMenu.addSeparator();
        settingsMenu.add(logoutItem);

        settingsButton = ButtonFactory.createSecondaryButton("Settings", loadIcon("/icons/settings.png"), () -> {
            settingsMenu.show(settingsButton, 0, settingsButton.getHeight());
        });

        add(settingsButton, BorderLayout.EAST);
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
