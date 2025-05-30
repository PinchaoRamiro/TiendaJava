package com.tiendajava.ui.screens.user.order;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.model.orders.Order;
import com.tiendajava.model.orders.OrderItem;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class OrderDetailDialog extends JDialog {

    private final MainUI parent;
    private final Order order;
    private final ProductService productService = new ProductService();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());

    private final JLabel orderIdLabel = new JLabel();
    private final JLabel orderDateLabel = new JLabel();
    private final JLabel orderTotalLabel = new JLabel();
    private final JLabel orderStatusLabel = new JLabel();
    private final JLabel shippingAddressLabel = new JLabel();
    private final JLabel paymentMethodLabel = new JLabel();
    private final JTable orderItemsTable;
    private final DefaultTableModel tableModel;

    public OrderDetailDialog(MainUI parent, Order order) {
        super(parent, "Order Details", true); // Modal dialog
        this.parent = parent;
        this.order = order;

        // Configuración del modelo y la tabla para los ítems de la orden
        String[] columnNames = {"Product", "Qty", "Unit Price", "Subtotal"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderItemsTable = new JTable(tableModel);

        // Estilo de la tabla
        styleTable();

        // Configuración del diálogo
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Layout principal
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.getPrimaryColor());

        initializeUI();
        displayOrderDetails();
    }

    private void styleTable() {
        orderItemsTable.setFont(Fonts.NORMAL_FONT);
        orderItemsTable.setForeground(UITheme.getTextColor());
        orderItemsTable.setBackground(UITheme.getBackgroundContrast());
        orderItemsTable.setSelectionBackground(UITheme.getPrimaryColor());
        orderItemsTable.setGridColor(UITheme.getTertiaryColor());
        orderItemsTable.setRowHeight(30);

        // Estilo del encabezado
        orderItemsTable.getTableHeader().setFont(Fonts.BOLD_NFONT);
        orderItemsTable.getTableHeader().setForeground(UITheme.getTextColor());
        orderItemsTable.getTableHeader().setBackground(UITheme.getPrimaryColor().darker());
        orderItemsTable.getTableHeader().setReorderingAllowed(false);
    }

    private void initializeUI() {
        // Panel principal con borde decorativo
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(UITheme.getPrimaryColor());

        // Panel superior para el título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("🛒 Order Details", SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UITheme.getTextColor());
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        titlePanel.add(title, BorderLayout.CENTER);

        // Panel de información de la orden
        JPanel orderInfoPanel = createOrderInfoPanel();

        // Panel de ítems de la orden
        JPanel itemsPanel = createItemsPanel();

        // Panel de acciones
        JPanel actionsPanel = createActionsPanel();

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(orderInfoPanel, BorderLayout.CENTER);
        mainPanel.add(itemsPanel, BorderLayout.SOUTH);
        mainPanel.add(actionsPanel, BorderLayout.PAGE_END);

        add(mainPanel);
    }

    private JPanel createOrderInfoPanel() {
        JPanel orderInfoPanel = new JPanel(new GridBagLayout());
        orderInfoPanel.setOpaque(false);
        orderInfoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.getBorderColor(), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        orderInfoPanel.setBackground(UITheme.getSecondaryColor());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Fila 1: Order ID y Date
        gbc.gridx = 0; gbc.gridy = 0;
        orderInfoPanel.add(createBoldLabel("Order ID:"), gbc);
        gbc.gridx = 1;
        orderInfoPanel.add(orderIdLabel, gbc);
        gbc.gridx = 2; gbc.weightx = 1.0;
        orderInfoPanel.add(createBoldLabel("Order Date:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.0;
        orderInfoPanel.add(orderDateLabel, gbc);

        // Fila 2: Total y Status
        gbc.gridx = 0; gbc.gridy = 1;
        orderInfoPanel.add(createBoldLabel("Total Amount:"), gbc);
        gbc.gridx = 1;
        orderInfoPanel.add(orderTotalLabel, gbc);
        gbc.gridx = 2; gbc.weightx = 1.0;
        orderInfoPanel.add(createBoldLabel("Status:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.0;
        orderInfoPanel.add(orderStatusLabel, gbc);

        // Fila 3: Payment Method
        gbc.gridx = 0; gbc.gridy = 2;
        orderInfoPanel.add(createBoldLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        orderInfoPanel.add(paymentMethodLabel, gbc);

        // Fila 4: Shipping Address (ocupa toda la fila)
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        orderInfoPanel.add(createBoldLabel("Shipping Address:"), gbc);
        gbc.gridy = 4;
        orderInfoPanel.add(shippingAddressLabel, gbc);

        return orderInfoPanel;
    }

    private JPanel createItemsPanel() {
        JPanel itemsPanel = new JPanel(new BorderLayout(0, 10));
        itemsPanel.setOpaque(false);
        itemsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel itemsTitle = createBoldLabel("Order Items:");
        itemsPanel.add(itemsTitle, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(orderItemsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.getBorderColor(), 1));
        scrollPane.getViewport().setBackground(UITheme.getBackgroundContrast());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        itemsPanel.add(scrollPane, BorderLayout.CENTER);

        return itemsPanel;
    }

    private JPanel createActionsPanel() {
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        actionsPanel.setBackground(UITheme.getPrimaryColor().darker());
        actionsPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton backButton = ButtonFactory.createSecondaryButton("Back to Orders", null, () -> {
            dispose(); // Cerrar el diálogo
        });

        JButton exportButton = ButtonFactory.createPrimaryButton("Export to PDF", null, this::exportToPDF);
        exportButton.setPreferredSize(new Dimension(150, 40));

        actionsPanel.add(exportButton);
        actionsPanel.add(backButton);

        return actionsPanel;
    }

    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Fonts.BOLD_NFONT);
        label.setForeground(UITheme.getTextColor());
        return label;
    }

    private void displayOrderDetails() {
        if (order == null) {
            orderIdLabel.setText("N/A");
            orderDateLabel.setText("N/A");
            orderTotalLabel.setText("N/A");
            orderStatusLabel.setText("N/A");
            shippingAddressLabel.setText("N/A");
            paymentMethodLabel.setText("N/A");
            tableModel.setRowCount(0);
            tableModel.addRow(new Object[]{"No order details available.", "", "", ""});
            return;
        }

        // Formatear la fecha
        String createdAt = order.getCreatedAt();
        Date date;
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            date = isoFormat.parse(createdAt);
        } catch (ParseException e) {
            date = new Date(); // Fecha actual como fallback
        }

        SimpleDateFormat displayFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());
        String formattedDate = displayFormat.format(date);

        orderIdLabel.setText("#" + order.getOrder_id());
        orderDateLabel.setText(formattedDate);
        orderTotalLabel.setText(currencyFormat.format(order.getTotal_amount()));

        // Estilo del estado según su valor
        orderStatusLabel.setText(order.getStatus().toString());
        switch (order.getStatus().toString().toLowerCase()) {
            case "approved" -> orderStatusLabel.setForeground(UITheme.getSuccessColor());
            case "rejected" -> orderStatusLabel.setForeground(UITheme.getDangerColor());
            case "pending" -> orderStatusLabel.setForeground(UITheme.getWarningColor());
            default -> orderStatusLabel.setForeground(UITheme.getTextColor());
        }

        shippingAddressLabel.setText(order.getShipping_address());
        paymentMethodLabel.setText(order.getPayment_method());

        // Rellenar la tabla de ítems
        tableModel.setRowCount(0);
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            tableModel.addRow(new Object[]{"No items in this order", "", "", ""});
        } else {
            for (OrderItem item : order.getOrderItems()) {
                ApiResponse<Product> productResponse = productService.getProductById(item.getProduct_id());
                if (!productResponse.isSuccess()) {
                    tableModel.addRow(new Object[]{
                        "Error loading product",
                        item.getQuantity(),
                        currencyFormat.format(item.getPrice()),
                        currencyFormat.format(item.getSubtotal())
                    });
                    continue;
                }
                Product product = productResponse.getData();
                tableModel.addRow(new Object[]{
                    product != null ? product.getName() : "Unknown Product",
                    item.getQuantity(),
                    currencyFormat.format(item.getPrice()),
                    currencyFormat.format(item.getSubtotal())
                });
            }
        }
    }

    private void exportToPDF() {
        // Implementación de exportación a PDF
        JOptionPane.showMessageDialog(this,
            "PDF export functionality would be implemented here",
            "Export to PDF",
            JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public MainUI getParent() {
        return parent;
    }
}
