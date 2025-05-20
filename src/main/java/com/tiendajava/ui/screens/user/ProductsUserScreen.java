package com.tiendajava.ui.screens.user;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.components.ProductItemCard;
import com.tiendajava.ui.components.SearchBar;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class ProductsUserScreen extends JPanel {

    private final ProductService productService = new ProductService();
    private final JPanel productsPanel = new JPanel(new GridLayout(0, 1, 15, 15)); // Espacio entre productos
    private final SearchBar searchBar;

    public ProductsUserScreen(MainUI parent) {
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20)); // padding general

        // 🔹 Top Panel (Titulo + Barra de Búsqueda)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(UITheme.getPrimaryColor());
        
        JLabel title = new JLabel("Our Products", AppIcons.SETTINGS_ICON, SwingConstants.LEFT);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());

        searchBar = new SearchBar(e -> searchProducts(productsPanel));

        topPanel.add(title);
        topPanel.add(searchBar);

        add(topPanel, BorderLayout.NORTH);

        // 🔹 Productos Panel
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        // 🔹 Cargar productos
        loadProducts( productsPanel);
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
        printProducts(products, productsPanel);
    }

    private void printProducts(List<Product> products, JPanel productsPanel) {    
        productsPanel.removeAll(); // Limpiar el panel de productos antes de agregar los nuevos      
        for (Product product : products) {
            productsPanel.add(new ProductItemCard(
                product,
                () -> {},
                () -> {},
                () -> addToCart(product)
            ));

        }
        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private void addToCart(Product product) {
        NotificationHandler.info("Added to cart: " + product.getName());
    }

    private void searchProducts( JPanel productsPanel) {
        String query = searchBar.getText();
        ApiResponse<List<Product>> response = productService.getProductsByName(query);
        if (response.isSuccess()) {
            productsPanel.removeAll(); // Limpiar el panel de productos antes de agregar los nuevos
            List<Product> products = response.getData();
            printProducts(products, productsPanel);
        } else {
            NotificationHandler.error("Failed to search products: " + response.getMessage());
        }
    }
}
