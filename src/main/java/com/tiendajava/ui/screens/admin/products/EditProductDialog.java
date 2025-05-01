package com.tiendajava.ui.screens.admin.products;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Category;
import com.tiendajava.model.Product;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.components.dialogs.ConfirmationDialog;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils; 

public class EditProductDialog extends IProductDialog {

    public EditProductDialog(Product product, Runnable onRunnable) {
        super(product, onRunnable);
        buildForm();
    }

    private void buildForm() {
        UIUtils.styleComboBox(categoryComboBox);
        fillFields();
        setTitle("Edit Product: " + product.getName());
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
        addLabelAndField(panel, gbc, row++, "Category", categoryComboBox);
        addLabelAndField(panel, gbc, row++, "Description", descriptionField);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton saveBtn = ButtonFactory.createPrimaryButton("Save Changes", null, this::saveProductChanges);
        panel.add(saveBtn, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void fillFields() {
        nameField.setText(product.getName());
        priceField.setText(String.valueOf(product.getPrice()));
        stockField.setText(String.valueOf(product.getStock()));
        descriptionField.setText(product.getDescription());
    }

    private void saveProductChanges(){
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();
        String description = descriptionField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty() || categoryComboBox.getSelectedItem() == null) {
            NotificationHandler.warning("Please fill in all required fields.");
            return;
        }
        ConfirmationDialog confirmationDialog = new ConfirmationDialog("Confirmation", "Are you sure you want to update the product: " + product.getName() + "?", () -> {
            try {
                BigDecimal price = new BigDecimal(priceText);
                int stock = Integer.parseInt(stockText);
                Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
    
                product.setName(name);
                product.setPrice(price);
                product.setStock(stock);
                product.setCategory_id(selectedCategory.getCategory_id());
                product.setDescription(description);
    
                ApiResponse<Product> response = productService.updateProduct(product);
    
                if (response.isSuccess()) {
                    NotificationHandler.success("Product updated successfully!");
                    dispose();
                    onRunnable.run();
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
