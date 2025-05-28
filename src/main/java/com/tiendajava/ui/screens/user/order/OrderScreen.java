package com.tiendajava.ui.screens.user.order;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.tiendajava.model.Cart;
import com.tiendajava.model.Product;
import com.tiendajava.model.orders.Order;
import com.tiendajava.service.OrderService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class OrderScreen extends JPanel {

    private final JTextField addressField = new JTextField(30);
    private final JComboBox<String> paymentMethodCombo = new JComboBox<>(new String[]{"Credit Card", "PayPal", "Cash on Delivery"});
    private final JTextArea cartItemsArea = new JTextArea(10, 50);
    private final JLabel totalLabel = new JLabel();
    private final OrderService orderService = new OrderService();
    private Cart cart;
    private final MainUI parent;

    public OrderScreen(MainUI parent, Cart cart) {
        this.cart = cart;
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());

        JLabel title = new JLabel("Place Your Order", SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.getPrimaryColor());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        gbc.gridy = 0;
        formPanel.add(createLabel("Shipping Address:"), gbc);
        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(createLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        formPanel.add(paymentMethodCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(createLabel("Cart Items:"), gbc);
        gbc.gridx = 1;
        cartItemsArea.setEditable(false);
        cartItemsArea.setBackground(UITheme.getSecondaryColor());
        cartItemsArea.setForeground(UITheme.getTextColor());
        JScrollPane scrollPane = new JScrollPane(cartItemsArea);
        formPanel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(createLabel("Total:"), gbc);
        gbc.gridx = 1;
        totalLabel.setForeground(UITheme.getSuccessColor());
        totalLabel.setFont(Fonts.SUBTITLE_FONT);
        formPanel.add(totalLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // action buttons
        JPanel buttonPanel = new JPanel();

        JButton confirmOrderBtn = ButtonFactory.createPrimaryButton("Confirm Order", null, this::submitOrder);
        buttonPanel.add(confirmOrderBtn);

        // Go to My Orders screen button
        JButton myOrdersBtn = ButtonFactory.createSecondaryButton("My Orders", null, () -> parent.showScreen("my-orders"));
        buttonPanel.add(myOrdersBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        refreshCartDetails();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(UITheme.getTextColor());
        label.setFont(Fonts.NORMAL_FONT);
        return label;
    }

    private void refreshCartDetails() {
        cart = parent.getCart();
        StringBuilder sb = new StringBuilder();
        for (Product p : cart.getItems()) {
            sb.append(p.getName())
              .append(" x")
              .append(p.getStock())
              .append(" - $")
              .append(p.getPrice().multiply(BigDecimal.valueOf(p.getStock())))
              .append("\n");
        }
        cartItemsArea.setText(sb.toString());
        totalLabel.setText("$" + cart.getTotalSubtotal().toString());
    }

    private void submitOrder() {
        String address = addressField.getText().trim();
        String paymentMethod = (String) paymentMethodCombo.getSelectedItem();
        if (address.isEmpty()) {
            NotificationHandler.warning("Address is required.");
            return;
        }

        cart = parent.getCart();
        if (cart.isEmpty()) {
            NotificationHandler.warning("Your cart is empty.");
            return;
        }

        Order order = new Order();
        order.setShipping_address(address);
        order.setPayment_method(paymentMethod);
        order.setOrderItems(cart.toOrderItems());
        order.setTotal_amount(cart.getTotalSubtotal());

        var resp = orderService.createOrder(order);
        if (resp.isSuccess()) {
            NotificationHandler.success("Order placed successfully!");
            cart.clearCart();
            refreshCartDetails();
            addressField.setText("");
        } else {
            NotificationHandler.error("Failed to place order: " + resp.getMessage());
        }
    }
}
