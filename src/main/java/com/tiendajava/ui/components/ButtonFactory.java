package com.tiendajava.ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JButton;

import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class ButtonFactory {

    public static  JButton createPrimaryButton(String text, Icon icon, Runnable onClick) {
        return createButton(text, icon, UITheme.getPrimaryButtonColor(), UITheme.getTextColor(), onClick);
    }

    public static JButton createSecondaryButton(String text, Icon icon,  Runnable onClick) {
        return createButton(text, icon,  UITheme.getSecodaryButtonColor(), UITheme.getTextColor(), onClick);
    }

    public static JButton createDangerButton(String text, Icon icon,  Runnable onClick) {
        return createButton(text, icon,  UITheme.getDangerColor(), UITheme.getTextColor(), onClick);
    }

    public static JButton createDarkButton(String text, Icon icon, Runnable onClick) {
        return createButton(text,icon, UITheme.getPrimaryColor(), UITheme.getTextColor(), onClick);
    }

    public static JButton createButton(String text, Icon icon, Color background, Color foreground, Runnable onClick) {
        JButton button = new JButton(text, icon);

        button.setFocusPainted(false);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(Fonts.BUTTON_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));
        button.addActionListener(e -> onClick.run());

        return button;
    }
}
