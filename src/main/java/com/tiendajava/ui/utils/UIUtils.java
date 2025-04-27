package com.tiendajava.ui.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;

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
        return BorderFactory.createLineBorder(UITheme.getTertiaryColor(), 1, true);
    }

    public static BasicScrollBarUI createDarkScrollBar() {
        return new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = UITheme.getTertiaryColor();
                this.trackColor = UITheme.getPrimaryColor();
                this.thumbHighlightColor = UITheme.getTertiaryColor();
                this.thumbDarkShadowColor = UITheme.getTertiaryColor();
                this.thumbLightShadowColor = UITheme.getTertiaryColor();
            }
        };
    }

    // Padding alrededor del scroll
    public static Border getScrollPaneBorder() {
        return BorderFactory.createEmptyBorder(10, 10, 10, 10); // Espaciado alrededor del scroll
    }

    // create Style form field beautiful

    public static void styleFormFields(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JTextField textField) {
                textField.setBackground(UITheme.getTertiaryColor());
                textField.setForeground(UITheme.getTextColor());
                textField.setBorder(BorderFactory.createLineBorder(UITheme.getBorderColor()));
            } else if (component instanceof JPasswordField passwordField) {
                passwordField.setBackground(UITheme.getTertiaryColor());
                passwordField.setForeground(UITheme.getTextColor());
                passwordField.setBorder(BorderFactory.createLineBorder(UITheme.getBorderColor()));
            }
        }
    }

    // create form field with label and text field
    public static JPanel createFormField(String labelText, JComponent inputField) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UITheme.getPrimaryColor());

        JLabel label = new JLabel(labelText);
        label.setFont(Fonts.NORMAL_FONT);
        label.setForeground(UITheme.getTextColor());

        panel.add(label, BorderLayout.WEST);
        panel.add(inputField, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        return panel;
    }

}