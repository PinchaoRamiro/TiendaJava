package com.tiendajava.ui.screens.admin.products.creaate_by_categories;

import java.awt.BorderLayout;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.model.ProductsModels.ElectronicsProduct;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.screens.admin.products.IProductDialog;
import com.tiendajava.ui.utils.UITheme;

public class CreateElectronicsProductDialog extends IProductDialog {

    private final JTextField brandField = new JTextField(20);
    private final JTextField voltageField = new JTextField(20);
    private final JTextField warrantyField = new JTextField(20);

    private JPanel actionButtonPanel;

    public CreateElectronicsProductDialog(String category) {
        super(null, category, null);

        selectImageBtn.setText("Choose Image");
        selectImageBtn.setBackground(UITheme.getPrimaryButtonColor());
    }

    @Override
    protected String getDialogTitle() {
        return "Create New Electronics Product";
    }

    @Override
    protected void buildForm() {
        super.buildForm();

        addLabelAndField(formPanel, gbc, "Brand", brandField);
        addLabelAndField(formPanel, gbc, "Voltage", voltageField);
        addLabelAndField(formPanel, gbc, "Warranty", warrantyField);

        if (actionButtonPanel == null) {
            actionButtonPanel = new JPanel(new BorderLayout());
            JButton createBtn = ButtonFactory.createPrimaryButton("Create Product", null, this::onSave);
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
        String brand = brandField.getText().trim();
        String voltage = voltageField.getText().trim();
        String warranty = warrantyField.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() ||
            description.isEmpty() || brand.isEmpty() || voltage.isEmpty() || warranty.isEmpty()) {
            NotificationHandler.error("Please fill all fields.");
            return;
        }

        try {
            BigDecimal price = new BigDecimal(priceStr);
            int stock = Integer.parseInt(stockStr);
            int categoryId = category.getCategory_id();

            super.product = new ElectronicsProduct(
                name,
                description,
                price,
                stock,
                categoryId,
                brand,
                voltage,
                warranty
            );

            new Thread(() -> {
                ApiResponse<Product> response = productService.createProductWithImage(super.product, imageFile, "electronic");
                System.out.println("Response: " + response);
                SwingUtilities.invokeLater(() -> {
                    if (response.isSuccess()) {
                        NotificationHandler.success("Product created successfully!");
                        // imprimir super product
                        System.out.println("Product + " + super.product);
                        dispose();
                        if (onRunnable != null) onRunnable.run();
                    } else {
                        NotificationHandler.error("Failed to create product: " + response.getMessage());
                        System.err.println("API Error: " + response.getMessage());
                    }
                });
            }).start();

        } catch (NumberFormatException ex) {
            NotificationHandler.error("Please enter valid numeric values for price and stock.");
        }
    }
}
