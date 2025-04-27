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
import com.tiendajava.ui.components.dialogs.ConfirmationDialog;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils; 

public class EditProductDialog extends JDialog {

    private final JTextField nameField = new JTextField(20);
    private final JTextField priceField = new JTextField(20);
    private final JTextField stockField = new JTextField(20);
    private final JTextField categoryField = new JTextField(20);
    private final JTextField descriptionField = new JTextField(20);

    private final ProductService productService = new ProductService();
    private final Product productToEdit;
    private final Runnable onProductUpdated;

    public EditProductDialog(Product product, Runnable onProductUpdated) {
        this.productToEdit = product;
        this.onProductUpdated = onProductUpdated;

        setTitle("Edit Product");
        setModal(true);
        setSize(400, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.getPrimaryColor());

        buildForm();
        fillFields();
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

        JButton saveBtn = ButtonFactory.createPrimaryButton("Save Changes", null, this::saveProductChanges);
        panel.add(saveBtn, gbc);

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

    private void fillFields() {
        nameField.setText(productToEdit.getName());
        priceField.setText(String.valueOf(productToEdit.getPrice()));
        stockField.setText(String.valueOf(productToEdit.getStock()));
        categoryField.setText(String.valueOf(productToEdit.getCategory_id()));
        descriptionField.setText(productToEdit.getDescription());
    }

    private void saveProductChanges(){
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();
        String categoryIdText = categoryField.getText().trim();
        String description = descriptionField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty() || categoryIdText.isEmpty()) {
            NotificationHandler.warning("Please fill in all required fields.");
            return;
        }
        ConfirmationDialog confirmationDialog = new ConfirmationDialog("Confirmation", "Are you sure you want to update the product: " + productToEdit.getName() + "?", () -> {
            try {
                BigDecimal price = new BigDecimal(priceText);
                int stock = Integer.parseInt(stockText);
                int categoryId = Integer.parseInt(categoryIdText);
    
                productToEdit.setName(name);
                productToEdit.setPrice(price);
                productToEdit.setStock(stock);
                productToEdit.setCategory_id(categoryId);
                productToEdit.setDescription(description);
    
                ApiResponse<Product> response = productService.updateProduct(productToEdit);
    
                if (response.isSuccess()) {
                    NotificationHandler.success("Product updated successfully!");
                    dispose();
                    onProductUpdated.run();
                } else {
                    NotificationHandler.error("Failed to update product: " + response.getMessage());
                }
    
            } catch (NumberFormatException e) {
                NotificationHandler.error("Invalid number format. Check price, stock, and category ID.");
            }
        });
        confirmationDialog.setVisible(true);
    }
}
