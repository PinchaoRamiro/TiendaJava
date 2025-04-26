package com.tiendajava.ui.screens.admin.products;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class CreateProductDialog extends JDialog {

    private final JTextField nameField = new JTextField(20);
    private final JTextField priceField = new JTextField(20);
    private final JTextField stockField = new JTextField(20);
    private final JTextField categoryField = new JTextField(20);
    private final JTextField descriptionField = new JTextField(20);

    private final ProductService productService = new ProductService();
    private final Runnable onProductCreated;

    public CreateProductDialog(Runnable onProductCreated) {
        this.onProductCreated = onProductCreated;
        setTitle("Create New Product");
        setModal(true);
        setSize(400, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.getPrimaryColor());

        buildForm();
    }

    private void buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(UIUtils.getDefaultPadding());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        addLabelAndField(panel, gbc, row++, "Name", nameField);
        addLabelAndField(panel, gbc, row++, "Price", priceField);
        addLabelAndField(panel, gbc, row++, "Stock", stockField);
        addLabelAndField(panel, gbc, row++, "Category ID", categoryField);
        addLabelAndField(panel, gbc, row++, "Description", descriptionField);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton createBtn = ButtonFactory.createPrimaryButton("Create", null, this::createProduct);
        panel.add(createBtn, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel label = new JLabel(labelText);
        label.setForeground(UITheme.getTextColor());
        label.setFont(Fonts.NORMAL_FONT);
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void createProduct() {
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();
        String categoryIdText = categoryField.getText().trim();
        String description = descriptionField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty() || categoryIdText.isEmpty()) {
            NotificationHandler.warning("Please fill in all required fields.");
            return;
        }

        try {
            BigDecimal price = new BigDecimal(priceText);
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                NotificationHandler.warning("Price must be greater than zero.");
                return;
            }
            int stock = Integer.parseInt(stockText);
            int categoryId = Integer.parseInt(categoryIdText);

            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
            product.setCategory_id(categoryId);
            product.setDescription(description);

            ApiResponse<Product> response = productService.createProduct(product);

            if (response.isSuccess()) {
                NotificationHandler.success("Product created successfully!");
                dispose();
                onProductCreated.run();
            } else {
                NotificationHandler.error("Failed to create product: " + response.getMessage());
            }

        } catch (NumberFormatException e) {
            NotificationHandler.error("Invalid number format. Check price, stock, and category ID.");
        }
    }
}
