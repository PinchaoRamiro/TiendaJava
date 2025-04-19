package com.tiendajava.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class HeaderPanel extends JPanel {

    public HeaderPanel(String userName, String lastName) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 50));
        setBackground(UITheme.getSecondaryColor());
        setBorder(BorderFactory.createLineBorder(UITheme.getTertiaryColor(), 1));

        JLabel welcomeLabel = new JLabel("Bienvenido, " + userName + " " + lastName);
        welcomeLabel.setForeground(UITheme.getTextColor());
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 5, 0));
        welcomeLabel.setFont(Fonts.SUBTITLE_FONT);

        add(welcomeLabel, BorderLayout.WEST);
    }
}
