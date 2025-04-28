package com.tiendajava.ui.screens.user;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tiendajava.model.Session;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;
import com.tiendajava.ui.utils.animations.TypingLabel;

public class DashboardUserScreen extends JPanel {

    public DashboardUserScreen(MainUI parent) {
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        JLabel title = new JLabel("RP STORE", UIUtils.LoadIcon("/icons/home.png"), SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(UIUtils.getDefaultPadding(40, 20, 0, 20));

        add(title, BorderLayout.NORTH);

        // Contenido principal
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UITheme.getPrimaryColor());
        centerPanel.setBorder(UIUtils.getDefaultPadding());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 10, 10);
        gbc.gridx = 0;

        // no centrar solo poner abajo del titulo
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.CENTER;

        // Mensaje personalizado para el usuario
        TypingLabel welcomeUserLabel = new TypingLabel(
            "Hello, " + Session.getInstance().getUser().getName() + " 👋",
            80 // milisegundos entre cada letra (más pequeño = más rápido)
        );
        welcomeUserLabel.setFont(Fonts.SUBTITLE_FONT);
        welcomeUserLabel.setForeground(UITheme.getSuccessColor());

        gbc.gridy = 0;
        centerPanel.add(welcomeUserLabel, gbc);
        welcomeUserLabel.startTyping();


        // Breve descripción de la tienda
        JLabel storeInfoLabel = new JLabel("<html><div style='text-align: center; '>RP Store is your trusted place<br>for buying amazing products.<br>Explore categories, add to your cart and order easily!</div></html>");
        storeInfoLabel.setFont(Fonts.NORMAL_FONT);
        storeInfoLabel.setForeground(UITheme.getTextColor());

        gbc.gridy = 1;
        centerPanel.add(storeInfoLabel, gbc);

        // features 
        JLabel featuresLabel = new JLabel("<html><div style='text-align: center;'>Features:<br>- Explore categories<br>- Add products to your cart<br>- Easy checkout</div></html>");
        featuresLabel.setFont(Fonts.NORMAL_FONT);
        featuresLabel.setForeground(UITheme.getTextColor());

        gbc.gridy = 2;
        centerPanel.add(featuresLabel, gbc);

        // Sobre el proyecto
        JLabel aboutLabel = new JLabel("<html><div style='text-align: center;'>This project was built using:<br><b>Java + Swing + REST API</b><br>for learning and practice purposes.</div></html>");
        aboutLabel.setFont(Fonts.SMALL_FONT);
        aboutLabel.setForeground(UITheme.getTextColor());

        gbc.gridy = 4;
        centerPanel.add(aboutLabel, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }
}
