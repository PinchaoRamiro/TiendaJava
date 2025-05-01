package com.tiendajava.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class SearchBar extends JPanel {

    private final JTextField searchField;
    private final JLabel searchButton;

    public SearchBar(ActionListener onSearch) {
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setPreferredSize(new Dimension(500, 40));

        searchField = new JTextField();
        searchField.setFont(Fonts.NORMAL_FONT);
        searchField.setBackground(UITheme.getSecondaryColor());
        searchField.setForeground(UITheme.getTextColor());
        searchField.setCaretColor(UITheme.getTextColor());
        searchField.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));

        searchField.addActionListener(onSearch);

        searchButton = ButtonFactory.createIconButton( AppIcons.SEARCH_ICON, () -> onSearch.actionPerformed(null));

        add(searchField, BorderLayout.CENTER);
        add(searchButton, BorderLayout.EAST);
    }

    public String getText() {
        return searchField.getText().trim();
    }

    public void clear() {
        searchField.setText("");
    }
}
