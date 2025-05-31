package com.tiendajava.ui.screens.user.products;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
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
import com.tiendajava.ui.screens.user.cart.AddCartDialog;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class ProductsUserScreen extends JPanel {

    private final ProductService productService = new ProductService();
    private final JPanel productsPanel = new JPanel(new GridLayout(0, 1, 15, 15)); 
    private final JComboBox<String> filterOptions = new JComboBox<>(new String[] {"All", "Clothing", "Electronics", "Furniture"});
    private final SearchBar searchBar;
    private final MainUI parent;
    private List<Product> products;

    public ProductsUserScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20)); 

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(UITheme.getPrimaryColor());
        
        JLabel title = new JLabel("Our Products", AppIcons.SETTINGS_ICON, SwingConstants.LEFT);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());

        searchBar = new SearchBar(e -> searchProducts(productsPanel));
        searchBar.setPreferredSize(new java.awt.Dimension(400, 40));

        filterOptions.setForeground(UITheme.getTextColor());
        filterOptions.setBackground(UITheme.getSecondaryColor());
        filterOptions.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        filterOptions.addActionListener(e -> {
            filterProducts();
        });

        topPanel.add(title);
        topPanel.add(searchBar);
        topPanel.add(filterOptions);

        add(topPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        loadProducts( productsPanel);
    }

    private void loadProducts(JPanel productsPanel) {
        ApiResponse<List<Product>> response = productService.getUserProducts();
        if (!response.isSuccess()) {
            NotificationHandler.error("Failed to load products: " + response.getMessage());
            return;
        }
        products = response.getData();
        if (products == null || products.isEmpty()) {
            NotificationHandler.warning("No products available.");
            return;
        }
        printProducts(products, productsPanel);
    }

    private void printProducts(List<Product> products, JPanel productsPanel) {    
        productsPanel.removeAll();    
        for (Product product : products) {
            productsPanel.add(new ProductItemCard(
                product,
                () -> {},
                () -> {},
                () -> onClickProduct(product),
                () -> addToCart(product)
            ));
        }
        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private void addToCart(Product product) {
        try {
            new AddCartDialog(parent.getCart(), product).setVisible(true);
        } catch (NumberFormatException e) {
            NotificationHandler.warning("Please enter a valid quantity.");
        } catch (IllegalArgumentException e) {
            NotificationHandler.error("Invalid product: " + e.getMessage());
        } catch (Exception e) {
            NotificationHandler.error("An error occurred while adding the product to the cart" );
            System.err.println("Error adding product to cart: " + e.getMessage());
        }
    }

    private void searchProducts( JPanel productsPanel) {
        String query = searchBar.getText();

        if (query == null || query.trim().isEmpty()) {
            NotificationHandler.warning("Please enter a search term.");
            return;
        }
        ApiResponse<List<Product>> response = productService.getProductsByName(query);
        if (response.isSuccess()) {
            productsPanel.removeAll(); 
            products = response.getData();
            printProducts(products, productsPanel);
        } else if(response.getMessage() != null) {
            NotificationHandler.warning("Product not found" + response.getMessage());
        }else {
            NotificationHandler.error("Failed to search products");
        }
    }

    private void filterProducts() {
        String selectedOption = (String) filterOptions.getSelectedItem();

        if (selectedOption.equals("All")) {
            printProducts(products, productsPanel);
        }

        else {
            List<Product> filteredProducts = products.stream()
                .filter(product -> product.getCategory().getCategory_name().equals(selectedOption))
                .collect(Collectors.toList());
            printProducts(filteredProducts, productsPanel);
        }


    }
    
    private void onClickProduct(Product product) {
        new InfoProductDialog(parent, product).setVisible(true);
    }
}
