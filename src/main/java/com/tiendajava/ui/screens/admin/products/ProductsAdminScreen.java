package com.tiendajava.ui.screens.admin.products;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import com.tiendajava.ui.components.dialogs.DeleteConfirmDialog;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class ProductsAdminScreen extends JPanel {

    private final MainUI parent;
    private final ProductService productService = new ProductService();
    private final JPanel productsPanel = new JPanel(new GridLayout(0, 2, 15, 15)); 

    public ProductsAdminScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        productsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel title = new JLabel("Manage Products", new ImageIcon(getClass().getResource("/icons/box.png")), SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(UIUtils.getDefaultPadding());

        add(title, BorderLayout.NORTH);

        JButton createProductBtn = ButtonFactory.createPrimaryButton("Add Product", new ImageIcon(getClass().getResource("/icons/user-add.png")), this::createProduct);
        JPanel topPanel = new JPanel();
        topPanel.setBackground(UITheme.getPrimaryColor());
        topPanel.add(createProductBtn);

        add(topPanel, BorderLayout.PAGE_START);

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
        if (products != null && !products.isEmpty()) {
            for (Product product : products) {
                productsPanel.add(createProductCard(product));
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

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.getSecondaryColor());
        card.setBorder(UIUtils.getRoundedBorder());
        card.setPreferredSize(new Dimension(300, 150));

        JLabel nameLabel = new JLabel(product.getName() + " - $" + product.getPrice());
        nameLabel.setFont(Fonts.SUBTITLE_FONT);
        nameLabel.setForeground(UITheme.getTextColor());
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        card.add(nameLabel, BorderLayout.NORTH);

        JPanel actionsPanel = new JPanel();
        actionsPanel.setBackground(UITheme.getSecondaryColor());

        JButton editBtn = ButtonFactory.createSecondaryButton("Edit", null, () -> editProduct(product));
        JButton deleteBtn = ButtonFactory.createDangerButton("Delete", null, () -> deleteProduct(product.getProduct_id(), product.getName()));

        actionsPanel.add(editBtn);
        actionsPanel.add(deleteBtn);

        card.add(actionsPanel, BorderLayout.SOUTH);

        return card;
    }

    private void createProduct() {
        CreateProductDialog dialog = new CreateProductDialog(this::loadProducts);
        dialog.setVisible(true);
    }

    private void editProduct(Product product) {
        EditProductDialog dialog = new EditProductDialog(product, this::loadProducts);
        dialog.setVisible(true);
    }

    private void deleteProduct(int productId, String productName) {
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

    // @Override
    // public MainUI getParent() {
    //     return parent;
    // }
}