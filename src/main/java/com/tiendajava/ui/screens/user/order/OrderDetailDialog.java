package com.tiendajava.ui.screens.user.order;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.tiendajava.model.Product;
import com.tiendajava.model.User;
import com.tiendajava.model.orders.Order;
import com.tiendajava.model.orders.OrderItem;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;
import com.tiendajava.utils.ApiResponse;

public class OrderDetailDialog extends JDialog implements java.awt.print.Printable {

    private final Order order;
    private final User user;
    private final ProductService productService = new ProductService();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());

    private final JTable orderItemsTable = new JTable();

    public OrderDetailDialog(MainUI parent, Order order, User user) {
        super(parent, "Order #" + order.getOrder_id(), true);
        this.order = order;
        this.user = user;

        setSize(900, 700);
        setLocationRelativeTo(parent);
        setResizable(true);

        // Configuración del panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.getPrimaryColor());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Añadir contenido
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        mainPanel.add(createFooterPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 30, 30)); // Fondo oscuro para el header
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel izquierdo con logo y título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel logoLabel = new JLabel(AppIcons.BOX_ICON);
        logoLabel.setFont(Fonts.TITLE_FONT.deriveFont(30f));
        logoLabel.setForeground(UITheme.getTextColor());

        JLabel titleLabel = new JLabel("Order Details");
        titleLabel.setFont(Fonts.TITLE_FONT.deriveFont(Font.BOLD, 24));
        titleLabel.setForeground(UITheme.getTextColor());

        JPanel logoTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        logoTitlePanel.setOpaque(false);
        logoTitlePanel.add(logoLabel);
        logoTitlePanel.add(titleLabel);

        titlePanel.add(logoTitlePanel, BorderLayout.WEST);

        // Panel derecho con botón de cierre
        JButton closeButton = new JButton(AppIcons.CANCEL_ICON);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());

        JPanel closeButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeButtonPanel.setOpaque(false);
        closeButtonPanel.add(closeButton);

        titlePanel.add(closeButtonPanel, BorderLayout.EAST);

        // Panel de información resumida
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        summaryPanel.setOpaque(false);

        summaryPanel.add(createSummaryItem("Order ID", String.valueOf(order.getOrder_id())));
        summaryPanel.add(createSummaryItem("Date", formatDate(order.getCreatedAt())));
        summaryPanel.add(createSummaryItem("Status", order.getStatus().toString()));
        summaryPanel.add(createSummaryItem("Total", currencyFormat.format(order.getTotal_amount())));

        headerPanel.add(titlePanel, BorderLayout.NORTH);
        headerPanel.add(summaryPanel, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createSummaryItem(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(Fonts.SMALL_FONT);
        labelComponent.setForeground(new Color(150, 150, 150));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(Fonts.BOLD_NFONT);
        valueComponent.setForeground(UITheme.getTextColor());

        panel.add(labelComponent, BorderLayout.NORTH);
        panel.add(valueComponent, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(UITheme.getPrimaryColor());

        // Panel principal con dos columnas
        JPanel mainContentPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        mainContentPanel.setOpaque(false);

        // Panel izquierdo con información del cliente y orden
        mainContentPanel.add(createOrderAndCustomerPanel());

        // Panel derecho con los items
        mainContentPanel.add(createItemsPanel());

        contentPanel.add(mainContentPanel, BorderLayout.CENTER);
        return contentPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JButton exportPDButton = ButtonFactory.createSecondaryButton("Export to PDF", null, this::exportToPDF);
        exportPDButton.setPreferredSize(new Dimension(150, 40));
        footerPanel.add(exportPDButton);

        return footerPanel;
    }

    private JPanel createOrderAndCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        // Panel de información de la orden
        panel.add(createSectionPanel("Order Information", createOrderInfoPanel()), BorderLayout.NORTH);

        // Panel de información del cliente
        panel.add(createSectionPanel("Customer Information", createCustomerInfoPanel()), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSectionPanel(String title, JPanel content) {
        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setOpaque(false);
        sectionPanel.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 1, 0, new Color(50, 50, 50)),
            new EmptyBorder(10, 0, 10, 0)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Fonts.SUBTITLE_FONT.deriveFont(Font.BOLD, 16));
        titleLabel.setForeground(UITheme.getTextColor());
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        sectionPanel.add(titleLabel, BorderLayout.NORTH);
        sectionPanel.add(content, BorderLayout.CENTER);

        return sectionPanel;
    }

    private JPanel createOrderInfoPanel() {
        JPanel orderInfoPanel = new JPanel(new GridBagLayout());
        orderInfoPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        orderInfoPanel.add(createInfoRow("Order ID: ", String.valueOf(order.getOrder_id())), gbc);

        gbc.gridy++;
        orderInfoPanel.add(createInfoRow("Date: ", formatDate(order.getCreatedAt())), gbc);

        gbc.gridy++;
        orderInfoPanel.add(createInfoRow("Status: ", order.getStatus().toString()), gbc);

        gbc.gridy++;
        orderInfoPanel.add(createInfoRow("Total Amount: ", currencyFormat.format(order.getTotal_amount())), gbc);

        gbc.gridy++;
        System.out.println("Payment Method: " + order.getPayment_method());
        orderInfoPanel.add(createInfoRow("Payment Method: ", order.getPayment_method()), gbc);

        return orderInfoPanel;
    }

    private JPanel createCustomerInfoPanel() {
        JPanel customerInfoPanel = new JPanel(new GridBagLayout());
        customerInfoPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        if (user != null) {
            gbc.gridy = 0;
            customerInfoPanel.add(createInfoRow("Name: ", user.getName() + " " + user.getLastName()), gbc);

            gbc.gridy++;
            customerInfoPanel.add(createInfoRow("Email: ", user.getEmail()), gbc);

            gbc.gridy++;
            customerInfoPanel.add(createInfoRow("Phone: ", user.getPhone()), gbc);

            gbc.gridy++;
            customerInfoPanel.add(createInfoRow("Shipping Address: ", order.getShipping_address()), gbc);
        } else {
            customerInfoPanel.add(createInfoRow("Customer: ", "Not found"), gbc);
        }

        return customerInfoPanel;
    }

    private JPanel createItemsPanel() {
        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.setBackground(UITheme.getPrimaryColor()); 
        itemsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel sectionTitle = new JLabel("Order Items", SwingConstants.LEFT); 
        sectionTitle.setFont(Fonts.SUBTITLE_FONT.deriveFont(Font.BOLD, 18f));
        sectionTitle.setForeground(UITheme.getTextColor());
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(UITheme.getSecondaryColor()); 
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.getBorderColor(), 1), 
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Crear tabla de items
        String[] columnNames = {"Product", "Price", "Quantity", "Subtotal"};

        orderItemsTable.setModel(new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        styleTable(orderItemsTable);

        JTableHeader tableHeader = orderItemsTable.getTableHeader();
        tableHeader.setFont(Fonts.BOLD_NFONT.deriveFont(Font.BOLD)); 
        tableHeader.setForeground(UITheme.getTextColor());
        tableHeader.setBackground(UITheme.getPrimaryColor().darker()); 
        tableHeader.setReorderingAllowed(false);
        tableHeader.setResizingAllowed(false);
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.getBorderColor())); 

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBackground(UITheme.getPrimaryColor().darker()); 
                label.setForeground(UITheme.getTextColor());
                label.setBorder(new EmptyBorder(8, 0, 8, 0)); // Padding
                return label;
            }
        };
        for (int i = 0; i < orderItemsTable.getColumnModel().getColumnCount(); i++) {
            orderItemsTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(orderItemsTable);
        scrollPane.getViewport().setBackground(UITheme.getSecondaryColor());
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());

        tableContainer.add(scrollPane, BorderLayout.CENTER);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setOpaque(false); 
        totalPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        List<OrderItem> items = order.getOrderItems();
        if (items != null && !items.isEmpty()) {
            for (OrderItem item : items) {
                ApiResponse<Product> productResponse = productService.getProductById(item.getProduct_id());
                Product product = productResponse.isSuccess() ? productResponse.getData() : null;

                if (product != null) {
                    ((DefaultTableModel) orderItemsTable.getModel()).addRow(new Object[]{
                        product.getName(),
                        currencyFormat.format(item.getPrice()),
                        item.getQuantity(),
                        currencyFormat.format(item.getSubtotal())
                    });
                } else {
                    ((DefaultTableModel) orderItemsTable.getModel()).addRow(new Object[]{
                        "Product ID: " + item.getProduct_id() + " (Not Found)", 
                        currencyFormat.format(item.getPrice()),
                        item.getQuantity(),
                        currencyFormat.format(item.getSubtotal())
                    });
                }
            }
        } else {
            ((DefaultTableModel) orderItemsTable.getModel()).addRow(new Object[]{"No items in this order", "", "", ""});
        }


        JLabel totalLabel = new JLabel("Total: " + order.getTotal_amount());
        totalLabel.setFont(Fonts.BOLD_NFONT.deriveFont(Font.BOLD, 16f));
        totalLabel.setForeground(UITheme.getInfoColor().brighter()); 
        totalLabel.setBorder(new EmptyBorder(5, 10, 5, 10));

        totalPanel.add(totalLabel);
        tableContainer.add(totalPanel, BorderLayout.SOUTH);

        itemsPanel.add(sectionTitle, BorderLayout.NORTH);
        itemsPanel.add(tableContainer, BorderLayout.CENTER);

        return itemsPanel;
    }

    // Método para aplicar estilos generales a la tabla
    private void styleTable(JTable table) {
        table.setFont(Fonts.NORMAL_FONT);
        table.setForeground(UITheme.getTextColor());
        table.setBackground(UITheme.getSecondaryColor());
        table.setSelectionBackground(UITheme.getPrimaryColor());
        table.setSelectionForeground(UITheme.getTextColor());
        table.setGridColor(UITheme.getBorderColor().darker()); 
        table.setRowHeight(35);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(new EmptyBorder(5, 5, 5, 5));

                if (isSelected) {
                    setBackground(UITheme.getPrimaryColor().darker());
                    setForeground(UITheme.getTextColor());
                } else {
                    setBackground(row % 2 == 0 ? UITheme.getPrimaryColor().brighter() : UITheme.getSecondaryColor().darker());
                    setForeground(UITheme.getTextColor());
                }
                return this;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JPanel createInfoRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(2, 0, 2, 0));

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(Fonts.SMALL_FONT);
        labelComponent.setForeground(new Color(150, 150, 150));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(Fonts.NORMAL_FONT);
        valueComponent.setForeground(UITheme.getTextColor());

        panel.add(labelComponent, BorderLayout.WEST);
        panel.add(valueComponent, BorderLayout.CENTER);

        return panel;
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = inputFormat.parse(dateString);
            return dateFormat.format(date);
        } catch (ParseException e) {
            return dateString;
        }
    }

    private void exportToPDF() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                NotificationHandler.error("Error exporting to PDF: " + e.getMessage());
            }
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;

        Color backgroundColor = UITheme.getPrimaryColor(); 
        g2d.setColor(backgroundColor);
        g2d.fillRect(
            (int) pageFormat.getImageableX(),
            (int) pageFormat.getImageableY(),
            (int) pageFormat.getImageableWidth(),
            (int) pageFormat.getImageableHeight()
        );

        AffineTransform oldTransform = g2d.getTransform();

        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        Component panelToPrint = getContentPane();

        double panelWidth = panelToPrint.getWidth();
        double panelHeight = panelToPrint.getHeight();

        if (panelWidth <= 0 || panelHeight <= 0) {
            return PAGE_EXISTS;
        }

        double scaleX = pageFormat.getImageableWidth() / panelWidth;
        double scaleY = pageFormat.getImageableHeight() / panelHeight;
        double scale = Math.min(scaleX, scaleY);

        g2d.scale(scale, scale);

        int xOffset = (int) ((pageFormat.getImageableWidth() / scale - panelWidth) / 2);
        int yOffset = (int) ((pageFormat.getImageableHeight() / scale - panelHeight) / 2);
        g2d.translate(xOffset, yOffset); 

        panelToPrint.printAll(g2d);

        g2d.setTransform(oldTransform);

        return PAGE_EXISTS;
    }
}
