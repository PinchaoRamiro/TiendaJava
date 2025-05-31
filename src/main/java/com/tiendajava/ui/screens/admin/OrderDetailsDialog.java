package com.tiendajava.ui.screens.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.model.User;
import com.tiendajava.model.orders.Order;
import com.tiendajava.model.orders.OrderItem;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class OrderDetailsDialog extends JDialog {

    private final Order order;
    private final User user;
    private final ProductService productService = new ProductService();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public OrderDetailsDialog(JFrame parent, Order order, User user) {
        super(parent, "Order #" + order.getOrder_id(), true);
        this.order = order;
        this.user = user;

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.getPrimaryColor());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.getSecondaryColor());
        headerPanel.setBorder(new EmptyBorder(20, 20, 40, 20));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Order Details");
        titleLabel.setFont(Fonts.TITLE_FONT.deriveFont(Font.BOLD, 24));
        titleLabel.setForeground(UITheme.getTextColor());
        titlePanel.add(titleLabel);

        JLabel closeButton = ButtonFactory.createIconButton(
            AppIcons.CANCEL_ICON,
            "Close",
            this::dispose
        );

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(closeButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UITheme.getPrimaryColor());

        JPanel infoPanel = new JPanel(new BorderLayout(0, 20));
        infoPanel.setBackground(UITheme.getPrimaryColor());
        infoPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        infoPanel.add(createOrderInfoPanel(), BorderLayout.NORTH);
        infoPanel.add(createCustomerInfoPanel(), BorderLayout.CENTER);
        infoPanel.add(createItemsPanel(), BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getViewport().setBackground(UITheme.getPrimaryColor());

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        return contentPanel;
    }

    private JPanel createOrderInfoPanel() {
        JPanel orderInfoPanel = new JPanel(new GridBagLayout());
        orderInfoPanel.setBackground(UITheme.getSecondaryColor());
        orderInfoPanel.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, UITheme.getBorderColor()),
            new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel sectionTitle = new JLabel("Order Information");
        sectionTitle.setFont(Fonts.SUBTITLE_FONT.deriveFont(Font.BOLD, 18));
        sectionTitle.setForeground(UITheme.getTextColor());
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        orderInfoPanel.add(sectionTitle, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        orderInfoPanel.add(createInfoLabel("Order ID: ", String.valueOf(order.getOrder_id())), gbc);

        gbc.gridx = 1;
        orderInfoPanel.add(createInfoLabel("Date: ", formatDate(order.getCreatedAt())), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        orderInfoPanel.add(createInfoLabel("Status: ", order.getStatus().toString()), gbc);

        gbc.gridx = 1;
        orderInfoPanel.add(createInfoLabel("Total: ", currencyFormat.format(order.getTotal_amount())), gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        orderInfoPanel.add(createInfoLabel("Payment Method: ", order.getPayment_method()), gbc);

        gbc.gridy++;
        orderInfoPanel.add(createInfoLabel("Shipping Address: ", order.getShipping_address()), gbc);

        return orderInfoPanel;
    }

    private JPanel createCustomerInfoPanel() {
        JPanel customerInfoPanel = new JPanel(new GridBagLayout());
        customerInfoPanel.setBackground(UITheme.getSecondaryColor());
        customerInfoPanel.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, UITheme.getBorderColor()),
            new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel sectionTitle = new JLabel("Customer Information ");
        sectionTitle.setFont(Fonts.SUBTITLE_FONT.deriveFont(Font.BOLD, 18));
        sectionTitle.setForeground(UITheme.getTextColor());
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        customerInfoPanel.add(sectionTitle, gbc);

        if (user != null) {
            gbc.gridy++;
            gbc.gridwidth = 1;
            customerInfoPanel.add(createInfoLabel("Name: ", user.getName() + " " + user.getLastName()), gbc);

            gbc.gridx = 1;
            customerInfoPanel.add(createInfoLabel("Email: ", user.getEmail()), gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            customerInfoPanel.add(createInfoLabel("Phone: ", user.getPhone()), gbc);
        } else {
            gbc.gridy++;
            gbc.gridwidth = 2;
            customerInfoPanel.add(createInfoLabel("Customer:", "Not found"), gbc);
        }

        return customerInfoPanel;
    }

    private JPanel createItemsPanel() {
        JPanel itemsPanel = new JPanel(new BorderLayout());
        itemsPanel.setBackground(UITheme.getSecondaryColor());
        itemsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel sectionTitle = new JLabel("Order Items");
        sectionTitle.setFont(Fonts.SUBTITLE_FONT.deriveFont(Font.BOLD, 18));
        sectionTitle.setForeground(UITheme.getTextColor());
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));

        itemsPanel.add(sectionTitle, BorderLayout.NORTH);

        JPanel itemsContentPanel = new JPanel();
        itemsContentPanel.setLayout(new BoxLayout(itemsContentPanel, BoxLayout.Y_AXIS));
        itemsContentPanel.setOpaque(false);

        JPanel headerPanel = new JPanel(new GridLayout(1, 4));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        headerPanel.add(createHeaderLabel("Product"));
        headerPanel.add(createHeaderLabel("Price"));
        headerPanel.add(createHeaderLabel("Quantity"));
        headerPanel.add(createHeaderLabel("Subtotal"));

        itemsContentPanel.add(headerPanel);

        double total = 0;
        List<OrderItem> items = order.getOrderItems();
        if (items == null || items.isEmpty()) {
            itemsContentPanel.add(createInfoLabel("Items:", "No items in this order"));
        } else {
            for (OrderItem item : items) {
                ApiResponse<Product> productResponse = productService.getProductById(item.getProduct_id());
                Product product = productResponse.isSuccess() ? productResponse.getData() : null;

                JPanel itemPanel = new JPanel(new GridLayout(1, 4));
                itemPanel.setOpaque(false);
                itemPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

                if (product != null) {
                    itemPanel.add(createItemLabel(product.getName()));
                    itemPanel.add(createItemLabel(currencyFormat.format(item.getPrice())));
                    itemPanel.add(createItemLabel(String.valueOf(item.getQuantity())));
                    itemPanel.add(createItemLabel(currencyFormat.format(item.getSubtotal())));
                    total += item.getSubtotal().doubleValue();
                } else {
                    itemPanel.add(createItemLabel("Product ID: " + item.getProduct_id()));
                    itemPanel.add(createItemLabel("N/A"));
                    itemPanel.add(createItemLabel(String.valueOf(item.getQuantity())));
                    itemPanel.add(createItemLabel("N/A"));
                }

                itemsContentPanel.add(itemPanel);
            }

            JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            totalPanel.setOpaque(false);
            totalPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

            JLabel totalLabel = new JLabel("Total: " + currencyFormat.format(total));
            totalLabel.setFont(Fonts.BOLD_NFONT.deriveFont(Font.BOLD, 16));
            totalLabel.setForeground(UITheme.getSuccessColor());

            totalPanel.add(totalLabel);
            itemsContentPanel.add(totalPanel);
        }

        itemsPanel.add(itemsContentPanel, BorderLayout.CENTER);
        return itemsPanel;
    }

    private JPanel createInfoLabel(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(2, 0, 2, 0));

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(Fonts.SMALL_FONT);
        labelComponent.setForeground(UITheme.getTextColor().darker());

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(Fonts.NORMAL_FONT);
        valueComponent.setForeground(UITheme.getTextColor());

        panel.add(labelComponent, BorderLayout.WEST);
        panel.add(valueComponent, BorderLayout.CENTER);

        return panel;
    }

    private JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(Fonts.BOLD_NFONT);
        label.setForeground(UITheme.getTextColor());
        label.setBorder(new EmptyBorder(5, 10, 5, 10));
        return label;
    }

    private JLabel createItemLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(Fonts.NORMAL_FONT);
        label.setForeground(UITheme.getTextColor());
        label.setBorder(new EmptyBorder(5, 10, 5, 10));
        return label;
    }

    private String formatDate(String dateString) {
        try {
            java.text.SimpleDateFormat inputFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            java.util.Date date = inputFormat.parse(dateString);
            java.text.SimpleDateFormat outputFormat = new java.text.SimpleDateFormat("MMM dd, yyyy hh:mm a");
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateString;
        }
    }
}
