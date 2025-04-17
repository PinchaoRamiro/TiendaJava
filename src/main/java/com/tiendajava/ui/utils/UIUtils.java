package com.tiendajava.ui.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class UIUtils {
    public static void applyDarkTheme() {
        UIManager.put("Panel.background", new Color(45, 45, 45));
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(70, 130, 180));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", new Color(60, 63, 65));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("PasswordField.background", new Color(60, 63, 65));
        UIManager.put("PasswordField.foreground", Color.WHITE);
        UIManager.put("PasswordField.selectionBackground", new Color(100, 100, 100));
        UIManager.put("PasswordField.selectionForeground", Color.WHITE);
        UIManager.put("OptionPane.background", new Color(45, 45, 45));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
    }

    public static JTextField createTextField() {
        JTextField field = new JTextField(15);
        field.setBackground(new Color(60, 63, 65));
        field.setForeground(Color.WHITE);
        return field;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(15);
        field.setBackground(new Color(60, 63, 65));
        field.setForeground(Color.WHITE);
        return field;
    }

    public static JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.addActionListener(e -> action.run());
        return button;
    }

    public static JPanel createBasePanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(new Color(45, 45, 45));
        return panel;
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }


}
