package com.tiendajava.ui.components;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tiendajava.model.Product;
import com.tiendajava.model.Session;
import com.tiendajava.service.CategoryService;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class ProductItemCard extends JPanel {

    private final CategoryService categoryService = new CategoryService();


    public ProductItemCard(Product product, Runnable onEdit, Runnable onDelete, Runnable onAddToCart) {

        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.getSecondaryColor());
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, UITheme.getBorderCardsColor()));
        setPreferredSize(new Dimension(400, 120));

        // Get a padding for the card
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setCursor(Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));


        // Imagen
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(100, 100));

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            imageLabel.setIcon(ChargueImage(product)); // Cargar la imagen desde la URL
        } else {
            imageLabel.setIcon( AppIcons.PRODUCT_ICON); // Icono por defecto
        }

        add(imageLabel, BorderLayout.WEST);

        // Información del producto
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setBackground(UITheme.getSecondaryColor());

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(Fonts.SUBTITLE_FONT);
        nameLabel.setForeground(UITheme.getTextColor());

        JLabel priceLabel = new JLabel("$" + product.getPrice());
        priceLabel.setFont(Fonts.NORMAL_FONT);
        priceLabel.setForeground(UITheme.getSuccessColor());

        JLabel stockLabel = new JLabel("Stock: " + product.getStock());
        stockLabel.setFont(Fonts.SMALL_FONT);
        stockLabel.setForeground(UITheme.getTextColor());

        // descripción
        JLabel descriptionLabel = new JLabel(product.getDescription());
        descriptionLabel.setFont(Fonts.SMALL_FONT);
        descriptionLabel.setForeground(UITheme.getTextColor());

        JLabel categoryLabel = new JLabel("Category: " + product.getCategory().getCategory_name());
        categoryLabel.setFont(Fonts.SMALL_FONT);
        categoryLabel.setForeground(UITheme.getTextColor());

        infoPanel.add(nameLabel);
        infoPanel.add(categoryLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(stockLabel);
        infoPanel.add(descriptionLabel);

        add(infoPanel, BorderLayout.CENTER);

        // Botones de acción
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionPanel.setBackground(UITheme.getSecondaryColor());

        if (Session.getInstance().getRole().equalsIgnoreCase("admin")) {
            JLabel editBtn = ButtonFactory.createIconButton(AppIcons.EDIT_ICON, onEdit);
            editBtn.setForeground(UITheme.getButtonColor());

            editBtn.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
            ImageIcon deleteIcon = AppIcons.DELETE_ICON;
            ImageIcon iconDanger = UIUtils.tintImage(deleteIcon, UITheme.getDangerColor());
            JLabel deleteBtn = ButtonFactory.createIconButton( iconDanger, onDelete);
            deleteBtn.setForeground(UITheme.getDangerColor());
            deleteBtn.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

            actionPanel.add(editBtn);
            actionPanel.add(deleteBtn);
        } else {
            JLabel addToCartBtn = ButtonFactory.createIconButton(AppIcons.CART_ADD_ICON, onAddToCart);
            actionPanel.add(addToCartBtn);
        }

        add(actionPanel, BorderLayout.EAST);
    }


    private ImageIcon ChargueImage(Product product) {
       // 1) Construye la URL de la imagen 
        String imageUrl = "http://localhost:5000" + product.getImage();

        System.out.println("Image URL: " + imageUrl); // Debugging line
        
        // 2) Carga el icono (con un placeholder si falla)
        ImageIcon rawIcon;
        try {
            rawIcon = new ImageIcon(URI.create(imageUrl).toURL());
        } catch (MalformedURLException e) {
            System.out.println("Error loading image: " + e.getMessage());
            rawIcon = AppIcons.PRODUCT_ICON;
        }
        // 3) Escala el icono a 100×100 px
        Image scaled = rawIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaled);

        return icon;

    }
}
