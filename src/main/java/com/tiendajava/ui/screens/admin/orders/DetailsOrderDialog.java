package com.tiendajava.ui.screens.admin.orders;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.tiendajava.model.Product;
import com.tiendajava.model.User;
import com.tiendajava.model.orders.Order;
import com.tiendajava.model.orders.OrderItem;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class DetailsOrderDialog extends JDialog {

    private final Order order;
    private final User user;
    private final ProductService productService = new ProductService();

    public DetailsOrderDialog( Order order, User user) {
        this.order = order;
        this.user = user;
        setTitle("Order Details - ID: " + order.getOrder_id());
        setModal(true);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.getPrimaryColor());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UITheme.getPrimaryColor());
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        addOrderInfo(content);
        addUserInfo(content);
        addItemsInfo(content);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(UITheme.getPrimaryColor());
        add(scroll, BorderLayout.CENTER);
    }

    private void addOrderInfo(JPanel panel) {
        JLabel title = new JLabel("🧾 Order Summary");
        title.setFont(Fonts.SUBTITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        panel.add(title);

        panel.add(createLabel("Order ID: " + order.getOrder_id()));
        panel.add(createLabel("Status: " + order.getStatus()));
        panel.add(createLabel("Total: $" + order.getTotal_amount()));
        panel.add(createLabel("Payment Method: " + order.getPayment_method()));
        panel.add(createLabel("Shipping Address: " + order.getShipping_address()));
        panel.add(createLabel("Created At: " + order.getCreatedAt()));
        panel.add(Box.createVerticalStrut(15));
    }

    private void addUserInfo(JPanel panel) {
        JLabel title = new JLabel("👤 Customer Information");
        title.setFont(Fonts.SUBTITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        panel.add(title);

        if (user != null) {
            panel.add(createLabel("Name: " + user.getName() + " " + user.getLastName()));
            panel.add(createLabel("Email: " + user.getEmail()));
            panel.add(createLabel("Phone: " + user.getPhone()));
        } else {
            panel.add(createLabel("User not found."));
        }

        panel.add(Box.createVerticalStrut(15));
    }

    private void addItemsInfo(JPanel panel) {
        JLabel title = new JLabel("🛍 Purchased Items");
        title.setFont(Fonts.SUBTITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        panel.add(title);

        List<OrderItem> items = order.getOrderItems();
        if (items == null || items.isEmpty()) {
            panel.add(createLabel("No items in this order."));
            return;
        }

        for (OrderItem item : items) {
            Product product = productService.getProductById(item.getProduct_id()).getData();
            if (product != null) {
                panel.add(createLabel("- " + product.getName() + " | Qty: " + item.getQuantity() + " | Price: $" + item.getPrice()));
            } else {
                panel.add(createLabel("- Product ID " + item.getProduct_id() + " (details not found)"));
            }
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(UITheme.getTextColor());
        label.setFont(Fonts.NORMAL_FONT);
        return label;
    }
}
