package com.tiendajava.ui.screens;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tiendajava.model.Session;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class AccountSettingsScreen extends JPanel {

    private final MainUI parent;

    public AccountSettingsScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());

        JLabel title = new JLabel("Account Settings", SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(UIUtils.getDefaultPadding());
        add(title, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(UITheme.getPrimaryColor());
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(UIUtils.getDefaultPadding());

        JLabel nameLabel = new JLabel("Name: " + Session.getInstance().getUser().getName());
        nameLabel.setForeground(UITheme.getTextColor());
        nameLabel.setFont(Fonts.NORMAL_FONT);

        JLabel emailLabel = new JLabel("Email: " + Session.getInstance().getUser().getEmail());
        emailLabel.setForeground(UITheme.getTextColor());
        emailLabel.setFont(Fonts.NORMAL_FONT);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(emailLabel);

        add(infoPanel, BorderLayout.CENTER);
    }
}
