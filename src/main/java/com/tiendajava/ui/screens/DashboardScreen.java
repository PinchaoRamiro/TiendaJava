package com.tiendajava.ui.screens;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class DashboardScreen extends JPanel {

    public DashboardScreen() {
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Home Panel", SwingConstants.CENTER);

        label.setFont(Fonts.TITLE_FONT);
        label.setForeground(UITheme.getTextColor());

        add(label, BorderLayout.CENTER);
    }
}
