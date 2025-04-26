package com.tiendajava.ui.components;

import java.awt.BorderLayout;
import java.awt.Cursor;
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
    private JLabel welcomeLabel = new JLabel();;
    private final UserService userService = new UserService();
    private JButton settingsButton;
    private final MainUI parent;
    private final JPopupMenu settingsMenu = new JPopupMenu();

    public HeaderPanel(MainUI parent) {
        this.parent = parent;

        setLayout(new BorderLayout());
        setBackground(UITheme.getSecondaryColor());
        setPreferredSize(new Dimension(0, 50));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.getTertiaryColor()));

        add(buildLogo(), BorderLayout.WEST);
        add(buildWelcomeLabel(), BorderLayout.CENTER);
    }

    private JLabel buildLogo() {
        JLabel logoLabel = new JLabel(loadIcon("/icons/logo.png"));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 10));
        return logoLabel;
    }

    private JLabel buildWelcomeLabel() {
        welcomeLabel = new JLabel("Welcome", SwingConstants.CENTER);
        welcomeLabel.setForeground(UITheme.getTextColor());
        welcomeLabel.setFont(Fonts.TITLE_FONT);
        return welcomeLabel;
    }

    public void userLogged() {
        var user = Session.getInstance().getUser();
        welcomeLabel.setText("Welcome, " + user.getName() + " " + user.getLastName());

        if (settingsButton == null) {
            createSettingsButton();
            buildSettingsMenu();
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

    private void createSettingsButton() {
        settingsButton = ButtonFactory.createSecondaryButton("Settings", loadIcon("/icons/settings.png"), () -> {
            settingsMenu.show(settingsButton, 0, settingsButton.getHeight());
        });

        settingsButton.setPreferredSize(new Dimension(140, 40));
        settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(settingsButton, BorderLayout.EAST);
    }

    private void buildSettingsMenu() {
        settingsMenu.removeAll(); // limpiar si ya existía

        settingsMenu.setFont(Fonts.NORMAL_FONT);
        settingsMenu.setBackground(UITheme.getSecondaryColor());

        JMenuItem accountItem = createMenuItem("Account", "/icons/user.png", () -> {
            System.out.println("Open account settings");
        });

        JMenuItem passwordItem = createMenuItem("Change Password", "/icons/door-key.png", () -> {
            System.out.println("Open password change");
        });

        JMenuItem logoutItem = createMenuItem("Logout", "/icons/exit.png", () -> {
            userService.Logout();
            parent.showScreen("login");
        });
        logoutItem.setBackground(UITheme.getDangerColor());

        settingsMenu.add(accountItem);
        settingsMenu.addSeparator();
        settingsMenu.add(passwordItem);
        settingsMenu.addSeparator();
        settingsMenu.add(logoutItem);
    }

    private JMenuItem createMenuItem(String text, String iconPath, Runnable action) {
        JMenuItem item = new JMenuItem(text, loadIcon(iconPath));
        item.setFont(Fonts.NORMAL_FONT);
        item.setBackground(UITheme.getButtonColor());
        item.setForeground(UITheme.getTextColor());
        item.addActionListener(e -> action.run());
        return item;
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
