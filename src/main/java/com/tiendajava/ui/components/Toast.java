package com.tiendajava.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class Toast extends JWindow {
    public Toast(String message, Color background, Color foreground, int durationMillis) {
        JPanel panel = new JPanel();
        panel.setBackground(background);
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setForeground(foreground);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(label, BorderLayout.CENTER);

        getContentPane().add(panel);
        setSize(300, 50);

        // Position bottom right of screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width - getWidth() - 20, screenSize.height - getHeight() - 50);

        setVisible(true);

        // Close after timeout
        new Timer(durationMillis, e -> dispose()).start();
    }
}
