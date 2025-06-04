package com.tiendajava.ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.animations.Toast;

public class NotificationHandler {

    private static final int DURATION = 3000; 
    private static Toast toast;

    public static void success(Component parent, String message) {
        toast = new Toast(parent, "✔ " + message, Fonts.SUCCESS_FONT, UITheme.getSuccessColor(), Color.WHITE, DURATION);
        toast.setVisible(true);
    }

    public static void error(Component parent, String message) {
        toast = new Toast(parent, "✖ " + message, Fonts.ERROR_FONT, UITheme.getErrorColor(), Color.WHITE, DURATION);
        toast.setVisible(true);
    }

    public static void warning(Component parent, String message) {
        toast = new Toast(parent, "⚠ " + message, Fonts.WARNING_FONT, UITheme.getWarningColor(), Color.WHITE, DURATION);
        toast.setVisible(true);
    }

    public static void info(Component parent, String message) {
        toast = new Toast(parent, "ℹ " + message, Fonts.INFO_FONT, UITheme.getInfoColor(), Color.WHITE, DURATION);
        toast.setVisible(true);
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

        toast = new Toast(dummy, message, Fonts.INFO_FONT, bgColor, fgColor, DURATION);
        toast.setAlwaysOnTop(true);
    }
}
