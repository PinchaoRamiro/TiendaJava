package com.tiendajava.ui.screens.admin.products.creaate_by_categories;

import java.awt.BorderLayout; // Necesario para el nuevo panel de botones
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.model.ProductsModels.FurnitureProduct;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.screens.admin.products.IProductDialog;
import com.tiendajava.ui.utils.UITheme;

public class CreateFurnitureProductDialog extends IProductDialog {

    private final JTextField dimensionsField = new JTextField(20);
    private final JTextField materialField = new JTextField(20);
    private final JTextField woodTypeField = new JTextField(20);

    private JPanel actionButtonPanel;

    public CreateFurnitureProductDialog(String category) {
        super(null, category, null);
        selectImageBtn.setText("Select Image"); 
        selectImageBtn.setBackground(UITheme.getPrimaryButtonColor()); 
    }

    @Override
    protected String getDialogTitle() {
        return "Create New Furniture Product";
    }

    @Override
    protected void buildForm() {
        super.buildForm();
        addLabelAndField(formPanel, gbc, "Dimensions", dimensionsField);
        addLabelAndField(formPanel, gbc, "Material", materialField);
        addLabelAndField(formPanel, gbc, "Wood Type", woodTypeField);

        if (actionButtonPanel == null) {
            actionButtonPanel = new JPanel(new BorderLayout());
            actionButtonPanel.setBackground(UITheme.getPrimaryColor());
            JButton createBtn = ButtonFactory.createPrimaryButton("Create Product", null, () -> onSave());
            JButton cancelBtn = ButtonFactory.createSecondaryButton("Cancel", null, this::dispose);
            actionButtonPanel.add(cancelBtn, BorderLayout.WEST);
            actionButtonPanel.add(createBtn, BorderLayout.CENTER);
            add(actionButtonPanel, BorderLayout.SOUTH); 
        }
    }

    @Override
    protected void onSave() {
        String name = nameField.getText().trim();
        String priceStr = priceField.getText().trim();
        String stockStr = stockField.getText().trim();
        String description = descriptionField.getText().trim();
        String dimensions = dimensionsField.getText().trim();
        String material = materialField.getText().trim();
        String woodType = woodTypeField.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || description.isEmpty() ||
            dimensions.isEmpty() || material.isEmpty() || woodType.isEmpty()) {
            NotificationHandler.error("Please fill all fields.");
            return;
        }

        BigDecimal priceValue;
        int stockValue;
        try {
            priceValue = new BigDecimal(priceStr);
            stockValue = Integer.parseInt(stockStr);
        } catch (NumberFormatException ex) {
            NotificationHandler.error("Price and Stock must be valid numbers.");
            return;
        }

        if (category == null) {
            NotificationHandler.error("Category data not loaded. Please try again.");
            return;
        }

        int categoryId = category.getCategory_id();
        super.product = new FurnitureProduct(
            name,
            description,
            priceValue,
            stockValue,
            categoryId,
            dimensions,
            material,
            woodType
        );

        new Thread(() -> {
            ApiResponse<Product> resp = productService.createProductWithImage(super.product, imageFile, "furniture");
            SwingUtilities.invokeLater(() -> { 
                if (resp.isSuccess()) {
                    NotificationHandler.success("Product created successfully!");
                    dispose(); 
                    if (onRunnable != null) {
                        onRunnable.run();
                    }
                } else {
                    NotificationHandler.error("Failed to create product: " + resp.getMessage());
                }
            });
        }).start();
    }
}