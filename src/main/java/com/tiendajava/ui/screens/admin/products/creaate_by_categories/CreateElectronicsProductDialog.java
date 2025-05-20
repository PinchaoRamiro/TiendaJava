package com.tiendajava.ui.screens.admin.products.creaate_by_categories;

import java.awt.BorderLayout;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.model.ProductsModels.ElectronicsProduct; // Corregido: Importar ElectronicsProduct
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.screens.admin.products.IProductDialog;
import com.tiendajava.ui.utils.UITheme;

public class CreateElectronicsProductDialog extends IProductDialog {

    private final JTextField brandField = new JTextField(20);
    private final JTextField voltageField = new JTextField(20);
    private final JTextField warrantyField = new JTextField(20);

    private JPanel actionButtonPanel; // Panel para los botones de acción

    public CreateElectronicsProductDialog(String category, Runnable onProductCreated) {
        super(null, category, onProductCreated);

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

        // Añadir campos específicos para Electrónica
        addLabelAndField(formPanel, gbc, "Brand", brandField);
        addLabelAndField(formPanel, gbc, "Voltage", voltageField);
        addLabelAndField(formPanel, gbc, "Warranty", warrantyField);

        // Configurar y añadir los botones de acción al diálogo principal (BorderLayout.SOUTH)
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
    protected void onSave() { // Renombrado de createProduct() a onSave()
        String name = nameField.getText().trim();
        String priceStr = priceField.getText().trim();
        String stockStr = stockField.getText().trim();
        String description = descriptionField.getText().trim();
        String brand = brandField.getText().trim();
        String voltage = voltageField.getText().trim();
        String warranty = warrantyField.getText().trim();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || description.isEmpty() || brand.isEmpty() || voltage.isEmpty() || warranty.isEmpty()) {
            NotificationHandler.error("Please fill all fields.");
            return;
        }

        BigDecimal priceValue;
        int stockValue;
        try {
            priceValue = new BigDecimal(priceStr);
            stockValue = Integer.parseInt(stockStr);
        } catch (NumberFormatException ex) {
            NotificationHandler.error("Price, Stock, Voltage (if numeric) and Warranty (if numeric) must be valid numbers.");
            return;
        }

        if (category == null) {
            NotificationHandler.error("Category data not loaded. Please try again.");
            return;
        }

        int categoryId = category.getCategory_id();

        // Corregido: Crear ElectronicsProduct en lugar de ClothingProduct
        Product newElectronicsProduct = new ElectronicsProduct(
                name,
                description,
                priceValue,
                stockValue,
                categoryId, // Usar categoryId
                brand,
                voltage,
                warranty
        );

        // Ejecutar la operación de creación en un hilo secundario para no bloquear el EDT
        new Thread(() -> {
            ApiResponse<Product> resp = productService.createProductWithImage(newElectronicsProduct, imageFile, "electronics");
            SwingUtilities.invokeLater(() -> { // Volver al EDT para actualizar la UI
                if (resp.isSuccess()) {
                    NotificationHandler.success("Product created successfully!");
                    dispose(); // Cerrar el diálogo
                    if (onRunnable != null) {
                        onRunnable.run(); // Ejecutar la acción de refresco
                    }
                } else {
                    NotificationHandler.error("Failed to create product: " + resp.getMessage());
                    System.out.println("Error: " + resp.getMessage());
                }
            });
        }).start();
    }
}