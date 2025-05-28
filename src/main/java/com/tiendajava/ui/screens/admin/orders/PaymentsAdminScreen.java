package com.tiendajava.ui.screens.admin.orders;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.User;
import com.tiendajava.model.orders.Order;
import com.tiendajava.service.OrderService;
import com.tiendajava.service.UserService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class PaymentsAdminScreen extends JPanel {

    private final MainUI parent;
    private final OrderService orderService = new OrderService();
    private final UserService userService = new UserService();
    private final JPanel paymentsPanel = new JPanel();

    public PaymentsAdminScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());

        JLabel title = new JLabel("Payments Overview", SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        paymentsPanel.setLayout(new BoxLayout(paymentsPanel, BoxLayout.Y_AXIS));
        paymentsPanel.setBackground(UITheme.getPrimaryColor());

        JScrollPane scrollPane = new JScrollPane(paymentsPanel);
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        add(scrollPane, BorderLayout.CENTER);

        loadPayments();
    }

    private void loadPayments() {
        paymentsPanel.removeAll();
        ApiResponse<List<Order>> response = orderService.getAllOrders();
        if (!response.isSuccess()) {
            NotificationHandler.error("Failed to load payments: " + response.getMessage());
            return;
        }

        List<Order> orders = response.getData();
        if (orders == null || orders.isEmpty()) {
            JLabel noPayments = new JLabel("No payments found.", SwingConstants.CENTER);
            noPayments.setFont(Fonts.NORMAL_FONT);
            noPayments.setForeground(UITheme.getTextColor());
            paymentsPanel.add(noPayments);
        } else {
            for (Order order : orders) {
                paymentsPanel.add(createPaymentCard(order));
                paymentsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }
        paymentsPanel.revalidate();
        paymentsPanel.repaint();
    }

    private JPanel createPaymentCard(Order order) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.getSecondaryColor());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.getTertiaryColor(), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 10)
        ));
        card.setMaximumSize(new Dimension(500, 200));

        User user = userService.findUserById(order.getUser_id()).getData();

        JLabel userLabel = new JLabel("User: " + (user != null ? user.getName() + " " + user.getLastName() : "Unknown"));
        JLabel methodLabel = new JLabel("Payment Method: " + order.getPayment_method());
        JLabel statusLabel = new JLabel("Status: " + order.getStatus());
        JLabel totalLabel = new JLabel("Amount: $" + order.getTotal_amount());
        JLabel dateLabel = new JLabel("Date: " + order.getCreatedAt());

        for (JLabel label : List.of(userLabel, methodLabel, statusLabel, totalLabel, dateLabel)) {
            label.setFont(Fonts.NORMAL_FONT);
            label.setForeground(UITheme.getTextColor());
            card.add(label);
        }

        // Status update
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setBackground(UITheme.getSecondaryColor());
        String[] statuses = {"Pending", "Approved", "Rejected"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        statusCombo.setSelectedItem(order.getStatus().name());
        JButton updateBtn = new JButton("Update Status");

        updateBtn.addActionListener(e -> {
            String selected = (String) statusCombo.getSelectedItem();
            if (selected != null && !selected.equals(order.getStatus().name())) {
                boolean success = orderService.updateOrderStatus(order.getOrder_id(), selected).isSuccess();
                if (success) {
                    NotificationHandler.success("Payment status updated.");
                    loadPayments();
                } else {
                    NotificationHandler.error("Failed to update status.");
                }
            }
        });

        // Botton to complete imformation
        JButton detailsBtn = new JButton("View Details");
        // detailsBtn.addActionListener(e -> {
        //     DetailsOrderDialog dialog = new DetailsOrderDialog( order, user);
        //     dialog.setVisible(true);
        // });

        statusPanel.add(statusCombo);
        statusPanel.add(updateBtn);
        statusPanel.add(detailsBtn);
        card.add(statusPanel);

        return card;
    }

    @Override
    public MainUI getParent() {
        return parent;
    }
}
