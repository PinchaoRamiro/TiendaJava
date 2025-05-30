package com.tiendajava.ui.screens.user.cart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import com.tiendajava.model.Cart;
import com.tiendajava.model.Product;
import com.tiendajava.service.CartService;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.components.dialogs.ConfirmationDialog;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class FunctionalCartScreen extends JPanel {
    
    private final CartService cartService;
    private final JPanel itemsPanel;
    private final JLabel totalLabel;
    private final MainUI parent;
    
    public FunctionalCartScreen(MainUI parent, Cart cart) {
        this.parent = parent;
        this.cartService = new CartService(cart);
        this.itemsPanel = new JPanel();
        this.totalLabel = new JLabel("Total: $0.00", SwingConstants.RIGHT);
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        applyTheme();
        setupTitle();
        setupItemsPanel();
        setupBottomPanel();
        
        refreshItems();
    }
    
    private void applyTheme() {
        setBackground(UITheme.getPrimaryColor());
        itemsPanel.setBackground(UITheme.getPrimaryColor());
    }
    
    private void setupTitle() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UITheme.getPrimaryColor());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel title = new JLabel("🛒 Shopping Cart", SwingConstants.LEFT);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        titlePanel.add(title, BorderLayout.WEST);
        
        add(titlePanel, BorderLayout.NORTH);
    }
    
    private void setupItemsPanel() {
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.getSecondaryColor()));
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        bottomPanel.setBackground(UITheme.getPrimaryColor());

        // Panel de total
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(UITheme.getPrimaryColor());
        
        JLabel totalText = new JLabel("TOTAL TO PAY: ");
        totalText.setFont(Fonts.SUBTITLE_FONT);
        totalText.setForeground(UITheme.getTextColor());
        totalPanel.add(totalText);
        
        totalLabel.setFont(Fonts.TITLE_FONT);
        totalLabel.setForeground(UITheme.getSuccessColor());
        totalPanel.add(totalLabel);
        
        bottomPanel.add(totalPanel, BorderLayout.WEST);
        
        // Panel de botones
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonsPanel.setBackground(UITheme.getPrimaryColor());
        
        JButton continueButton = ButtonFactory.createSecondaryButton("Continue Shopping", null, 
            () -> parent.showScreen("products-user"));
        
        JButton clearButton = ButtonFactory.createDangerButton("Clear Cart", null, 
            () -> clearCart());
        
        JButton checkoutButton = ButtonFactory.createPrimaryButton("Checkout", null, 
            () -> checkout());
        
        buttonsPanel.add(continueButton);
        buttonsPanel.add(clearButton);
        buttonsPanel.add(checkoutButton);
        
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void clearCart() {
        new ConfirmationDialog(
            "Clear Cart Confirmation",
            UITheme.getDangerColor(),
            "Are you sure you want to clear the cart?",
            () -> {
                cartService.clearCart();
                refreshItems();
            }
        ).setVisible(true);
    }
    
    private void checkout() {
        if (cartService.getCartItems().isEmpty()) {
            NotificationHandler.warning("Your cart is empty.");
        } else {
            parent.showScreen("payment");
        }
    }
    
    private void refreshItems() {
        itemsPanel.removeAll();
        
        List<Product> products = cartService.getCartItems();
        if (products.isEmpty()) {
            addEmptyCartMessage();
        } else {
            for (Product product : products) {
                addCartItem(product);
            }
        }
        
        updateTotal();
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }
    
    private void addEmptyCartMessage() {
        JPanel emptyPanel = new JPanel(new BorderLayout());
        emptyPanel.setBackground(UITheme.getPrimaryColor());
        
        JLabel empty = new JLabel("Your cart is empty", SwingConstants.CENTER);
        empty.setFont(Fonts.TITLE_FONT);
        empty.setForeground(UITheme.getTextColor());
        
        JLabel suggestion = new JLabel("Add products to get started!", SwingConstants.CENTER);
        suggestion.setFont(Fonts.NORMAL_FONT);
        suggestion.setForeground(UITheme.getTextColor());
        
        JPanel messagePanel = new JPanel(new GridLayout(2, 1));
        messagePanel.setBackground(UITheme.getPrimaryColor());
        messagePanel.add(empty);
        messagePanel.add(suggestion);
        
        emptyPanel.add(messagePanel, BorderLayout.CENTER);
        itemsPanel.add(emptyPanel);
    }
    
    private void addCartItem(Product product) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.getSecondaryColor(), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        itemPanel.setBackground(UITheme.getSecondaryColor().brighter());
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Panel de imagen (simplificado)
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(80, 80));
        imagePanel.setBackground(UITheme.getSecondaryColor());
        
        JLabel initialLabel = new JLabel(
        product.getName().substring(0, 1), SwingConstants.CENTER);
        initialLabel.setFont(Fonts.TITLE_FONT.deriveFont(44f));
        initialLabel.setForeground(Color.WHITE);
        imagePanel.add(initialLabel);
        
        itemPanel.add(imagePanel, BorderLayout.WEST);
        
        // Información del producto
        itemPanel.add(createProductInfoPanel(product), BorderLayout.CENTER);
        
        // Acciones
        itemPanel.add(createProductActionsPanel(product), BorderLayout.EAST);
        
        itemsPanel.add(itemPanel);
        itemsPanel.add(Box.createVerticalStrut(10));
    }
    
    private JPanel createProductInfoPanel(Product product) {
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        infoPanel.setBackground(UITheme.getSecondaryColor().brighter());
        
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(Fonts.BOLD_NFONT);
        nameLabel.setForeground(UITheme.getTextColor());
        
        JLabel priceLabel = new JLabel("Unit price: $" + product.getPrice());
        priceLabel.setFont(Fonts.NORMAL_FONT);
        priceLabel.setForeground(UITheme.getTextColor());
        
        BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(product.getStock()));
        JLabel subtotalLabel = new JLabel("Subtotal: $" + subtotal);
        subtotalLabel.setFont(Fonts.BOLD_NFONT);
        subtotalLabel.setForeground(UITheme.getTextColor());
        
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(subtotalLabel);
        
        return infoPanel;
    }
    
    private JPanel createProductActionsPanel(Product product) {
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBackground(UITheme.getSecondaryColor().brighter());
        
        // Panel de cantidad
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        qtyPanel.setBackground(UITheme.getSecondaryColor().brighter());
        
        qtyPanel.add(new JLabel("Quantity:"));

        int maxStock = getMaxStock(product);
        
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(
            product.getStock(), 1, maxStock, 1
        );
        JSpinner qtySpinner = new JSpinner(spinnerModel);
        qtySpinner.setPreferredSize(new Dimension(60, 25));
        qtyPanel.add(qtySpinner);
        
        actionsPanel.add(qtyPanel);
        actionsPanel.add(Box.createVerticalStrut(10));
        
        // Panel de botones
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        btnPanel.setBackground(UITheme.getSecondaryColor().brighter());
        
        JButton updateButton = createActionButton("Update", UITheme.getSecondaryColor(), e -> {
            int qty = (Integer) qtySpinner.getValue();
            cartService.updateQuantity(product.getProduct_id(), qty);
            refreshItems();
        });
        
        JButton removeButton = createActionButton("Remove", UITheme.getDangerColor(), e -> {
            new ConfirmationDialog(
                "Remove Item Confirmation",
                UITheme.getDangerColor(),
                "Are you sure you want to remove this item from the cart?",
                () -> {
                    cartService.removeFromCart(product.getProduct_id());
                    refreshItems();
                    NotificationHandler.success("Item removed from cart.");
                }
            ).setVisible(true);
        });
        
        btnPanel.add(updateButton);
        btnPanel.add(removeButton);
        
        actionsPanel.add(btnPanel);
        return actionsPanel;
    }
    
    private JButton createActionButton(String text, Color bgColor, ActionListener action) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(Fonts.SMALL_FONT);
        button.setFocusPainted(false);
        button.addActionListener(action);
        button.setCursor(Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        button.setFocusable(false);
        return button;
    }
    
    private void updateTotal() {
        totalLabel.setText("$" + cartService.getTotal());
    }

    private int getMaxStock(Product product) {
        ProductService productService = new ProductService();
        int maxStock = productService.getStockByProductId(product.getProduct_id());
        if (maxStock > 0) {
            return maxStock;
        } else {
            NotificationHandler.warning("No stock available for " + product.getName());
            return 0; // Default to 1 if no stock available
        }
    }
}