package com.tiendajava.ui.screens.admin.products;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout; 
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities; 

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.components.ProductItemCard;
import com.tiendajava.ui.components.SearchBar;
import com.tiendajava.ui.components.dialogs.DeleteConfirmDialog;
import com.tiendajava.ui.screens.admin.products.creaate_by_categories.CreateClothingProductDialog;
import com.tiendajava.ui.screens.admin.products.creaate_by_categories.CreateElectronicsProductDialog;
import com.tiendajava.ui.screens.admin.products.creaate_by_categories.CreateFurnitureProductDialog;
import com.tiendajava.ui.screens.user.products.InfoProductDialog;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;


public class ProductsAdminScreen extends JPanel {

    private final MainUI parent;
    private final ProductService productService = new ProductService();
    private final JPanel productsPanel;
    private final SearchBar searchBar;
    private List<Product> products = null;

    private static final int TOP_PANEL_VERTICAL_GAP = 15;
    private static final int TOP_PANEL_HORIZONTAL_GAP = 20;
    private static final int PRODUCTS_PANEL_VERTICAL_PADDING = 10;
    private static final int PRODUCTS_PANEL_HORIZONTAL_PADDING = 20;
    private static final int CREATE_BUTTON_WIDTH = 170;
    private static final int CREATE_BUTTON_HEIGHT = 40;
    private static final int PRODUCT_CARD_H_GAP = 10;
    private static final int PRODUCT_CARD_V_GAP = 10; 
    private static final int GRID_COLS = 1; 
    public ProductsAdminScreen(MainUI parent) {
        this.parent = parent;
        this.searchBar = new SearchBar(e -> searchProducts());

        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UITheme.getPrimaryColor());
        topPanel.setBorder(BorderFactory.createEmptyBorder(TOP_PANEL_VERTICAL_GAP, TOP_PANEL_HORIZONTAL_GAP, PRODUCTS_PANEL_VERTICAL_PADDING, TOP_PANEL_HORIZONTAL_GAP)); // Ajustado el padding

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        titlePanel.setBackground(UITheme.getPrimaryColor());
        JLabel title = new JLabel("Manage Products", AppIcons.BOX_ICON, SwingConstants.CENTER); 
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); 
        titlePanel.add(title); 
        topPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel actionPanel = new JPanel(new BorderLayout(15, 0));
        actionPanel.setBackground(UITheme.getPrimaryColor());

        JButton createProductBtn = ButtonFactory.createPrimaryButton("Create Product", AppIcons.ADD_ICON, this::createProduct);
        createProductBtn.setPreferredSize(new Dimension(CREATE_BUTTON_WIDTH, CREATE_BUTTON_HEIGHT));
        actionPanel.add(createProductBtn, BorderLayout.WEST);
        actionPanel.add(searchBar, BorderLayout.CENTER);

        topPanel.add(actionPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        productsPanel = new JPanel(new GridLayout(0, GRID_COLS, PRODUCT_CARD_H_GAP, PRODUCT_CARD_V_GAP));
        productsPanel.setBorder(BorderFactory.createEmptyBorder(PRODUCTS_PANEL_VERTICAL_PADDING, PRODUCTS_PANEL_HORIZONTAL_PADDING, PRODUCTS_PANEL_VERTICAL_PADDING, PRODUCTS_PANEL_HORIZONTAL_PADDING));
        productsPanel.setBackground(UITheme.getPrimaryColor()); 

        JScrollPane scrollPane = new JScrollPane(productsPanel);
        scrollPane.setBorder(null); 
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        getProductsDataBase();
        loadProducts();
    }

    private void loadProducts() {
        parent.showLoading("Loading products...");
        SwingUtilities.invokeLater(() -> {
            productsPanel.removeAll();
            if (products != null && !products.isEmpty()) {
                parent.hideLoading();
                printProducts(products);
            } else {
                JLabel noData = new JLabel("No products available", SwingConstants.CENTER);
                noData.setFont(Fonts.NORMAL_FONT);
                noData.setForeground(UITheme.getTextColor());
                JPanel noDataPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                noDataPanel.setBackground(UITheme.getPrimaryColor());
                noDataPanel.add(noData);
                productsPanel.add(noDataPanel); 
            }
            productsPanel.revalidate();
            productsPanel.repaint();
        });
    }

    private void printProducts(List<Product> productsToDisplay) {
        productsPanel.removeAll();
        if (productsToDisplay != null && !productsToDisplay.isEmpty()) {
            for (Product product : productsToDisplay) {
                productsPanel.add(new ProductItemCard(
                    product,
                    () -> editProduct(product),
                    () -> deleteProduct( product.getProduct_id(), product.getName()),
                    () -> onClickProduct(product),
                    () -> {} 
                ));
            }
        }
        productsPanel.revalidate(); 

        productsPanel.repaint(); 
    }

    private void getProductsDataBase() {
        new Thread(() -> {
            ApiResponse<List<Product>> response = productService.getAllProducts();
            SwingUtilities.invokeLater(() -> { 
                this.products = response.isSuccess() ? response.getData() : null;
                if (!response.isSuccess()) {
                    NotificationHandler.error("Failed to load products: " + response.getMessage());
                }
                loadProducts(); 
            });
        }).start();
    }

    private void createProduct() {
        new SelectCategoryProductDialog(parent, category -> {
            SwingUtilities.invokeLater(() -> { 
                switch (category) {
                    case "Electronics" -> new CreateElectronicsProductDialog("Electronics").setVisible(true); // Actualizar desde DB
                    case "Clothing" -> new CreateClothingProductDialog("Clothing").setVisible(true); // Actualizar desde DB
                    case "Furniture" -> new CreateFurnitureProductDialog("Furniture").setVisible(true); // Actualizar desde DB
                    default -> NotificationHandler.error("Invalid category selected.");
                }
            });
        }).setVisible(true);
    }

    private void editProduct(Product product) {
        SwingUtilities.invokeLater(() -> { 
            EditProductDialog dialog = new EditProductDialog(product, product.getCategory().getCategory_name(), this::getProductsDataBase); // Actualizar desde DB
            dialog.setVisible(true);
        });
    }

    private void deleteProduct( int productId, String productName) {
        new DeleteConfirmDialog(productName, () -> {
            new Thread(() -> {
                ApiResponse<String> response = productService.deleteProduct(productId);
                SwingUtilities.invokeLater(() -> { 
                    if (response.isSuccess()) {
                        NotificationHandler.success("Product deleted successfully!");
                        getProductsDataBase();
                    } else {
                        NotificationHandler.error("Failed to delete product: " + response.getMessage());
                    }
                });
            }).start();
        }).setVisible(true);
    }

    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Product added: " + product);
        printProducts(products);
    }

    private void searchProducts() {
        String keyword = searchBar.getText();
        if (keyword.trim().isEmpty()) {
            NotificationHandler.info("Showing all products.");
            getProductsDataBase(); 
            return;
        }
        
        new Thread(() -> {
            ApiResponse<List<Product>> response = productService.getProductsByName(keyword);
            System.out.println("Response: " + response);
            SwingUtilities.invokeLater(() -> {
                if (response.isSuccess() && response.getData() != null && !response.getData().isEmpty()) {
                    printProducts(response.getData());
                    NotificationHandler.info("Products found for: " + keyword);
                } else {
                    NotificationHandler.info("No products found with the name: " + keyword);
                    productsPanel.removeAll();
                    JLabel noData = new JLabel("No products found for \"" + keyword + "\"", SwingConstants.CENTER);
                    noData.setFont(Fonts.NORMAL_FONT);
                    noData.setForeground(UITheme.getTextColor());
                    JPanel noDataPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    noDataPanel.setBackground(UITheme.getPrimaryColor());
                    noDataPanel.add(noData);
                    productsPanel.add(noDataPanel);
                }
                productsPanel.revalidate();
                productsPanel.repaint();
            });
        }).start();
    }

    private void onClickProduct(Product product) {
        new InfoProductDialog( parent, product).setVisible(true);
    }

    public MainUI getParentPA() {
        return parent;
    }
}