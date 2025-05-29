package com.tiendajava.ui.screens.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.DashboardSummary;
import com.tiendajava.model.Product;
import com.tiendajava.model.orders.OrderStatusReport;
import com.tiendajava.service.ReportService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class AdminDashboardScreen extends JPanel {

    private final ReportService reportService = new ReportService();
    private final JPanel cardsPanel = new JPanel(new GridLayout(0, 2, 20, 20));

    public AdminDashboardScreen(MainUI parent) {
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel title = new JLabel("Admin Dashboard", AppIcons.HOME_ICON, SwingConstants.LEFT);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        cardsPanel.setBackground(UITheme.getPrimaryColor());

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getHorizontalScrollBar().setUI(UIUtils.createDarkScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        // Cargar resumen
        loadDashboardSummary();
    }

    private void loadDashboardSummary() {
        cardsPanel.removeAll();

        // Cargar datos
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

        // 📦 Número total de productos
        cardsPanel.add(createInfoCard("Total Products", dashboard.getTotalProducts() + "", AppIcons.PRODUCTS_ICON, UITheme.getSuccessColor()));

        // 👤 Número total de usuarios
        cardsPanel.add(createInfoCard("Total Users", dashboard.getTotalUsers() + "", AppIcons.USERS_ICON, UITheme.getInfoColor()));

        // 🛒 Productos fuera de stock
        cardsPanel.add(createInfoCard("Out of Stock", String.valueOf(outOfStockProducts != null ? outOfStockProducts.size() : 0), AppIcons.PRODUCT_CONFIG_ICON, UITheme.getWarningColor()));

        // 🧾 Órdenes por estado
        if (ordersStatus != null) {
            for (OrderStatusReport status : ordersStatus) {
                cardsPanel.add(createInfoCard(status.getStatus(), String.valueOf(status.getCount()), AppIcons.CART_ICON, UITheme.getInfoColor()));
            }
        } else {
            cardsPanel.add(createInfoCard("Orders Status", "No data available", AppIcons.EDIT_ICON, UITheme.getWarningColor()));
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createInfoCard(String title, String value, ImageIcon icon, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.getSecondaryColor());
        card.setBorder(BorderFactory.createLineBorder(UITheme.getTertiaryColor(), 2));
        card.setPreferredSize(new Dimension(250, 150));

        JLabel iconLabel = new JLabel(icon);
        // set a margin top 
        iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(iconLabel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(Fonts.SUBTITLE_FONT);
        titleLabel.setForeground(color);
        card.add(titleLabel, BorderLayout.CENTER);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        // set a margin bottom
        valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        valueLabel.setFont(Fonts.TITLE_FONT);
        valueLabel.setForeground(UITheme.getTextColor());
        card.add(valueLabel, BorderLayout.SOUTH);

        return card;
    }
}
