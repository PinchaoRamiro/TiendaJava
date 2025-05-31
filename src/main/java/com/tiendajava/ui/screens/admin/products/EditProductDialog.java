package com.tiendajava.ui.screens.admin.products;

import java.awt.BorderLayout;
import java.math.BigDecimal; 

import javax.swing.JButton;
import javax.swing.JPanel; 
import javax.swing.SwingUtilities;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.ui.components.ButtonFactory; 
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.components.dialogs.ConfirmationDialog;
import com.tiendajava.ui.utils.UITheme;

public class EditProductDialog extends IProductDialog {
    private JPanel actionButtonPanel;

    public EditProductDialog(Product product, String category, Runnable onRunnable) {
        super(product, category, onRunnable);
    }

    @Override
    protected String getDialogTitle() {
        return "Edit Product: " + (product != null ? product.getName() : "");
    }

    @Override
    protected void buildForm() {
        super.buildForm();
        if (actionButtonPanel == null) {
            actionButtonPanel = new JPanel(new BorderLayout());
            actionButtonPanel.setBackground(UITheme.getPrimaryColor());
            JButton saveBtn = ButtonFactory.createPrimaryButton("Save Changes", null, () -> onSave()); // Llama a onSave()
            actionButtonPanel.add(saveBtn, BorderLayout.CENTER);

            add(actionButtonPanel, BorderLayout.SOUTH);
        }
    }

    @Override
    protected void populateFields() {
        super.populateFields(); 
    }

    @Override
    protected void onSave() {
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();
        String description = descriptionField.getText().trim();

        if ( priceText.isEmpty() || stockText.isEmpty() || description.isEmpty()) {
            NotificationHandler.warning("Please fill in all required fields (Name, Price, Stock, Description).");
            return;
        }

        ConfirmationDialog confirmationDialog = new ConfirmationDialog("Confirmation", UITheme.getButtonColor(),
                "Are you sure you want to update the product: " + product.getName() + "?", () -> {
            try {
                BigDecimal price = new BigDecimal(priceText);
                int stock = Integer.parseInt(stockText);

                product.setPrice(price);
                product.setStock(stock);
                product.setDescription(description);
                new Thread(() -> {
                    ApiResponse<Product> response;
                    if (imageFile != null) {
                        String lowerCaseCategoryName = category.getCategory_name() != null ? category.getCategory_name().toLowerCase() : null;
                        response = productService.createProductWithImage(product, imageFile, lowerCaseCategoryName);
                    } else {
                        response = productService.updateProduct(product);
                    }

                    SwingUtilities.invokeLater(() -> { 
                        if (response.isSuccess()) {
                            NotificationHandler.success("Product updated successfully!");
                            dispose(); 
                            if (onRunnable != null) {
                                onRunnable.run(); 
                            }
                        } else {
                            NotificationHandler.error("Failed to update product: " + response.getMessage());
                        }
                    });
                }).start();

            } catch (NumberFormatException e) {
                NotificationHandler.error("Invalid number format. Please check Price and Stock.");
            }
        });
        confirmationDialog.setVisible(true);
    }
}