package com.tiendajava.ui.screens;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.tiendajava.model.Session;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class DashboardScreen extends JPanel {

    public DashboardScreen(MainUI mainFrame) {
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());

        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        infoPanel.setBackground(UITheme.getPrimaryColor());
        infoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        infoPanel.add(createCard("📧 Email", Session.getInstance().getUser().getEmail()));
        infoPanel.add(createCard("📄 Document", Session.getInstance().getUser().getNumDocument()));
        infoPanel.add(createCard("🏠 Adress", Session.getInstance().getUser().getAddress()));
        infoPanel.add(createCard("📞 Phone", Session.getInstance().getUser().getPhone()));

        add(infoPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.getSecondaryColor());
        card.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(Fonts.SUBTITLE_FONT);
        titleLabel.setForeground(UITheme.getInfoColor());

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(Fonts.NORMAL_FONT);
        valueLabel.setForeground(UITheme.getTextColor());

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }
}
