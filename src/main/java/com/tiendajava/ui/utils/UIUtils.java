package com.tiendajava.ui.utils;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

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

    public static JTextField createTextField() {
        JTextField field = new JTextField(15);
        return field;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(15);
        return field;
    }
}