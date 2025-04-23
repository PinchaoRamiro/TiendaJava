package com.tiendajava.ui.screens;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class DashboardScreen extends JPanel {

    public DashboardScreen(MainUI mainFrame) {
        setLayout(new BorderLayout());

        ImageIcon homeIcon = new ImageIcon(getClass().getResource("/icons/home.png"));

        JLabel label = new JLabel("Home Panel", homeIcon, SwingConstants.CENTER);

        label.setFont(Fonts.TITLE_FONT);
        label.setForeground(UITheme.getTextColor());

        add(label, BorderLayout.CENTER);
    }
}