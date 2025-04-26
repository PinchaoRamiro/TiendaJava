package com.tiendajava.ui.utils;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class UIUtils {
    public static void applyDarkTheme() {
        UIManager.put("Panel.background", UITheme.getPrimaryColor());
        UIManager.put("Label.foreground", UITheme.getTextColor());
        UIManager.put("TextField.background", UITheme.getTertiaryColor());
        UIManager.put("TextField.foreground", UITheme.getTextColor());
        UIManager.put("TextField.caretForeground", UITheme.getTextColor());
        UIManager.put("TextField.border", BorderFactory.createLineBorder(UITheme.getBorderColor()));
        UIManager.put("PasswordField.background",  UITheme.getTertiaryColor());
        UIManager.put("PasswordField.foreground", UITheme.getTextColor());
        UIManager.put("PasswordField.caretForeground", UITheme.getTextColor());
        UIManager.put("PasswordField.border", BorderFactory.createLineBorder(UITheme.getBorderColor()));
    }

    public static JTextField createTextField(Font font) {
        JTextField field = new JTextField(15);
        field.setFont(font);
        return field;
    }

    public static JLabel createTextLabel(String text, Font font){
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(font);
        return textLabel;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(15);
        return field;
    }

        // === Misc ===
    public static Border getDefaultPadding() {
        return BorderFactory.createEmptyBorder(10, 10, 10, 10); // Espaciado por defecto
    }

    public static Border getDefaultPadding(int top, int left, int bottom, int right) {
        return BorderFactory.createEmptyBorder(top, left, bottom, right); // Espaciado personalizado
    }

    public static Border getRoundedBorder() {
        return BorderFactory.createEmptyBorder( ); // Bordes redondeados
    }
}