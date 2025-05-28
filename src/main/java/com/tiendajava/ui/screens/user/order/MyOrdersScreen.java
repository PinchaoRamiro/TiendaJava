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
import com.tiendajava.model.orders.Order;
import com.tiendajava.model.orders.OrderItem;
import com.tiendajava.service.OrderService;
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
    private List<Order> orders;

    public MyOrdersScreen(MainUI parent) {
        this.parent = parent;

        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(UITheme.getPrimaryColor());

        JLabel title = new JLabel("My Orders", SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        headerPanel.add(title);

        // create a filter by status
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        filterPanel.setBackground(UITheme.getPrimaryColor());
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel filterLabel = new JLabel("Filter by status: ");
        filterLabel.setFont(Fonts.NORMAL_FONT);
        filterLabel.setForeground(UITheme.getTextColor());
        statusFilter.setFont(Fonts.NORMAL_FONT);
        statusFilter.setBackground(UITheme.getBackgroundContrast());
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
        headerPanel.add(filterPanel);

        add(headerPanel, BorderLayout.NORTH);



        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setBackground(UITheme.getPrimaryColor());

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        add(scrollPane, BorderLayout.CENTER);

        getOrderToDataBase();
        loadOrders();
    }


    private void getOrderToDataBase(){
        ApiResponse<List<Order>> response = orderService.getMyOrders();
        if (!response.isSuccess()) {
            NotificationHandler.error("Failed to load orders: " + response.getMessage());
            return;
        }

        orders = response.getData();
    }

    private void loadOrders() {
        ordersPanel.removeAll();

        String selectedStatus = (String) statusFilter.getSelectedItem();

        if (orders == null || orders.isEmpty()) {
            JLabel noOrders = new JLabel("You have no orders.", SwingConstants.CENTER);
            noOrders.setFont(Fonts.NORMAL_FONT);
            noOrders.setForeground(UITheme.getTextColor());
            ordersPanel.add(noOrders);
        } else {
            orders.stream()
                .filter(order -> {
                    if ("All".equalsIgnoreCase(selectedStatus)) return true;
                    return order.getStatus().toString().equalsIgnoreCase(selectedStatus);
                })
                .forEach(order -> {
                    ordersPanel.add(createOrderCard(order));
                    ordersPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                });
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
        card.setPreferredSize(new Dimension(350, 220));
        card.setMaximumSize(new Dimension(600, 250));

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

        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            JLabel noItems = new JLabel("No items in this order.");
            noItems.setFont(Fonts.SMALL_FONT);
            noItems.setForeground(UITheme.getTextColor());
            card.add(noItems);
            return card;
        }

        JLabel itemTitle = new JLabel("Items:");
        itemTitle.setFont(Fonts.BOLD_NFONT);
        itemTitle.setForeground(UITheme.getTextColor());
        card.add(itemTitle);

        for (OrderItem item : order.getOrderItems()) {
            // Muestra solo el ID y cantidad (opcional: puedes guardar un mapa de nombres si ya los tienes)
            String itemText = "• Product ID: " + item.getProduct_id() + " (x" + item.getQuantity() + ")";
            JLabel itemLabel = new JLabel(itemText);
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
