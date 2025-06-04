package com.tiendajava.ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

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
                button.setFont(Fonts.BUTTON_FONT.deriveFont(Fonts.BUTTON_FONT.getSize() + 2f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setFont(Fonts.BUTTON_FONT);
            }
        });

        return button;
    }

    public static JLabel createIconButton(ImageIcon icon, String tooltip, Runnable onClick) {
        JLabel button = new JLabel(icon);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip); // <- Aquí se añade la ayuda

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(new ImageIcon(icon.getImage().getScaledInstance(26, 26, java.awt.Image.SCALE_SMOOTH)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(icon);
            }
        });

        return button;
    }

    public static class ActionButton extends JButton {
        public ActionButton(String text, ImageIcon icon, Runnable action) {
            super(text, icon);
            setFont(Fonts.NORMAL_FONT);
            setBackground(UITheme.getSecondaryColor());
            setForeground(UITheme.getTextColor());
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.getTertiaryColor(), 1),
                BorderFactory.createEmptyBorder(12, 30, 12, 30)
            ));
            setMinimumSize(new Dimension(160, 45));
            setPreferredSize(new Dimension(200, 50));
            
            addActionListener(e -> action.run());
            
            // Efecto hover
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    setBackground(UITheme.getTertiaryColor());
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    setBackground(UITheme.getSecondaryColor());
                }
            });
        }
    }
}
