package com.tiendajava.ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;

import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class ButtonFactory {

    public static JButton createPrimaryButton(String text, Runnable onClick) {
        return createButton(text, UITheme.getPrimaryButtonColor(), UITheme.getTextColor(), onClick);
    }

    public static JButton createSecondaryButton(String text, Runnable onClick) {
        return createButton(text, UITheme.getSecodaryButtonColor(), UITheme.getTextColor(), onClick);
    }

    public static JButton createDangerButton(String text, Runnable onClick) {
        return createButton(text, UITheme.getDangerColor(), UITheme.getTextColor(), onClick);
    }

    public static JButton createDarkButton(String text, Runnable onClick) {
        return createButton(text, UITheme.getPrimaryColor(), UITheme.getTextColor(), onClick);
    }

    public static JButton createButton(String text, Color background, Color foreground, Runnable onClick) {
        JButton button = new JButton(text);

        button.setFocusPainted(false);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(Fonts.BUTTON_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));
        button.addActionListener(e -> onClick.run());

        button.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setBackground(UITheme.getFocusColor());
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setBackground(background);
            }
        });

        return button;
    }
}
