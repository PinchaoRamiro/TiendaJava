package com.tiendajava.ui.screens.user;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon; 
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;

public class ProductsUserScreen extends JPanel {

    private final ProductService productService = new ProductService();

    public ProductsUserScreen(MainUI parent) {
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());

        // Título
        JLabel title = new JLabel("Our Products", new ImageIcon(getClass().getResource("/icons/config-product.png")), SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // Panel de productos
        JPanel productsPanel = new JPanel(new GridLayout(0, 3, 20, 20)); // 3 columnas
        productsPanel.setBackground(UITheme.getPrimaryColor());
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        add(scrollPane, BorderLayout.CENTER);

        loadProducts(productsPanel);
    }

    private void loadProducts(JPanel productsPanel) {
        ApiResponse<List<Product>> response = productService.getAllProducts();
        if (!response.isSuccess()) {
            NotificationHandler.error("Failed to load products: " + response.getMessage());
            return;
        }
        List<Product> products = response.getData();
        if (products == null || products.isEmpty()) {
            NotificationHandler.warning("No products available.");
            return;
        }

        for (Product product : products) {
            productsPanel.add(createProductCard(product));
        }
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.getSecondaryColor());
        card.setBorder(BorderFactory.createLineBorder(UITheme.getTertiaryColor(), 2));
        card.setPreferredSize(new Dimension(200, 250));

        // 1) Construye la URL de la imagen 
        String imageUrl = "http://localhost:5000" + product.getImage();

        System.out.println("Image URL: " + imageUrl); // Debugging line
        
        // 2) Carga el icono (con un placeholder si falla)
        ImageIcon rawIcon;
        try {
            rawIcon = new ImageIcon(URI.create(imageUrl).toURL());
        } catch (MalformedURLException e) {
            System.out.println("Error loading image: " + e.getMessage());
            rawIcon = new ImageIcon(getClass().getResource("/icons/product.png"));
        }
        // 3) Escala el icono a 100×100 px
        Image scaled = rawIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaled);

        // 4) Crea un JLabel centrado con la imagen
        JLabel imgLabel = new JLabel(icon, SwingConstants.CENTER);
        imgLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        card.add(imgLabel, BorderLayout.CENTER);

        // Nombre y precio
        JLabel nameLabel  = new JLabel(product.getName(),  SwingConstants.CENTER);
        nameLabel.setFont(Fonts.SUBTITLE_FONT);
        nameLabel.setForeground(UITheme.getTextColor());
        card.add(nameLabel, BorderLayout.NORTH);

        JLabel priceLabel = new JLabel("$" + product.getPrice(), SwingConstants.CENTER);
        priceLabel.setFont(Fonts.NORMAL_FONT);
        priceLabel.setForeground(UITheme.getSuccessColor());
        card.add(priceLabel, BorderLayout.SOUTH);

        return card;
    }

}
