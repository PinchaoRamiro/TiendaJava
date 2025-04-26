package com.tiendajava.ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class ButtonFactory {

    public static JButton createPrimaryButton(String text, Icon icon, Runnable onClick) {
        return createStyledButton(text, icon, UITheme.getPrimaryButtonColor(), UITheme.getTextColor(), onClick);
    }

    public static JButton createSecondaryButton(String text, Icon icon, Runnable onClick) {
        return createStyledButton(text, icon, UITheme.getSecodaryButtonColor(), UITheme.getTextColor(), onClick);
    }

    public static JButton createDangerButton(String text, Icon icon, Runnable onClick) {
        return createStyledButton(text, icon, UITheme.getDangerColor(), UITheme.getTextColor(), onClick);
    }

    public static JButton createDarkButton(String text, Icon icon, Runnable onClick) {
        return createStyledButton(text, icon, UITheme.getPrimaryColor(), UITheme.getTextColor(), onClick);
    }

    private static JButton createStyledButton(String text, Icon icon, Color bgColor, Color fgColor, Runnable onClick) {
        JButton button = new JButton(text, icon);
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(Fonts.BUTTON_FONT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 40));
        button.setBorder(BorderFactory.createLineBorder(UITheme.getBorderColor(), 1));
        button.setOpaque(true);

        button.addActionListener(e -> onClick.run());

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }
}
