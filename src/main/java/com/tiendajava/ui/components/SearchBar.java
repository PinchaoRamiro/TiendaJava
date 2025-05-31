package com.tiendajava.ui.components;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme; 

public class SearchBar extends JPanel {

    private final JTextField searchField;

    private static final String PLACEHOLDER_TEXT = "Search...";

    public SearchBar(ActionListener onSearch) {
        setLayout(new BorderLayout(5, 0)); 
        setOpaque(false); 
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2)); 

        searchField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(UITheme.getSecodaryButtonColor().darker()); 
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15); 

                super.paintComponent(g2); 
                g2.dispose();
            }
        };
        searchField.setFont(Fonts.NORMAL_FONT);
        searchField.setForeground(UITheme.getTextColor());
        searchField.setOpaque(false);
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        searchField.setText(PLACEHOLDER_TEXT);
        searchField.setForeground(UITheme.getTextColor()); 
        searchField.setBackground(UITheme.getSecodaryButtonColor());

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(PLACEHOLDER_TEXT)) {
                    searchField.setText("");
                    searchField.setForeground(UITheme.getTextColor());
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(PLACEHOLDER_TEXT);
                    searchField.setForeground(UITheme.getTextColor());
                }
            }
        });

        searchField.addActionListener(onSearch);

        JLabel searchButtonIcon = ButtonFactory.createIconButton(
            AppIcons.SEARCH_ICON,
            "Search",
            () -> onSearch.actionPerformed(null)
        ); 

        add(searchField, BorderLayout.CENTER);
        add(searchButtonIcon, BorderLayout.EAST);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(UITheme.getSecondaryColor().brighter()); 
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); 
        g2.dispose();
    }


    public String getText() {
        String text = searchField.getText().trim();
        if (text.equals(PLACEHOLDER_TEXT) || text.isEmpty()) {
            return "";
        }
        return text;
    }

    public void clear() {
        searchField.setText(PLACEHOLDER_TEXT);
        searchField.setForeground(UITheme.getTextColor().darker());
    }
}