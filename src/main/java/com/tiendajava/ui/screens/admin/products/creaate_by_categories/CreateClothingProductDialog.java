package com.tiendajava.ui.screens.admin.products.creaate_by_categories;

import java.awt.BorderLayout;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities; // Necesario para SwingUtilities.invokeLater

import com.tiendajava.model.Product;
import com.tiendajava.model.ProductsModels.ClothingProduct;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.screens.admin.products.IProductDialog;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.utils.ApiResponse;

public class CreateClothingProductDialog extends IProductDialog {

    private Product productCreated;

    private final JTextField sizeField = new JTextField(20);
    private final JTextField colorField = new JTextField(20);
    private final JTextField materialField = new JTextField(20);

    private JPanel actionButtonPanel;

    public CreateClothingProductDialog(String category) {
        super(null, category, null);
        selectImageBtn.setText("Choose Image"); 
        selectImageBtn.setBackground(UITheme.getPrimaryButtonColor());
    }

    @Override
    protected String getDialogTitle() {
        return "Create New Clothing Product";
    }

    @Override
    public void buildForm() { 
        super.buildForm();

        addLabelAndField(formPanel, gbc, "Size", sizeField);
        addLabelAndField(formPanel, gbc, "Color", colorField);
        addLabelAndField(formPanel, gbc, "Material", materialField);

        if (actionButtonPanel == null) {
            actionButtonPanel = new JPanel(new BorderLayout());
            actionButtonPanel.setBackground(UITheme.getPrimaryColor());
            JButton createBtn = ButtonFactory.createPrimaryButton("Create Product", null, this::onSave); 
            JButton cancelBtn = ButtonFactory.createSecondaryButton("Cancel",null, this::dispose);
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
        String size = sizeField.getText().trim();
        String color = colorField.getText().trim();
        String material = materialField.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || description.isEmpty() || size.isEmpty() || color.isEmpty() || material.isEmpty()) {
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

        productCreated = new ClothingProduct(
                name,
                description,
                priceValue,
                stockValue,
                categoryId, 
                size,
                color,
                material
        );

        new Thread(() -> {
            ApiResponse<Product> resp = productService.createProductWithImage(productCreated, imageFile, "clothing");
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