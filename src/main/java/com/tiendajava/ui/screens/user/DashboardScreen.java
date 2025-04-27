package com.tiendajava.ui.screens.user;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.tiendajava.model.Session;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class DashboardScreen extends JPanel {

    private static final int CARD_PADDING = 20;
    private static final int CARD_BORDER_RADIUS = 15;

    public DashboardScreen(MainUI mainFrame) {
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());

        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 20, 20));
        infoPanel.setBackground(UITheme.getPrimaryColor());
        infoPanel.setBorder(new EmptyBorder(CARD_PADDING, CARD_PADDING, CARD_PADDING, CARD_PADDING));

        infoPanel.add(createCard("📧 Email", Session.getInstance().getUser().getEmail()));
        infoPanel.add(createCard("📄 Document", Session.getInstance().getUser().getNumDocument()));
        infoPanel.add(createCard("🏠 Address", Session.getInstance().getUser().getAddress()));
        infoPanel.add(createCard("📞 Phone", Session.getInstance().getUser().getPhone()));

        add(infoPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.getSecondaryColor());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CARD_BORDER_RADIUS, CARD_BORDER_RADIUS);
                g2.dispose();
            }
        };
        card.setBackground(UITheme.getSecondaryColor());
        card.setBorder(new EmptyBorder(CARD_PADDING, CARD_PADDING, CARD_PADDING, CARD_PADDING));

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
