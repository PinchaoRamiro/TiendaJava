package com.tiendajava.ui.screens.user.Cart;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.BorderFactory;
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
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class CartScreen extends JPanel {

    private final CartService cartService;
    private final JPanel itemsPanel;
    private final JLabel totalLabel;

    public CartScreen(MainUI parent, Cart cart) {
        this.cartService = new CartService(cart);
        this.itemsPanel = new JPanel();
        this.totalLabel = new JLabel();

        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20)); // Padding general

        // Título
        JLabel title = new JLabel("🛒 Your Cart");
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setAlignmentX(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(title, BorderLayout.NORTH);

        // Contenedor de items
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(UITheme.getPrimaryColor());

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(com.tiendajava.ui.utils.UIUtils.createDarkScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior (total + acciones)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(UITheme.getPrimaryColor());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        totalLabel.setFont(Fonts.SUBTITLE_FONT);
        totalLabel.setForeground(UITheme.getSuccessColor());
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        JButton clearBtn = ButtonFactory.createDangerButton("Clear Cart", null, () -> {
            cartService.clearCart();
            refresh();
        });

        bottomPanel.add(clearBtn, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        refresh();
    }

    private void refresh() {
        itemsPanel.removeAll();

        List<Product> products = cartService.getCartItems();

        if (products.isEmpty()) {
            JLabel empty = new JLabel("Your cart is empty.", SwingConstants.CENTER);
            empty.setFont(Fonts.NORMAL_FONT);
            empty.setForeground(UITheme.getTextColor());
            itemsPanel.add(empty);
        } else {
            for (Product product : products) {
                itemsPanel.add(createCartItem(product));
            }
        }

        updateTotal();
        revalidate();
        repaint();
    }

    private JPanel createCartItem(Product product) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.getSecondaryColor());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setFont(Fonts.BOLD_NFONT);
        nameLabel.setForeground(UITheme.getTextColor());

        JLabel priceLabel = new JLabel("Unit: $" + product.getPrice() + " | Subtotal: $" + product.getPrice().multiply(BigDecimal.valueOf(product.getStock())));
        priceLabel.setFont(Fonts.SMALL_FONT);
        priceLabel.setForeground(UITheme.getTextColor());

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setBackground(UITheme.getSecondaryColor());
        info.add(nameLabel);
        info.add(priceLabel);

        panel.add(info, BorderLayout.CENTER);

        // Acciones
        JPanel actions = new JPanel();
        actions.setBackground(UITheme.getSecondaryColor());

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(product.getStock(), 1, 999, 1);
        JSpinner qtySpinner = new JSpinner(spinnerModel);
        qtySpinner.setPreferredSize(new Dimension(60, 25));

        JButton updateBtn = ButtonFactory.createSecondaryButton("Update", null, () -> {
            int qty = (Integer) qtySpinner.getValue();
            cartService.updateQuantity(product.getProduct_id(), qty);
            refresh();
        });

        JButton removeBtn = ButtonFactory.createDangerButton("Remove", null, () -> {
            cartService.removeFromCart(product.getProduct_id());
            refresh();
        });

        actions.add(new JLabel("Qty:"));
        actions.add(qtySpinner);
        actions.add(updateBtn);
        actions.add(removeBtn);

        panel.add(actions, BorderLayout.EAST);

        return panel;
    }

    private void updateTotal() {
        BigDecimal total = cartService.getTotal();
        totalLabel.setText("Total: $" + total);
    }
}
