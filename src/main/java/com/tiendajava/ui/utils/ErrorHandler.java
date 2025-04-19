package com.tiendajava.ui.utils;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ErrorHandler {

    public static void showErrorMessage(Component patern, String title, String message) {
        UIManager.put("OptionPane.messageFont", Fonts.ERROR_FONT);
        UIManager.put("OptionPane.buttonFont", Fonts.ERROR_FONT);
        UIManager.put("OptionPane.background", UITheme.getPrimaryColor());
        UIManager.put("OptionPane.messageForeground", UITheme.getErrorColor());
        UIManager.put("OptionPane.foreground", UITheme.getErrorColor());

        JOptionPane.showMessageDialog(patern, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarningMessage(Component parent,  String title, String message) {
        UIManager.put("OptionPane.messageFont", Fonts.WARNING_FONT);
        UIManager.put("OptionPane.buttonFont", Fonts.WARNING_FONT);
        UIManager.put("OptionPane.background", UITheme.getSecondaryColor());
        UIManager.put("OptionPane.messageForeground", UITheme.getWarningColor());
        UIManager.put("OptionPane.foreground", UITheme.getWarningColor());

        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void showInfoMessage(Component parent, String title, String message) {
        UIManager.put("OptionPane.messageFont", Fonts.INFO_FONT);
        UIManager.put("OptionPane.buttonFont", Fonts.INFO_FONT);
        UIManager.put("OptionPane.background", UITheme.getTertiaryColor());
        UIManager.put("OptionPane.messageForeground", UITheme.getInfoColor());
        UIManager.put("OptionPane.foreground", UITheme.getInfoColor());

        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showSuccessMessage(Component parent, String title, String message) {
        UIManager.put("OptionPane.messageFont", Fonts.SUCCESS_FONT);
        UIManager.put("OptionPane.buttonFont", Fonts.SUCCESS_FONT);
        UIManager.put("OptionPane.background", UITheme.getPrimaryColor());
        UIManager.put("OptionPane.messageForeground", UITheme.getSuccessColor());
        UIManager.put("OptionPane.foreground", UITheme.getSuccessColor());

        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
