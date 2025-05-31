package com.tiendajava.ui.screens.user.cart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.tiendajava.model.Cart;
import com.tiendajava.model.Product;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class AddCartDialog extends JDialog {

    private final JSpinner quantitySpinner;
    private final JLabel totalLabel;
    private final JLabel stockLabel;
    private final Cart cart;
    private final Product product;
    private final DecimalFormat priceFormat = new DecimalFormat("$#,##0.00");

    public AddCartDialog(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;

        setTitle("Add to Cart");
        setModal(true);
        setSize(400, 650); 
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true); 
        getRootPane().setBorder(BorderFactory.createLineBorder(UITheme.getSecondaryColor().brighter(), 2));

        getContentPane().setBackground(UITheme.getPrimaryColor());

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, product.getStock(), 1);
        quantitySpinner = new JSpinner(spinnerModel);
        
        totalLabel = new JLabel();
        stockLabel = new JLabel();

        setupUI();
        updateTotalPrice();
        
        quantitySpinner.addChangeListener(e -> updateTotalPrice());
        
        SwingUtilities.invokeLater(() -> getRootPane().setDefaultButton(getAddButton()));
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.getPrimaryColor());
        mainPanel.setBorder(new EmptyBorder(15, 25, 15, 25)); 

        JPanel headerPanel = createHeaderPanel();
        JPanel centerPanel = createCenterPanel();
        JPanel bottomPanel = createBottomPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(new EmptyBorder(0, 0, 15, 0)); 

        JLabel closeButton = ButtonFactory.createIconButton(
            AppIcons.CANCEL_ICON,
            "Close",
            this::dispose
        );

        panel.add(closeButton, BorderLayout.EAST);

        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setBackground(UITheme.getPrimaryColor());

        JLabel imageLabel = new JLabel(product.getName().substring(0, 1), SwingConstants.CENTER);
        imageLabel.setFont(Fonts.TITLE_FONT.deriveFont(48f));
        imageLabel.setForeground(Color.WHITE);
        imageLabel.setBackground(UITheme.getAccentColor());
        imageLabel.setOpaque(true);
        imageLabel.setPreferredSize(new Dimension(80, 80));
        imageLabel.setBorder(BorderFactory.createLineBorder(UITheme.getTertiaryColor(), 2));

        JPanel imageContainer = new JPanel(new GridBagLayout());
        imageContainer.setBackground(UITheme.getPrimaryColor());
        imageContainer.add(imageLabel);
        
        JLabel titleLabel = new JLabel("Add Product to Cart");
        titleLabel.setFont(Fonts.TITLE_FONT.deriveFont(26f)); 
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel productNameLabel = new JLabel(product.getName());
        productNameLabel.setFont(Fonts.SUBTITLE_FONT.deriveFont(20f)); 
        productNameLabel.setForeground(UITheme.getAccentColor());
        productNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleContainer.add(imageContainer);
        titleContainer.add(Box.createVerticalStrut(5));
        titleContainer.add(titleLabel);
        titleContainer.add(Box.createVerticalStrut(8)); 
        titleContainer.add(productNameLabel);
        
        panel.add(titleContainer, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.getPrimaryColor());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        JPanel productInfoPanel = createProductInfoPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(productInfoPanel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(10, 8, 10, 8);
        panel.add(createSeparator(), gbc);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(Fonts.NORMAL_FONT);
        quantityLabel.setForeground(UITheme.getTextColor());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(quantityLabel, gbc);

        setupSpinnerStyle();
        gbc.gridx = 1;
        gbc.weightx = 0.7; 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        panel.add(quantitySpinner, gbc);

        stockLabel.setFont(Fonts.SMALL_FONT.deriveFont(13f)); 
        stockLabel.setForeground(UITheme.getInfoColor()); 
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 8, 8, 8); 
        panel.add(stockLabel, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(15, 8, 8, 8);
        panel.add(createTotalPanel(), gbc);

        return panel;
    }

    private JPanel createProductInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout()); 
        panel.setBackground(UITheme.getSecondaryColor());
        panel.setBorder(new CompoundBorder(
            new LineBorder(UITheme.getTertiaryColor(), 1), 
            new EmptyBorder(12, 15, 12, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0; 

        JLabel priceLabel = new JLabel("Unit price: " + priceFormat.format(product.getPrice()));
        priceLabel.setFont(Fonts.SUBTITLE_FONT); 
        priceLabel.setForeground(UITheme.getSuccessColor().brighter()); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(priceLabel, gbc);

        JLabel categoryLabel = new JLabel("Category: " + product.getCategory().getCategory_name());
        categoryLabel.setFont(Fonts.NORMAL_FONT); 
        categoryLabel.setForeground(UITheme.getTextColor());
        gbc.gridy = 1;
        panel.add(categoryLabel, gbc);

        if (product.getDescription() != null && !product.getDescription().trim().isEmpty()) {
            JLabel descLabel = new JLabel("<html>" + product.getDescription() + "</html>");
            descLabel.setFont(Fonts.SMALL_FONT);
            descLabel.setForeground(UITheme.getTextColor());
            descLabel.setVerticalAlignment(SwingConstants.TOP); 
            gbc.gridy = 2;
            gbc.insets = new Insets(8, 0, 2, 0);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weighty = 1.0; 
            panel.add(descLabel, gbc);
        }

        return panel;
    }

    private JPanel createTotalPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.getAccentColor()); 
        panel.setBorder(new CompoundBorder(
            new LineBorder(UITheme.getAccentColor().brighter(), 2),
            new EmptyBorder(12, 18, 12, 18)
        ));

        JLabel totalTitleLabel = new JLabel("💰 Total:");
        totalTitleLabel.setFont(Fonts.SUBTITLE_FONT);
        totalTitleLabel.setForeground(UITheme.getTextColor());

        totalLabel.setFont(Fonts.TITLE_FONT.deriveFont(28f)); 
        totalLabel.setForeground(Color.WHITE); 
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(totalTitleLabel, BorderLayout.WEST);
        panel.add(totalLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createSeparator() {
        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(0, 1));
        separator.setBackground(UITheme.getTertiaryColor()); 
        return separator;
    }

    private void setupSpinnerStyle() {
        quantitySpinner.setFont(Fonts.NORMAL_FONT);
        quantitySpinner.setPreferredSize(new Dimension(100, 35)); 
        quantitySpinner.setBackground(UITheme.getSecondaryColor());
        quantitySpinner.setForeground(UITheme.getTextColor());

        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) quantitySpinner.getEditor();
        JTextField textField = editor.getTextField();
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setFont(Fonts.NORMAL_FONT);
        textField.setBackground(UITheme.getSecondaryColor());
        textField.setForeground(UITheme.getTextColor());
        textField.setBorder(new LineBorder(UITheme.getTertiaryColor(), 1)); 
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateTotalPrice();
            }
        });
        
        for (Component comp : quantitySpinner.getComponents()) {
            if (comp instanceof JButton b) {
                b.setBackground(UITheme.getTertiaryColor());
                b.setForeground(UITheme.getTextColor());
                b.setBorder(BorderFactory.createLineBorder(UITheme.getPrimaryColor().darker(), 1));
                b.setFocusPainted(false);
            }
        }
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(new EmptyBorder(25, 0, 0, 0));


        JButton addButton = getAddButton();
        addButton.setPreferredSize(new Dimension(150, 40)); 
        addButton.setFont(Fonts.NORMAL_FONT.deriveFont(14f));
        addButton.setIcon(AppIcons.CART_ICON);
        panel.add(addButton);

        return panel;
    }

    private JButton getAddButton() {
        return ButtonFactory.createPrimaryButton("Add to Cart", AppIcons.CART_ICON, this::addToCart);
    }

    private void updateTotalPrice() {
        try {
            int quantity = (Integer) quantitySpinner.getValue();
            double total = product.getPrice().doubleValue() * quantity;
            totalLabel.setText(priceFormat.format(total));
            
            // Update stock information
            int remaining = product.getStock() - quantity;
            if (remaining <= 5 && remaining > 0) {
                stockLabel.setText("⚠️ Only " + remaining + " units will remain");
                stockLabel.setForeground(UITheme.getWarningColor());
            } else if (remaining <= 0) {
                stockLabel.setText("⛔ Out of Stock for selected quantity");
                stockLabel.setForeground(UITheme.getDangerColor());
            }
            else {
                stockLabel.setText("✅ Available: " + product.getStock() + " units");
                stockLabel.setForeground(UITheme.getInfoColor());
            }
        } catch (NumberFormatException e) {
            totalLabel.setText("$0.00");
            stockLabel.setText("Invalid quantity entered");
            stockLabel.setForeground(UITheme.getDangerColor());
        } catch (Exception e) {
            totalLabel.setText("$0.00");
            stockLabel.setText("Error calculating price");
            stockLabel.setForeground(UITheme.getDangerColor());
        }
    }

    private void addToCart() {
        try {
            int qty = (Integer) quantitySpinner.getValue();
            
            if (qty <= 0) {
                NotificationHandler.warning("Quantity must be greater than 0.");
                return;
            }
            
            if (qty > product.getStock()) {
                NotificationHandler.warning("Quantity exceeds available stock (" + 
                                             product.getStock() + " units).");
                return;
            }
            
            cart.addItem(product, qty);
            
            String message = String.format("✅ %d %s added to cart\nTotal: %s", 
                                            qty, 
                                            qty == 1 ? "item" : "items",
                                            priceFormat.format(product.getPrice().doubleValue() * qty));
            
            NotificationHandler.success(message);
            dispose();
            
        } catch (Exception e) {
            NotificationHandler.error("Error adding product to cart.");
        }
    }
}