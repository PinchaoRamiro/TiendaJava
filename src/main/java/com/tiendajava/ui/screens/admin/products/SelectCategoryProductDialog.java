package com.tiendajava.ui.screens.admin.products;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout; // Importar Dimension
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
import javax.swing.SwingUtilities; // Importar SwingUtilities

import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class SelectCategoryProductDialog extends JDialog {

    // Constantes para el diseño
    private static final int DIALOG_WIDTH = 600;
    private static final int DIALOG_HEIGHT = 300; // Alto ligeramente aumentado
    private static final int PADDING = 15; // Padding general
    private static final int TITLE_BOTTOM_PADDING = 10;
    private static final int BUTTON_PANEL_H_GAP = 15; // Espacio horizontal entre botones
    private static final int BUTTON_PANEL_V_GAP = 15; // Espacio vertical entre botones
    private static final Dimension BUTTON_PREFERRED_SIZE = new Dimension(120, 50); // Tamaño preferido para los botones

    public SelectCategoryProductDialog(JFrame parent, Consumer<String> onCategorySelected) {
        super(parent, "Select Category", true); // true para modal

        // Configuración básica del diálogo
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(parent); // Centrar respecto al padre
        setResizable(false); // Generalmente, los diálogos modales no se deberían poder redimensionar
        getContentPane().setBackground(UITheme.getPrimaryColor());
        setLayout(new BorderLayout(PADDING, PADDING)); // Espacio entre regiones del BorderLayout

        // --- Panel del título ---
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Para centrar el JLabel
        titlePanel.setBackground(UITheme.getPrimaryColor());
        JLabel title = new JLabel("Select a Category to Create Product", AppIcons.BOX_ICON, SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, TITLE_BOTTOM_PADDING, PADDING)); // Padding del título
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        // --- Panel de botones de categoría ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_PANEL_H_GAP, BUTTON_PANEL_V_GAP));
        buttonPanel.setBackground(UITheme.getPrimaryColor());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, PADDING, PADDING, PADDING)); // Padding inferior del panel

        JButton clothingBtn = createCategoryButton("Clothing", AppIcons.BOX_ICON, onCategorySelected); // Asumiendo que tienes iconos específicos
        JButton electronicsBtn = createCategoryButton("Electronics", AppIcons.BOX_ICON, onCategorySelected);
        JButton furnitureBtn = createCategoryButton("Furniture", AppIcons.BOX_ICON, onCategorySelected);

        buttonPanel.add(clothingBtn);
        buttonPanel.add(electronicsBtn);
        buttonPanel.add(furnitureBtn);

        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createCategoryButton(String category, ImageIcon icon, Consumer<String> onSelect) {
        JButton button = new JButton(category, icon);
        button.setFocusPainted(false); // Quitar el borde de enfoque
        button.setFont(Fonts.NORMAL_FONT);
        button.setForeground(UITheme.getTextColor());
        button.setBackground(UITheme.getPrimaryButtonColor());
        button.setPreferredSize(BUTTON_PREFERRED_SIZE); // Establecer tamaño preferido
        button.addActionListener((ActionEvent e) -> {
            onSelect.accept(category);
            dispose(); // Cerrar el diálogo después de la selección
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