package com.tiendajava.ui.screens.admin;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.DashboardSummary;
import com.tiendajava.model.OrderStatusReport;
import com.tiendajava.model.Product;
import com.tiendajava.service.ReportService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class AdminDashboardScreen extends JPanel {

    private final MainUI parent;
    private final ReportService reportService = new ReportService();

    public AdminDashboardScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setForeground(UITheme.getTextColor());
        initUI();
    }

    private void initUI() {
        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(1, 2, 20, 20));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        content.setBackground(UITheme.getPrimaryColor());

        // Panel de resumen
        JPanel summaryPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        summaryPanel.setBackground(UITheme.getSecondaryColor());
        summaryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UITheme.getTextColor()), "Summary", 0, 0, Fonts.NORMAL_FONT, UITheme.getTextColor()));

        // Panel de pedidos por estado
        JPanel ordersStatusPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        ordersStatusPanel.setBackground(UITheme.getSecondaryColor());
        ordersStatusPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UITheme.getTextColor()), "Orders by Status", 0, 0, Fonts.NORMAL_FONT, UITheme.getTextColor()));

        content.add(summaryPanel);
        content.add(ordersStatusPanel);

        add(new JScrollPane(content), BorderLayout.CENTER);

        // Cargar datos
        loadDashboardSummary(summaryPanel);
        loadOrdersByStatus(ordersStatusPanel);
    }

    private void loadDashboardSummary(JPanel panel) {
        ApiResponse<DashboardSummary> response = reportService.getDashboardSummary();
        DashboardSummary summary = response != null ? response.getData() : null;
        ApiResponse<List<Product>> outOfStock = reportService.getOutOfStockProducts();
        if (summary != null) {
            panel.add(createLabel("Total of users: " + summary.getTotalUsers()));
            panel.add(createLabel("Total Products: " + summary.getTotalProducts()));
            panel.add(createLabel("Producto out of stock: " + outOfStock.getData().size()));
            panel.add(createLabel("Total Orders: " + summary.getTotalOrders()));
            panel.add(createLabel("Total Incomes: $" + summary.getTotalRevenue()));
        } else {
            panel.add(createLabel("Could not load the summary."));
        }
    }

    private void loadOrdersByStatus(JPanel panel) {
        ApiResponse<List<OrderStatusReport>> response = reportService.getOrdersByStatus();
        List<OrderStatusReport> reports = response != null ? response.getData() : null;
        if (reports != null && !reports.isEmpty()) {
            for (OrderStatusReport report : reports) {
                panel.add(createLabel(report.getStatus() + ": " + report.getCount()));
            }
        } else {
            panel.add(createLabel("Could not load the orders by status."));
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Fonts.NORMAL_FONT);
        label.setForeground(UITheme.getTextColor());
        return label;
    }
}
