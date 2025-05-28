package com.tiendajava.ui.screens.user.cart;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tiendajava.model.Cart;
import com.tiendajava.model.Product;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class AddCartDialog extends JDialog {

    private final JTextField quantityField = new JTextField(10);
    private final Cart cart;
    private final Product product;

    public AddCartDialog(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;

        setTitle("Add to Cart");
        setModal(true);
        setSize(300, 200);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.getPrimaryColor());

        JPanel panel = new JPanel();
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Enter quantity for: " + product.getName());
        title.setFont(Fonts.SUBTITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        quantityField.setMaximumSize(new Dimension(100, 30));
        quantityField.setFont(Fonts.NORMAL_FONT);

        JButton addButton = ButtonFactory.createPrimaryButton("Add to Cart", null, this::addToCart);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(addButton);

        add(panel);
    }

    private void addToCart() {
        try {
            int qty = Integer.parseInt(quantityField.getText().trim());
            if (qty <= 0) throw new NumberFormatException();
            if (qty > product.getStock()) {
                NotificationHandler.warning("Quantity exceeds available stock.");
                return;
            }
            cart.addItem(product, qty);
            NotificationHandler.success("Product added to cart!");
            dispose();
        } catch (NumberFormatException e) {
            NotificationHandler.warning("Please enter a valid quantity.");
        }
    }
}
