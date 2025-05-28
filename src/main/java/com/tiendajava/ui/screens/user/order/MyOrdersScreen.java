package com.tiendajava.ui.screens.user.order;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.model.orders.Order;
import com.tiendajava.model.orders.OrderItem;
import com.tiendajava.service.OrderService;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class MyOrdersScreen extends JPanel {

    private final MainUI parent;
    private final OrderService orderService = new OrderService();
    private final JPanel ordersPanel = new JPanel();
    private final JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All", "Pending", "Completed", "Cancelled"});

    public MyOrdersScreen(MainUI parent) {
        this.parent = parent;

        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());

        JLabel title = new JLabel("My Orders", SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // create a filter by status
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.setBackground(UITheme.getPrimaryColor());
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel filterLabel = new JLabel("Filter by status: ");
        filterLabel.setFont(Fonts.NORMAL_FONT);
        filterLabel.setForeground(UITheme.getTextColor());
        statusFilter.setFont(Fonts.NORMAL_FONT);
        statusFilter.setBackground(UITheme.getSecondaryColor());
        statusFilter.setForeground(UITheme.getTextColor());
        statusFilter.setBorder(BorderFactory.createLineBorder(UITheme.getTertiaryColor(), 1));
        statusFilter.addActionListener(e -> {
            String selectedStatus = (String) statusFilter.getSelectedItem();
            if (selectedStatus != null) {
                loadOrders(); 
            }
        });
        filterPanel.add(filterLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        filterPanel.add(statusFilter);
        add(filterPanel);


        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setBackground(UITheme.getPrimaryColor());

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        add(scrollPane, BorderLayout.CENTER);

        loadOrders();
    }

    private void loadOrders() {
        ordersPanel.removeAll();

        ApiResponse<List<Order>> response = orderService.getMyOrders();
        if (!response.isSuccess()) {
            NotificationHandler.error("Failed to load orders: " + response.getMessage());
            return;
        }

        List<Order> orders = response.getData();
        if (orders == null || orders.isEmpty()) {
            JLabel noOrders = new JLabel("You have no orders.", SwingConstants.CENTER);
            noOrders.setFont(Fonts.NORMAL_FONT);
            noOrders.setForeground(UITheme.getTextColor());
            ordersPanel.add(noOrders);
        } else {
            for (Order order : orders) {
                ordersPanel.add(createOrderCard(order));
                ordersPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        ordersPanel.revalidate();
        ordersPanel.repaint();
    }

    private JPanel createOrderCard(Order order) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setBackground(UITheme.getSecondaryColor());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.getTertiaryColor(), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setPreferredSize(new Dimension(300, 200));
        card.setMaximumSize(new Dimension(350, 200));

        JLabel idLabel = new JLabel("Order ID: " + order.getOrder_id());
        JLabel dateLabel = new JLabel("Date: " + order.getCreatedAt());
        JLabel statusLabel = new JLabel("Status: " + order.getStatus());
        JLabel totalLabel = new JLabel("Total: $" + order.getTotal_amount());

        for (JLabel label : List.of(idLabel, dateLabel, statusLabel, totalLabel)) {
            label.setFont(Fonts.NORMAL_FONT);
            label.setForeground(UITheme.getTextColor());
            card.add(label);
        }

        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(new JLabel("Items:", SwingConstants.LEFT));

        if(order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            NotificationHandler.error("No items found in order ID: " + order.getOrder_id());
            JLabel noItemsLabel = new JLabel("No items in this order.", SwingConstants.CENTER);
            noItemsLabel.setFont(Fonts.NORMAL_FONT);
            noItemsLabel.setForeground(UITheme.getTextColor());
            card.add(noItemsLabel);
            return card;
        }


        for (OrderItem item : order.getOrderItems()) {
            ProductService productService = new ProductService();
            ApiResponse<Product> response = productService.getProductById(item.getProduct_id());
            if (!response.isSuccess()) {
                NotificationHandler.error("Failed to load product details: " + response.getMessage());
                continue;
            }
            Product product = response.getData();
            if (product == null) {
                NotificationHandler.error("Product not found for ID: " + item.getProduct_id());
                continue;
            }

            JLabel itemLabel = new JLabel("- " + product.getName() + " ( x" + item.getQuantity() + " )");
            itemLabel.setFont(Fonts.SMALL_FONT);
            itemLabel.setForeground(UITheme.getTextColor());
            card.add(itemLabel);
        }

        return card;
    }

    @Override
    public MainUI getParent() {
        return parent;
    }
}
