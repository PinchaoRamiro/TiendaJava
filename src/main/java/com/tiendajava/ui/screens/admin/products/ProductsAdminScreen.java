package com.tiendajava.ui.screens.admin.products;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.ProductItemCard;
import com.tiendajava.ui.components.SearchBar;
import com.tiendajava.ui.components.dialogs.DeleteConfirmDialog;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class ProductsAdminScreen extends JPanel {

    private final MainUI parent;
    private final ProductService productService = new ProductService();
    private final JPanel productsPanel = new JPanel(new GridLayout(0, 1, 15, 15)); 
    private final SearchBar searchBar;

    public ProductsAdminScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        productsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Título ---
        JLabel title = new JLabel("Manage Products", new ImageIcon(getClass().getResource("/icons/box.png")), SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(UIUtils.getDefaultPadding());
        add(title, BorderLayout.NORTH);

        // --- Barra superior: Botón + Búsqueda ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(UITheme.getPrimaryColor());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JButton createProductBtn = ButtonFactory.createPrimaryButton("Add Product", UIUtils.LoadIcon("/icons/user-add.png"), this::createProduct);
        topPanel.add(createProductBtn);

        searchBar = new SearchBar(e -> searchProducts());
        topPanel.add(searchBar);

        add(topPanel, BorderLayout.NORTH);

        // --- Productos ---
        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());
        add(scrollPane, BorderLayout.CENTER);

        loadProducts();
    }
    private void loadProducts() {
        productsPanel.removeAll();
        ApiResponse<List<Product>> response = productService.getAllProducts();
        List<Product> products = response.isSuccess() ? response.getData() : null;
        printProducts(products);
        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private void printProducts(List<Product> products) {
        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                productsPanel.add(new ProductItemCard(
                    product,
                    () -> editProduct(product),
                    () -> deleteProduct(product.getProduct_id(), product.getName()),
                    () -> {}
                ));
            }
        } else {
            JLabel noData = new JLabel("No products available", SwingConstants.CENTER);
            noData.setFont(Fonts.NORMAL_FONT);
            noData.setForeground(UITheme.getTextColor());
            productsPanel.add(noData);
        }

        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private void createProduct() {
        CreateProductDialog dialog = new CreateProductDialog(this::loadProducts);
        dialog.setVisible(true);
    }

    private void editProduct(Product product) {
        EditProductDialog dialog = new EditProductDialog(product, this::loadProducts);
        dialog.setVisible(true);
    }

    private void deleteProduct( int productId, String productName) {
        new DeleteConfirmDialog(productName, () -> {
            ApiResponse<String> response = productService.deleteProduct(productId);
            if (response.isSuccess()) {
                NotificationHandler.success("Product deleted successfully!");
                loadProducts();
            } else {
                NotificationHandler.error("Failed to delete product: " + response.getMessage());
            }
            loadProducts(); 
        }).setVisible(true);
    }

    private void searchProducts() {
        productsPanel.removeAll(); // Limpiar la lista de productos antes de agregar los nuevos
        String keyword = searchBar.getText();
        ApiResponse<List<Product>> response = productService.getProductsByName(keyword);
        List<Product> products = response.isSuccess() ? response.getData() : null;
        if(products == null){
            NotificationHandler.error("No products found with the name: " + keyword);
             loadProducts();
             return;
        }
        printProducts(products); // Mostrar los productos filtrados
        System.out.println("Searching: " + keyword);
    }
    

    // @Override
    // public MainUI getParent() {
    //     return parent;
    // }
}