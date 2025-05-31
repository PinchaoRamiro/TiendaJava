package com.tiendajava.ui.screens.user.order;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Session;
import com.tiendajava.model.orders.Order;
import com.tiendajava.service.OrderService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class OrderHistoryScreen extends JPanel {

    private final MainUI parent;
    private final OrderService orderService;
    private final JTable orderHistoryTable;
    private final DefaultTableModel tableModel;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("es", "CO"));
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private List<Order> orders;

    public OrderHistoryScreen(MainUI parent) {
        this.parent = parent;
        this.orderService = new OrderService();

        String[] columnNames = {"Order ID", "Date", "Total", "Status", "Shipping Address"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderHistoryTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                c.setBackground(UITheme.getSecondaryColor());
                c.setForeground(UITheme.getTextColor());
                if (!isRowSelected(row)) {
                    c.setBackground(new Color(
                        Math.min(255, c.getBackground().getRed() + 10),
                        Math.min(255, c.getBackground().getGreen() + 10),
                        Math.min(255, c.getBackground().getBlue() + 10)
                    ));
                }

                return c;
            }
        };

        configureTableAppearance();

        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        initializeUI();
        loadOrderHistory();
    }

    private void configureTableAppearance() {
        orderHistoryTable.setFont(Fonts.NORMAL_FONT);
        orderHistoryTable.setRowHeight(30);
        orderHistoryTable.setAutoCreateRowSorter(true);
        orderHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                return this;
            }
        };

        for (int i = 0; i < orderHistoryTable.getColumnCount(); i++) {
            orderHistoryTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        orderHistoryTable.getTableHeader().setFont(Fonts.BOLD_NFONT);
        orderHistoryTable.getTableHeader().setForeground(UITheme.getTextColor());
        orderHistoryTable.getTableHeader().setBackground(UITheme.getPrimaryColor().darker());
        orderHistoryTable.getTableHeader().setReorderingAllowed(false);
        orderHistoryTable.getTableHeader().setResizingAllowed(false);

        orderHistoryTable.setSelectionBackground(UITheme.getPrimaryColor().brighter());
        orderHistoryTable.setSelectionForeground(UITheme.getTextColor());
    }

    private void initializeUI() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("📦 Order History", SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UITheme.getTextColor());
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        titlePanel.add(title, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);

        // Panel de la tabla con scroll
        JScrollPane scrollPane = new JScrollPane(orderHistoryTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.getBorderColor(), 1));
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());

        add(scrollPane, BorderLayout.CENTER);

        // Panel de acciones
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        actionsPanel.setBackground(UITheme.getSecondaryColor());
        actionsPanel.setOpaque(true);
        actionsPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        JButton viewDetailsButton = ButtonFactory.createPrimaryButton("View Order Details", null, () -> {
            int selectedRow = orderHistoryTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    int orderId = (int) tableModel.getValueAt(selectedRow, 0);
                    Order selectedOrder = orders.stream()
                            .filter(order -> order.getOrder_id() == orderId)
                            .findFirst()
                            .orElse(null);
                    if (selectedOrder != null) {
                        new OrderDetailDialog(parent, selectedOrder, Session.getInstance().getUser()).setVisible(true);
                    } else {
                        NotificationHandler.error("Failed to load order details");
                    }
                } catch (Exception e) {
                    NotificationHandler.error("Error viewing order details: " + e.getMessage());
                }
            } else {
                NotificationHandler.warning("Please select an order to view details.");
            }
        });
        viewDetailsButton.setPreferredSize(new Dimension(180, 40));

        actionsPanel.add(viewDetailsButton);

        add(actionsPanel, BorderLayout.SOUTH);
    }

    private void loadOrderHistory() {
        tableModel.setRowCount(0);

        ApiResponse<List<Order>> response = orderService.getMyOrders();
        if (!response.isSuccess()) {
            NotificationHandler.error("Failed to load order history: " + response.getMessage());
            return;
        }

        orders = response.getData();

        if (orders == null || orders.isEmpty()) {
            tableModel.addRow(new Object[]{"No orders found", "", "", "", ""});
            orderHistoryTable.setEnabled(false);
        } else {
            orderHistoryTable.setEnabled(true);
            for (Order order : orders) {
                Date date;
                try {
                    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    date = isoFormat.parse(order.getCreatedAt());
                } catch (ParseException e) {
                    date = new Date(); 
                }

                tableModel.addRow(new Object[]{
                    order.getOrder_id(),
                    dateFormat.format(date),
                    currencyFormat.format(order.getTotal_amount()),
                    order.getStatus(),
                    order.getShipping_address()
                });
            }
        }
    }
}
