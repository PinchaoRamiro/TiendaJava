package com.tiendajava.ui.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.Timer;

public class NotificationHandler {

    private static final int DURATION = 3000; // Duration in milliseconds

    public static void success(Component parent, String message) {
        showToast(parent, "✔ " + message, UITheme.getSuccessColor(), Color.WHITE);
    }

    public static void error(Component parent, String message) {
        showToast(parent, "✖ " + message, UITheme.getErrorColor(), Color.WHITE);
    }

    public static void warning(Component parent, String message) {
        showToast(parent, "⚠ " + message, UITheme.getWarningColor(), Color.WHITE);
    }

    public static void info(Component parent, String message) {
        showToast(parent, "ℹ " + message, UITheme.getInfoColor(), Color.WHITE);
    }

    private static void showToast(Component parent, String message, Color bgColor, Color fgColor) {
        JWindow toast = new JWindow();

        JLabel label = new JLabel(message);
        label.setOpaque(true);
        label.setBackground(bgColor);
        label.setForeground(fgColor);
        label.setFont(Fonts.NORMAL_FONT);
        label.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        toast.getContentPane().add(label);
        toast.pack();

        // Try to position the toast near the parent component, otherwise center it
        int x, y;
        try {
            Point parentLocation = parent.getLocationOnScreen();
            x = parentLocation.x + (parent.getWidth() - toast.getWidth()) / 2;
            y = parentLocation.y + parent.getHeight() - toast.getHeight() - 50;
        } catch (IllegalComponentStateException e) {
            // If parent isn't visible or not available, fallback to center of screen
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            x = (screenSize.width - toast.getWidth()) / 2;
            y = (screenSize.height - toast.getHeight()) / 2;
        }

        toast.setLocation(x, y);
        toast.setAlwaysOnTop(true);
        toast.setVisible(true);

        new Timer(DURATION, e -> toast.dispose()).start();
    }

    public static void success(String message) {
        showToastToCenter(message, UITheme.getSuccessColor(), Color.WHITE);
    }

    public static void error(String message) {
        showToastToCenter(message, UITheme.getErrorColor(), Color.WHITE);
    }

    public static void warning(String message) {
        showToastToCenter(message, UITheme.getWarningColor(), Color.WHITE);
    }

    public static void info(String message) {
        showToastToCenter(message, UITheme.getInfoColor(), Color.WHITE);
    }

    private static void showToastToCenter(String message, Color bgColor, Color fgColor) {
        JFrame dummy = new JFrame(); 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dummy.setLocation(screenSize.width / 2, screenSize.height / 2);
        dummy.setSize(1, 1);
        showToast(dummy, message, bgColor, fgColor);
    }
}
