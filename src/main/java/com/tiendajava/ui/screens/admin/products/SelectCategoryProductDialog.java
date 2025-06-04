package com.tiendajava.ui.screens.admin.products;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout; 
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class SelectCategoryProductDialog extends JDialog {
    private static final int DIALOG_WIDTH = 600;
    private static final int DIALOG_HEIGHT = 300; 
    private static final int PADDING = 15;
    private static final int TITLE_BOTTOM_PADDING = 10;
    private static final int BUTTON_PANEL_H_GAP = 15; 
    private static final int BUTTON_PANEL_V_GAP = 15; 
    private static final Dimension BUTTON_PREFERRED_SIZE = new Dimension(120, 50); 

    public SelectCategoryProductDialog(JFrame parent, Consumer<String> onCategorySelected) {
        super(parent, "Select Category", true);

        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(parent);
        setResizable(false); 
        getContentPane().setBackground(UITheme.getPrimaryColor());
        setLayout(new BorderLayout(PADDING, PADDING));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        titlePanel.setBackground(UITheme.getPrimaryColor());
        JLabel title = new JLabel("Select a Category to Create Product", AppIcons.BOX_ICON, SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, TITLE_BOTTOM_PADDING, PADDING)); 
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_PANEL_H_GAP, BUTTON_PANEL_V_GAP));
        buttonPanel.setBackground(UITheme.getPrimaryColor());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, PADDING, PADDING, PADDING)); 

        JButton clothingBtn = createCategoryButton("Clothing", AppIcons.BOX_ICON, onCategorySelected); 
        JButton electronicsBtn = createCategoryButton("Electronics", AppIcons.BOX_ICON, onCategorySelected);
        JButton furnitureBtn = createCategoryButton("Furniture", AppIcons.BOX_ICON, onCategorySelected);

        buttonPanel.add(clothingBtn);
        buttonPanel.add(electronicsBtn);
        buttonPanel.add(furnitureBtn);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createCategoryButton(String category, ImageIcon icon, Consumer<String> onSelect) {
        JButton button = new JButton(category, icon);
        button.setFocusPainted(false); 
        button.setFont(Fonts.NORMAL_FONT);
        button.setForeground(UITheme.getTextColor());
        button.setBackground(UITheme.getPrimaryButtonColor());
        button.setPreferredSize(BUTTON_PREFERRED_SIZE); 
        button.addActionListener((ActionEvent e) -> {
            onSelect.accept(category);
            dispose(); 
        });
        return button;
    }
    @Override
    public void setVisible(boolean b) {
        if (b) {
            SwingUtilities.invokeLater(() -> super.setVisible(true));
        } else {
            SwingUtilities.invokeLater(() -> super.setVisible(false));
        }
    }
}