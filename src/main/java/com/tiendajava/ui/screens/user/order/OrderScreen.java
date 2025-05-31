package com.tiendajava.ui.screens.user.order;

import java.awt.BorderLayout;
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
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.text.NumberFormat;
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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.tiendajava.model.Cart;
import com.tiendajava.model.Product;
import com.tiendajava.model.orders.Order;
import com.tiendajava.service.OrderService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.components.dialogs.ConfirmationDialog;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class OrderScreen extends JPanel implements Printable {

  private final OrderService orderService = new OrderService();
  private Cart cart;
  private final MainUI parent;
  private final JTextField addressField = new JTextField(30);
  private final JComboBox<String> paymentMethodCombo = new JComboBox<>(
      new String[] { "Credit Card", "PayPal", "Cash on Delivery" });
  private final JTable cartItemsTable;
  private final DefaultTableModel tableModel;
  private final JLabel totalLabel = new JLabel();
  private JPanel invoicePanel;
  private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("es", "CO"));

  public OrderScreen(MainUI parent, Cart cart) {
    this.parent = parent;
    this.cart = cart;

    // Configuración del modelo de la tabla
    String[] columnNames = { "Product", "Qty", "Price", "Subtotal" };
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    cartItemsTable = new JTable(tableModel);
    styleTable();

    setLayout(new BorderLayout());
    setBackground(UITheme.getPrimaryColor());
    setBorder(new EmptyBorder(20, 20, 20, 20));

    initializeUI();
    refreshCartDetails();
  }

  private void initializeUI() {
    // Crear el panel principal de la factura
    invoicePanel = new JPanel(new BorderLayout());
    invoicePanel.setBackground(UITheme.getSecondaryColor());
    invoicePanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(UITheme.getBorderColor(), 1),
        BorderFactory.createEmptyBorder(20, 20, 20, 20)));

    // Añadir componentes
    invoicePanel.add(createHeaderPanel(), BorderLayout.NORTH);
    invoicePanel.add(createBodyPanel(), BorderLayout.CENTER);
    invoicePanel.add(createFooterPanel(), BorderLayout.SOUTH);

    // Añadir scroll al panel principal
    JScrollPane scrollPane = new JScrollPane(invoicePanel);
    scrollPane.setBorder(null);
    scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());
    scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
    add(scrollPane, BorderLayout.CENTER);
  }

  private JPanel createHeaderPanel() {
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setOpaque(false);

    JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    titlePanel.setOpaque(false);

    JLabel logoLabel = new JLabel(AppIcons.BOX_ICON);
    logoLabel.setFont(Fonts.TITLE_FONT.deriveFont(30f));

    JLabel titleLabel = new JLabel("Order Details");
    titleLabel.setFont(Fonts.TITLE_FONT.deriveFont(Font.BOLD, 24));
    titleLabel.setForeground(UITheme.getTextColor());

    titlePanel.add(logoLabel);
    titlePanel.add(titleLabel);

    JPanel orderInfoPanel = new JPanel(new GridLayout(0, 1, 0, 5));
    orderInfoPanel.setOpaque(false);

    String orderId = "ORD-" + System.currentTimeMillis();
    String orderDate = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());

    JLabel orderIdLabel = new JLabel("Order #: " + orderId);
    orderIdLabel.setFont(Fonts.NORMAL_FONT);
    orderIdLabel.setForeground(UITheme.getTextColor());
    orderIdLabel.setHorizontalAlignment(SwingConstants.RIGHT);

    JLabel orderDateLabel = new JLabel("Date: " + orderDate);
    orderDateLabel.setFont(Fonts.NORMAL_FONT);
    orderDateLabel.setForeground(UITheme.getTextColor());
    orderDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

    orderInfoPanel.add(orderIdLabel);
    orderInfoPanel.add(orderDateLabel);

    headerPanel.add(titlePanel, BorderLayout.WEST);
    headerPanel.add(orderInfoPanel, BorderLayout.EAST);

    return headerPanel;
  }

  private JPanel createBodyPanel() {
    JPanel bodyPanel = new JPanel(new BorderLayout(20, 20));
    bodyPanel.setOpaque(false);

    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setOpaque(false);
    formPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 5, 8, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    gbc.gridx = 0;
    gbc.gridy = 0;
    formPanel.add(createLabel("Shipping Address:"), gbc);

    gbc.gridx = 1;
    gbc.gridwidth = 2;
    addressField.setBackground(UITheme.getBackgroundContrast());
    addressField.setForeground(UITheme.getTextColor());
    addressField.setBorder(BorderFactory.createLineBorder(UITheme.getBorderColor(), 1));
    addressField.setPreferredSize(new Dimension(300, 30));
    formPanel.add(addressField, gbc);

    gbc.gridx = 0;
    gbc.gridy++;
    gbc.gridwidth = 1;
    formPanel.add(createLabel("Payment Method:"), gbc);

    gbc.gridx = 1;
    paymentMethodCombo.setBackground(UITheme.getBackgroundContrast());
    paymentMethodCombo.setForeground(UITheme.getTextColor());
    paymentMethodCombo.setBorder(BorderFactory.createLineBorder(UITheme.getBorderColor(), 1));
    paymentMethodCombo.setPreferredSize(new Dimension(200, 30));
    formPanel.add(paymentMethodCombo, gbc);

    JPanel itemsPanel = new JPanel(new BorderLayout());
    itemsPanel.setOpaque(false);
    itemsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

    JLabel itemsTitle = createLabel("Order Items:");
    itemsTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
    itemsPanel.add(itemsTitle, BorderLayout.NORTH);

    JScrollPane itemsScrollPane = new JScrollPane(cartItemsTable);
    itemsScrollPane.setBorder(BorderFactory.createLineBorder(UITheme.getBorderColor(), 1));
    itemsScrollPane.getViewport().setBackground(UITheme.getBackgroundContrast());
    itemsScrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
    itemsPanel.add(itemsScrollPane, BorderLayout.CENTER);

    JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    totalPanel.setOpaque(false);
    totalPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

    totalLabel.setForeground(UITheme.getSuccessColor());
    totalLabel.setFont(Fonts.SUBTITLE_FONT.deriveFont(Font.BOLD, 20));
    totalPanel.add(totalLabel);

    itemsPanel.add(totalPanel, BorderLayout.SOUTH);

    bodyPanel.add(formPanel, BorderLayout.NORTH);
    bodyPanel.add(itemsPanel, BorderLayout.CENTER);

    return bodyPanel;
  }

  private JPanel createFooterPanel() {
    JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
    footerPanel.setBackground(UITheme.getPrimaryColor().darker());
    footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

    JButton confirmOrderBtn = ButtonFactory.createPrimaryButton("Confirm Order",
        null, this::submitOrder);
    confirmOrderBtn.setPreferredSize(new Dimension(150, 40));

    JButton exportPdfBtn = ButtonFactory.createSecondaryButton("Export to PDF",
        null, this::exportToPDF);
    exportPdfBtn.setPreferredSize(new Dimension(150, 40));

    footerPanel.add(exportPdfBtn);
    footerPanel.add(confirmOrderBtn);

    return footerPanel;
  }

  private JLabel createLabel(String text) {
    JLabel label = new JLabel(text);
    label.setFont(Fonts.NORMAL_FONT);
    label.setForeground(UITheme.getTextColor());
    return label;
  }

  private void styleTable() {
    cartItemsTable.setFont(Fonts.NORMAL_FONT);
    cartItemsTable.setForeground(UITheme.getTextColor());
    cartItemsTable.setBackground(UITheme.getBackgroundContrast());
    cartItemsTable.setSelectionBackground(UITheme.getPrimaryColor());
    cartItemsTable.setSelectionForeground(UITheme.getTextColor());
    cartItemsTable.setGridColor(UITheme.getBorderColor());
    cartItemsTable.setRowHeight(30);

    cartItemsTable.getTableHeader().setFont(Fonts.BOLD_NFONT);
    cartItemsTable.getTableHeader().setForeground(UITheme.getTextColor());
    cartItemsTable.getTableHeader().setBackground(UITheme.getPrimaryColor().darker());
    cartItemsTable.getTableHeader().setReorderingAllowed(false);
    cartItemsTable.getTableHeader().setResizingAllowed(false);

    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(new EmptyBorder(5, 5, 5, 5));

        if (isSelected) {
          setBackground(UITheme.getPrimaryColor());
          setForeground(UITheme.getTextColor());
        } else {
          setBackground(row % 2 == 0 ? UITheme.getSecondaryColor() : UITheme.getSecondaryColor().darker());
          setForeground(UITheme.getTextColor());
        }

        return this;
      }
    };

    for (int i = 0; i < cartItemsTable.getColumnCount(); i++) {
      cartItemsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
  }

  private void refreshCartDetails() {
    cart = parent.getCart();
    tableModel.setRowCount(0);

    if (cart.isEmpty()) {
      tableModel.addRow(new Object[] { "Your cart is empty.", "", "", "" });
      cartItemsTable.setEnabled(false);
    } else {
      cartItemsTable.setEnabled(true);
      for (Product p : cart.getItems()) {
        BigDecimal subtotal = p.getPrice().multiply(BigDecimal.valueOf(p.getStock()));
        tableModel.addRow(new Object[] {
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
      new ConfirmationDialog(
          "Do you want to print the invoice?",
          UITheme.getPrimaryButtonColor(),
          "Print",
          this::exportToPDF).setVisible(true);
      cart.clearCart();
      refreshCartDetails();
      addressField.setText("");
    } else {
      NotificationHandler.error("Failed to place order: " + resp.getMessage());
    }
  }

  private void exportToPDF() {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setPrintable(this);

    parent.showLoading("Exporting to PDF...");

    SwingUtilities.invokeLater(
        () -> {
          parent.hideLoading();
          if (job.printDialog()) {
            try {
              job.print();
            } catch (PrinterException e) {
              NotificationHandler.error("Error exporting to PDF: " + e.getMessage());
            }
          }
        });
  }

  @Override
  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
    if (pageIndex > 0) {
      return Printable.NO_SUCH_PAGE;
    }

    Graphics2D g2d = (Graphics2D) graphics;
    g2d.setColor(UITheme.getPrimaryColor());
    g2d.fillRect(
        (int) pageFormat.getImageableX(),
        (int) pageFormat.getImageableY(),
        (int) pageFormat.getImageableWidth(),
        (int) pageFormat.getImageableHeight());

    double scaleX = pageFormat.getImageableWidth() / invoicePanel.getWidth();
    double scaleY = pageFormat.getImageableHeight() / invoicePanel.getHeight();
    double scale = Math.min(scaleX, scaleY);

    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
    g2d.scale(scale, scale);
    int xOffset = (int) ((pageFormat.getImageableWidth() / scale - invoicePanel.getWidth()) / 2);
    int yOffset = (int) ((pageFormat.getImageableHeight() / scale - invoicePanel.getHeight()) / 2);
    g2d.translate(xOffset, yOffset);

    invoicePanel.printAll(g2d);

    return Printable.PAGE_EXISTS;
  }
}
