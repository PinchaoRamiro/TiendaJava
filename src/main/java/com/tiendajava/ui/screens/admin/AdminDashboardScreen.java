package com.tiendajava.ui.screens.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.DashboardSummary;
import com.tiendajava.model.Product;
import com.tiendajava.model.Session;
import com.tiendajava.model.orders.OrderStatusReport;
import com.tiendajava.service.ReportService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;
import com.tiendajava.ui.utils.animations.TypingLabel;

public class AdminDashboardScreen extends JPanel {

    private final ReportService reportService = new ReportService();
    private final JPanel cardsPanel = new JPanel(new GridLayout(0, 2, 20, 20));

    public AdminDashboardScreen(MainUI parent) {
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.getPrimaryColor());
        headerPanel.setBorder(UIUtils.getDefaultPadding(20, 20, 10, 20));

        JLabel title = new JLabel("Admin Dashboard", AppIcons.HOME_ICON, SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());

        // Mensaje de bienvenida personalizado
        TypingLabel welcomeLabel = new TypingLabel(
            "Welcome, " + Session.getInstance().getUser().getName() + "!",
            80
        );
        welcomeLabel.setFont(Fonts.SUBTITLE_FONT);
        welcomeLabel.setForeground(UITheme.getSuccessColor());
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomeLabel.startTyping();

        add(headerPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(scrollPane, BorderLayout.CENTER);

        loadDashboardSummary();
    }

    private void loadDashboardSummary() {
        cardsPanel.removeAll();

        ApiResponse<DashboardSummary> dashboardResponse = reportService.getDashboardSummary();
        ApiResponse<List<OrderStatusReport>> ordersStatusResponse = reportService.getOrdersByStatus();
        ApiResponse<List<Product>> outOfStockResponse = reportService.getOutOfStockProducts();

        if (!dashboardResponse.isSuccess() && !ordersStatusResponse.isSuccess() && !outOfStockResponse.isSuccess()) {
            NotificationHandler.error("Failed to load dashboard data");
            return;
        }

        DashboardSummary dashboard = dashboardResponse.getData();
        List<OrderStatusReport> ordersStatus = ordersStatusResponse.getData();
        List<Product> outOfStockProducts = outOfStockResponse.getData();

        cardsPanel.add(createInfoCard("Total Products", String.valueOf(dashboard.getTotalProducts()), AppIcons.PRODUCTS_ICON, UITheme.getSuccessColor()));
        cardsPanel.add(createInfoCard("Total Users", String.valueOf(dashboard.getTotalUsers()), AppIcons.USERS_ICON, UITheme.getInfoColor()));
        cardsPanel.add(createInfoCard("Out of Stock", String.valueOf(outOfStockProducts != null ? outOfStockProducts.size() : 0), AppIcons.PRODUCT_CONFIG_ICON, UITheme.getWarningColor()));

        if (ordersStatus != null) {
            for (OrderStatusReport status : ordersStatus) {
                cardsPanel.add(createInfoCard("Orders - " + status.getStatus(), String.valueOf(status.getCount()), AppIcons.CART_ICON, UITheme.getInfoColor()));
            }
        } else {
            cardsPanel.add(createInfoCard("Orders Status", "No data available", AppIcons.EDIT_ICON, UITheme.getWarningColor()));
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createInfoCard(String title, String value, ImageIcon icon, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.getSecondaryColor());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.getTertiaryColor(), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        card.setPreferredSize(new Dimension(240, 150));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(Fonts.SUBTITLE_FONT);
        titleLabel.setForeground(color);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        card.add(titleLabel);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setFont(Fonts.TITLE_FONT);
        valueLabel.setForeground(UITheme.getTextColor());
        card.add(valueLabel);

        return card;
    }
}
