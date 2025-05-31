package com.tiendajava.ui.components;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;

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
import com.tiendajava.ui.components.dialogs.ConfirmationDialog;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class HeaderPanel extends JPanel {
    private JLabel welcomeLabel = new JLabel();;
    private final UserService userService = new UserService();
    private JButton settingsButton;
    private final MainUI parent;
    private final JPopupMenu settingsMenu = new JPopupMenu();

    public HeaderPanel(MainUI parent) {
        this.parent = parent; // Assigning the constructor parameter to the final field

        setLayout(new BorderLayout());
        setBackground(UITheme.getSecondaryColor());
        setPreferredSize(new Dimension(0, 50));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.getTertiaryColor()));

        add(buildLogo(), BorderLayout.WEST);
        add(buildWelcomeLabel(), BorderLayout.CENTER);
    }

    private JLabel buildLogo() {
        JLabel logoLabel = new JLabel(AppIcons.LOGO_ICON);
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
        settingsButton = ButtonFactory.createSecondaryButton("Settings", AppIcons.SETTINGS_ICON, () -> {
            settingsMenu.show(settingsButton, 0, settingsButton.getHeight());
        });
        

        settingsButton.setPreferredSize(new Dimension(140, 40));
        settingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(settingsButton, BorderLayout.EAST);
    }

    private void buildSettingsMenu() {
        settingsMenu.removeAll(); 

        settingsMenu.setFont(Fonts.NORMAL_FONT);
        settingsMenu.setBackground(UITheme.getSecondaryColor());

        JMenuItem accountItem = createMenuItem("Account", AppIcons.USER_ICON, () -> {
            parent.showScreen("account-settings");
        });

        JMenuItem passwordItem = createMenuItem("Change Password", AppIcons.KEYS_ICON, () -> {
            parent.showScreen("change-password");
        });

        JMenuItem logoutItem = createMenuItem("Logout", AppIcons.EXIT_ICON, () -> logout());

        logoutItem.setBackground(UITheme.getDangerColor());

        settingsMenu.add(accountItem);
        settingsMenu.addSeparator();
        settingsMenu.add(passwordItem);
        settingsMenu.addSeparator();
        settingsMenu.add(logoutItem);
    }

    private void logout() {
        new ConfirmationDialog( "Logout Confirmation", UITheme.getDangerColor(), "Are you sure you want to logout?", () -> {
            userService.Logout();
            parent.showScreen("login");
        }).setVisible(true);
    }

    private JMenuItem createMenuItem(String text, ImageIcon icon, Runnable action) {
        JMenuItem item = new JMenuItem(text, icon);
        item.setFont(Fonts.NORMAL_FONT);
        item.setBackground(UITheme.getSecodaryButtonColor());
        item.setForeground(UITheme.getTextColor());
        item.addActionListener(e -> action.run());
        return item;
    }

    public void setLoggedInState(boolean loggedIn) {
        if (loggedIn) {
            userLogged();
        } else {
            deleteWelcomeMessage();
        }
    }
}
