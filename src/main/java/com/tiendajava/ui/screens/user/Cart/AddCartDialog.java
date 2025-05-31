package com.tiendajava.ui.screens.user.cart;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

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

        setTitle("Agregar al Carrito");
        setModal(true);
        setSize(420, 380);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UITheme.getPrimaryColor());

        // Configurar spinner con límites
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, product.getStock(), 1);
        quantitySpinner = new JSpinner(spinnerModel);
        
        // Labels para mostrar información calculada
        totalLabel = new JLabel();
        stockLabel = new JLabel();

        setupUI();
        updateTotalPrice();
        
        // Listener para actualizar precio total cuando cambie la cantidad
        quantitySpinner.addChangeListener(e -> updateTotalPrice());
        
        // Soporte para Enter key
        getRootPane().setDefaultButton(getAddButton());
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.getPrimaryColor());
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header con información del producto
        JPanel headerPanel = createHeaderPanel();
        
        // Panel central con controles
        JPanel centerPanel = createCenterPanel();
        
        // Panel inferior con botones
        JPanel bottomPanel = createBottomPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Icono y título
        JLabel iconLabel = new JLabel("🛒", SwingConstants.CENTER);
        iconLabel.setFont(Fonts.TITLE_FONT);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Agregar al Carrito");
        titleLabel.setFont(Fonts.TITLE_FONT);
        titleLabel.setForeground(UITheme.getTextColor());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Nombre del producto con estilo
        JLabel productNameLabel = new JLabel(product.getName());
        productNameLabel.setFont(Fonts.SUBTITLE_FONT);
        productNameLabel.setForeground(UITheme.getSuccessColor());
        productNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(iconLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(productNameLabel);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.getPrimaryColor());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Panel de información del producto
        JPanel productInfoPanel = createProductInfoPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(productInfoPanel, gbc);

        // Separador visual
        gbc.gridy = 1;
        gbc.insets = new Insets(15, 8, 15, 8);
        panel.add(createSeparator(), gbc);

        // Label "Cantidad"
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(Fonts.NORMAL_FONT);
        quantityLabel.setForeground(UITheme.getTextColor());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.NONE;
        panel.add(quantityLabel, gbc);

        // Spinner personalizado
        setupSpinnerStyle();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(quantitySpinner, gbc);

        // Stock disponible
        stockLabel.setText("Stock: " + product.getStock() + " units");
        stockLabel.setFont(Fonts.SMALL_FONT);
        stockLabel.setForeground(UITheme.getInfoColor());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(stockLabel, gbc);

        // Total a pagar
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 8, 8, 8);
        panel.add(createTotalPanel(), gbc);

        return panel;
    }

    private JPanel createProductInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.getSecondaryColor());
        panel.setBorder(new CompoundBorder(
            new LineBorder(UITheme.getSecondaryColor(), 1),
            new EmptyBorder(12, 15, 12, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 0);

        // Precio unitario
        JLabel priceLabel = new JLabel("Unit price: " + priceFormat.format(product.getPrice()));
        priceLabel.setFont(Fonts.NORMAL_FONT);
        priceLabel.setForeground(UITheme.getSuccessColor());
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(priceLabel, gbc);

        // Categoría
        JLabel categoryLabel = new JLabel("Caregory: " + product.getCategory().getCategory_name());
        categoryLabel.setFont(Fonts.SMALL_FONT);
        categoryLabel.setForeground(UITheme.getTextColor());
        gbc.gridy = 1;
        panel.add(categoryLabel, gbc);

        // Descripción (si existe)
        if (product.getDescription() != null && !product.getDescription().trim().isEmpty()) {
            JLabel descLabel = new JLabel("<html><div style='width: 250px;'>" + 
                                        product.getDescription() + "</div></html>");
            descLabel.setFont(Fonts.SMALL_FONT);
            descLabel.setForeground(UITheme.getTextColor());
            gbc.gridy = 2;
            gbc.insets = new Insets(8, 0, 2, 0);
            panel.add(descLabel, gbc);
        }

        return panel;
    }

    private JPanel createTotalPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.getWarningColor().darker());
        panel.setBorder(new CompoundBorder(
            new LineBorder(UITheme.getWarningColor(), 2),
            new EmptyBorder(10, 15, 10, 15)
        ));

        JLabel totalTitleLabel = new JLabel("💰 Total to pay:");
        totalTitleLabel.setFont(Fonts.NORMAL_FONT);
        totalTitleLabel.setForeground(UITheme.getTextColor());

        totalLabel.setFont(Fonts.SUBTITLE_FONT);
        totalLabel.setForeground(UITheme.getWarningColor());
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(totalTitleLabel, BorderLayout.WEST);
        panel.add(totalLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createSeparator() {
        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(0, 1));
        separator.setBackground(UITheme.getSecondaryColor().brighter());
        return separator;
    }

    private void setupSpinnerStyle() {
        quantitySpinner.setFont(Fonts.NORMAL_FONT);
        quantitySpinner.setPreferredSize(new Dimension(100, 30));
        
        // Personalizar el editor del spinner
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) quantitySpinner.getEditor();
        editor.getTextField().setHorizontalAlignment(JTextField.CENTER);
        editor.getTextField().setFont(Fonts.NORMAL_FONT);
        
        // Listener para validación en tiempo real
        editor.getTextField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateTotalPrice();
            }
        });
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton cancelButton = ButtonFactory.createSecondaryButton("Cancel", AppIcons.CANCEL_ICON, this::dispose);
        cancelButton.setPreferredSize(new Dimension(100, 35));

        JButton addButton = getAddButton();
        addButton.setPreferredSize(new Dimension(120, 35));

        panel.add(cancelButton);
        panel.add(addButton);

        return panel;
    }

    private JButton getAddButton() {
        return ButtonFactory.createPrimaryButton("🛒 Add", null, this::addToCart);
    }

    private void updateTotalPrice() {
        try {
            int quantity = (Integer) quantitySpinner.getValue();
            double total = product.getPrice().doubleValue() * quantity;
            totalLabel.setText(priceFormat.format(total));
            
            // Actualizar información de stock
            int remaining = product.getStock() - quantity;
            if (remaining < 5) {
                stockLabel.setText("⚠️ They would stay " + remaining + " units in stock");
                stockLabel.setForeground(UITheme.getWarningColor());
            } else {
                stockLabel.setText("✅ Disponible: " + product.getStock() + " units");
                stockLabel.setForeground(UITheme.getInfoColor());
            }
        } catch (Exception e) {
            totalLabel.setText("$0.00");
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
            
            // Mensaje de éxito más informativo
            String message = String.format("✅ %d %s added to cart\nTotal: %s", 
                                         qty, 
                                         qty == 1 ? "product" : "products",
                                         priceFormat.format(product.getPrice().doubleValue() * qty));
            
            NotificationHandler.success(message);
            dispose();
            
        } catch (Exception e) {
            NotificationHandler.error("Error to add product to cart.");
        }
    }
}