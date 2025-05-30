package com.tiendajava.ui.screens.user.order;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat; 
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory; 
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField; 
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.tiendajava.model.Cart; 
import com.tiendajava.model.Product;
import com.tiendajava.model.orders.Order;
import com.tiendajava.service.OrderService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class OrderScreen extends JPanel implements Printable {

    private final OrderService orderService = new OrderService();
    private Cart cart;
    private final MainUI parent;
    private final JTextField addressField = new JTextField(30);
    private final JComboBox<String> paymentMethodCombo = new JComboBox<>(new String[]{"Credit Card", "PayPal", "Cash on Delivery"});
    private final JTable cartItemsTable;
    private final DefaultTableModel tableModel; 
    private final JLabel totalLabel = new JLabel();
    private final JLabel orderDateLabel = new JLabel();
    private final JLabel orderIdLabel = new JLabel();
    private JPanel invoicePanel;

    public OrderScreen(MainUI parent, Cart cart) {
        this.parent = parent;
        this.cart = cart;

        String[] columnNames = {"Product", "Qty", "Price", "Subtotal"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        cartItemsTable = new JTable(tableModel);
        cartItemsTable.setFont(Fonts.NORMAL_FONT);
        cartItemsTable.setForeground(UITheme.getTextColor());
        cartItemsTable.setBackground(UITheme.getSecondaryColor());
        cartItemsTable.setSelectionBackground(UITheme.getPrimaryColor()); 
        cartItemsTable.setGridColor(UITheme.getTertiaryColor().darker()); 
        cartItemsTable.setRowHeight(25); 

        cartItemsTable.getTableHeader().setFont(Fonts.BOLD_NFONT);
        cartItemsTable.getTableHeader().setForeground(UITheme.getTextColor());
        cartItemsTable.getTableHeader().setBackground(UITheme.getPrimaryColor().darker()); 
        cartItemsTable.getTableHeader().setReorderingAllowed(false); 
        cartItemsTable.getTableHeader().setResizingAllowed(false); 

        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBorder(new EmptyBorder(20, 20, 20, 20)); 

        initializeUI();
        refreshCartDetails();
    }

    private void initializeUI() {
        invoicePanel = new JPanel(new BorderLayout());
        invoicePanel.setBackground(UITheme.getSecondaryColor()); 
        invoicePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.getBorderColor(), 1), 
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel headerPanel = createHeaderPanel();
        invoicePanel.add(headerPanel, BorderLayout.NORTH);

        JPanel bodyPanel = createBodyPanel();
        invoicePanel.add(bodyPanel, BorderLayout.CENTER);

        JPanel footerPanel = createFooterPanel();
        invoicePanel.add(footerPanel, BorderLayout.SOUTH);

        JScrollPane screenScrollPane = new JScrollPane(invoicePanel);
        screenScrollPane.setBorder(null);
        screenScrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
        screenScrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar()); 
        add(screenScrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0)); 
        headerPanel.setOpaque(false); 

        JLabel title = new JLabel("INVOICE", SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT.deriveFont(Font.BOLD, 28f));
        title.setForeground(UITheme.getTertiaryColor().darker()); 

        JPanel storeInfoPanel = new JPanel(new GridLayout(0, 1, 0, 5)); 
        storeInfoPanel.setOpaque(false);

        JLabel storeName = new JLabel("My Store");
        storeName.setFont(Fonts.SUBTITLE_FONT);
        storeName.setForeground(UITheme.getTextColor());

        JLabel storeAddress = new JLabel("123 Main Street, City, Country");
        storeAddress.setFont(Fonts.NORMAL_FONT);
        storeAddress.setForeground(UITheme.getTextColor());

        JLabel storeContact = new JLabel("Phone: (123) 456-7890 | Email: info@mystore.com");
        storeContact.setFont(Fonts.NORMAL_FONT);
        storeContact.setForeground(UITheme.getTextColor());

        storeInfoPanel.add(storeName);
        storeInfoPanel.add(storeAddress);
        storeInfoPanel.add(storeContact);

        JPanel orderInfoPanel = new JPanel(new GridLayout(0, 1, 0, 5)); 
        orderInfoPanel.setOpaque(false);

        String orderId = "ORD-" + System.currentTimeMillis();
        String orderDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());

        orderIdLabel.setText("Order #: " + orderId);
        orderIdLabel.setFont(Fonts.NORMAL_FONT);
        orderIdLabel.setForeground(UITheme.getTextColor());
        orderIdLabel.setHorizontalAlignment(SwingConstants.RIGHT); 

        orderDateLabel.setText("Date: " + orderDate);
        orderDateLabel.setFont(Fonts.NORMAL_FONT);
        orderDateLabel.setForeground(UITheme.getTextColor());
        orderDateLabel.setHorizontalAlignment(SwingConstants.RIGHT); 

        orderInfoPanel.add(orderIdLabel);
        orderInfoPanel.add(orderDateLabel);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.add(title);

        headerPanel.add(titlePanel, BorderLayout.NORTH);
        headerPanel.add(storeInfoPanel, BorderLayout.WEST);
        headerPanel.add(orderInfoPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createBodyPanel() {
        JPanel bodyPanel = new JPanel(new BorderLayout(0, 20)); 
        bodyPanel.setOpaque(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Shipping Address:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2; // Ocupa dos columnas
        addressField.setBackground(UITheme.getBackgroundContrast());
        addressField.setForeground(UITheme.getTextColor());
        addressField.setCaretColor(UITheme.getTextColor()); // Color del cursor
        addressField.setBorder(BorderFactory.createLineBorder(UITheme.getBorderColor(), 1));
        addressField.setPreferredSize(new Dimension(300, 30)); // Altura fija para el campo
        formPanel.add(addressField, gbc);

        // Método de pago
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1; // Resetea a una columna
        formPanel.add(createLabel("Payment Method:"), gbc);

        gbc.gridx = 1;
        paymentMethodCombo.setBackground(UITheme.getBackgroundContrast());
        paymentMethodCombo.setForeground(UITheme.getTextColor());
        paymentMethodCombo.setBorder(BorderFactory.createLineBorder(UITheme.getBorderColor(), 1));
        paymentMethodCombo.setPreferredSize(new Dimension(200, 30)); // Altura fija
        // Si necesitas un renderer personalizado, implementa uno aquí.
        formPanel.add(paymentMethodCombo, gbc);

        bodyPanel.add(formPanel, BorderLayout.NORTH);

        // Detalles del carrito (centro del body) - Ahora usando JTable
        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.setOpaque(false);
        // itemsPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW)); // Debugging border

        JLabel orderDetailsLabel = createLabel("Order Details:");
        orderDetailsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Espacio inferior
        itemsPanel.add(orderDetailsLabel, BorderLayout.NORTH);

        JScrollPane itemsScrollPane = new JScrollPane(cartItemsTable);
        itemsScrollPane.setBorder(BorderFactory.createLineBorder(UITheme.getBorderColor(), 1)); // Borde para la tabla
        itemsScrollPane.getViewport().setBackground(UITheme.getBackgroundContrast()); // Fondo del viewport
        itemsScrollPane.getVerticalScrollBar().setUI(com.tiendajava.ui.utils.UIUtils.createDarkScrollBar()); // Estilo de scrollbar
        itemsPanel.add(itemsScrollPane, BorderLayout.CENTER);

        bodyPanel.add(itemsPanel, BorderLayout.CENTER);

        // Total (abajo del body)
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setOpaque(false);
        totalPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // Espacio superior

        totalLabel.setForeground(UITheme.getSuccessColor());
        totalLabel.setFont(Fonts.SUBTITLE_FONT.deriveFont(Font.BOLD, 20f)); // Tamaño de fuente un poco más grande
        totalPanel.add(totalLabel);

        bodyPanel.add(totalPanel, BorderLayout.SOUTH);

        return bodyPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        footerPanel.setBackground(UITheme.getPrimaryColor().darker()); // Un tono más oscuro para el pie de página
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        // footerPanel.setBorder(BorderFactory.createLineBorder(Color.ORANGE)); // Debugging border

        // Botón para confirmar orden
        JButton confirmOrderBtn = ButtonFactory.createPrimaryButton("Confirm Order",
            null, this::submitOrder);
        confirmOrderBtn.setPreferredSize(new Dimension(150, 40)); // Tamaño fijo

        // Botón para exportar a PDF
        JButton exportPdfBtn = ButtonFactory.createSecondaryButton("Export to PDF",
            null, this::exportToPDF);
        exportPdfBtn.setPreferredSize(new Dimension(150, 40)); // Tamaño fijo

        footerPanel.add(confirmOrderBtn);
        footerPanel.add(exportPdfBtn);

        return footerPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Fonts.NORMAL_FONT);
        label.setForeground(UITheme.getTextColor());
        label.setBackground(UITheme.getSecondaryColor());
        return label;
    }

    private void refreshCartDetails() {
        cart = parent.getCart();

        // Limpiar la tabla
        tableModel.setRowCount(0);

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("es", "CO"));

        if (cart.isEmpty()) {
            tableModel.addRow(new Object[]{"Your cart is empty.", "", "", ""});
            cartItemsTable.setEnabled(false); 
        } else {
            cartItemsTable.setEnabled(true);
            for (Product p : cart.getItems()) {
                BigDecimal subtotal = p.getPrice().multiply(BigDecimal.valueOf(p.getStock()));
                tableModel.addRow(new Object[]{
                    p.getName(),
                    p.getStock(),
                    currencyFormat.format(p.getPrice()),
                    currencyFormat.format(subtotal)
                });
            }
        }

        totalLabel.setText("Total: " + currencyFormat.format(cart.getTotalSubtotal()));
        revalidate();
        repaint();
    }

    private void submitOrder() {
        String address = addressField.getText().trim();
        String paymentMethod = (String) paymentMethodCombo.getSelectedItem();

        if (address.isEmpty()) {
            NotificationHandler.warning("Address is required.");
            return;
        }

        cart = parent.getCart();
        if (cart.isEmpty()) {
            NotificationHandler.warning("Your cart is empty.");
            return;
        }

        Order order = new Order();
        order.setShipping_address(address);
        order.setPayment_method(paymentMethod);
        order.setOrderItems(cart.toOrderItems());
        order.setTotal_amount(cart.getTotalSubtotal());

        var resp = orderService.createOrder(order);
        if (resp.isSuccess()) {
            NotificationHandler.success("Order placed successfully!");
            cart.clearCart(); 
            refreshCartDetails(); 
            addressField.setText(""); 
            orderIdLabel.setText("Order #: " + "ORD-" + System.currentTimeMillis());
            orderDateLabel.setText("Date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        } else {
            NotificationHandler.error("Failed to place order: " + resp.getMessage());
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
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        double scaleX = pageFormat.getImageableWidth() / invoicePanel.getWidth();
        double scaleY = pageFormat.getImageableHeight() / invoicePanel.getHeight();
        double scale = Math.min(scaleX, scaleY);
        g2d.scale(scale, scale);

        invoicePanel.printAll(g2d);

        return PAGE_EXISTS;
    }
}