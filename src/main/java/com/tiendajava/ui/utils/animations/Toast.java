package com.tiendajava.ui.utils.animations;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.Timer;

public class Toast extends JWindow {
    public Toast(Component parent, String message, Font font, Color background, Color foreground, int durationMillis) {
        JLabel label = new JLabel(message);
        label.setOpaque(true);
        label.setBackground(background);
        label.setForeground(foreground);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        getContentPane().add(label);
        pack();

        int x, y;
        try {
            Point parentLocation = parent.getLocationOnScreen();
            x = parentLocation.x + (parent.getWidth() - getWidth()) / 2;
            y = parentLocation.y + parent.getHeight() - getHeight() - 50;
        } catch (IllegalComponentStateException e) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            x = (screenSize.width - getWidth()) / 2;
            y = (screenSize.height - getHeight()) / 2;
        }

        
        setLocation(x, y);
        setAlwaysOnTop(true);

        setVisible(true);

        new Timer(durationMillis, e -> dispose()).start();
    }
}
