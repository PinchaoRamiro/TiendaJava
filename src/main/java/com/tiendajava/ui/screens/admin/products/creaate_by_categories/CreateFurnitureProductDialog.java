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

    // Panel para los botones de acción, se agregará en BorderLayout.SOUTH del diálogo
    private JPanel actionButtonPanel;

    public CreateFurnitureProductDialog(String category, Runnable onProductCreated) {
        // Llama al constructor de la superclase IProductDialog
        // 'product' es null para creación
        super(null, category, onProductCreated);

        // No es necesario llamar a setTitle, setModal, setSize, setLocationRelativeTo,
        // getContentPane().setBackground aquí, ya se manejan en el constructor de IProductDialog.

        // Configurar el estilo del botón selectImageBtn si quieres que sea diferente.
        // selectImageBtn es un miembro de la superclase y ya está inicializado.
        // Solo cambiamos sus propiedades aquí, no lo reasignamos.
        // Asumiendo que UITheme no tiene getSecondaryButtonColor(), usamos el primario o un color fijo.
        selectImageBtn.setText("Select Image"); // Mantener texto original o cambiarlo
        selectImageBtn.setBackground(UITheme.getPrimaryButtonColor()); // O UITheme.getAccentColor(), o Color.LIGHT_GRAY, etc.

        // buildForm() es llamado automáticamente por IProductDialog.initializeCategoryAndUI()
        // después de que la categoría se haya cargado.
        // No es necesario llamarlo aquí de nuevo.
    }

    @Override
    protected String getDialogTitle() {
        return "Create New Furniture Product";
    }

    @Override
    protected void buildForm() {
        // Llama a la implementación de la superclase para construir los campos comunes
        super.buildForm(); // Esto asegurará que nameField, priceField, etc. se añadan y currentRow se actualice.

        // Añadir campos específicos para Muebles usando los campos de la superclase
        // addLabelAndField ahora usa formPanel y gbc de la superclase directamente.
        addLabelAndField(formPanel, gbc, "Dimensions", dimensionsField);
        addLabelAndField(formPanel, gbc, "Material", materialField);
        addLabelAndField(formPanel, gbc, "Wood Type", woodTypeField);

        // Inicializar y añadir el panel de botones de acción si no se ha hecho
        if (actionButtonPanel == null) {
            actionButtonPanel = new JPanel(new BorderLayout());
            actionButtonPanel.setBackground(UITheme.getPrimaryColor());
            JButton createBtn = ButtonFactory.createPrimaryButton("Create Product", null, () -> onSave());
            JButton cancelBtn = ButtonFactory.createSecondaryButton("Cancel", null, this::dispose);
            actionButtonPanel.add(cancelBtn, BorderLayout.WEST);
            actionButtonPanel.add(createBtn, BorderLayout.CENTER);
            add(actionButtonPanel, BorderLayout.SOUTH); // Añadir al diálogo
        }
    }

    @Override
    protected void onSave() {
        // Validar y obtener los datos de los campos
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

        // 'category' es un campo protegido de IProductDialog
        if (category == null) {
            NotificationHandler.error("Category data not loaded. Please try again.");
            return;
        }

        int categoryId = category.getCategory_id();

        // Crear el objeto FurnitureProduct
        Product newFurnitureProduct = new FurnitureProduct(
            name,
            description,
            priceValue,
            stockValue,
            categoryId,
            dimensions,
            material,
            woodType
        );

        // Ejecutar la operación de creación en un hilo secundario para no bloquear el EDT
        new Thread(() -> {
            // 'productService' es un campo protegido de IProductDialog
            ApiResponse<Product> resp = productService.createProductWithImage(newFurnitureProduct, imageFile, "furniture");
            SwingUtilities.invokeLater(() -> { // Volver al EDT para actualizar la UI
                if (resp.isSuccess()) {
                    NotificationHandler.success("Product created successfully!");
                    dispose(); // Cerrar el diálogo. 'dispose()' es un método de JDialog, accesible.
                    // 'onRunnable' es un campo protegido de IProductDialog
                    if (onRunnable != null) {
                        onRunnable.run(); // Ejecutar la acción de refresco de la pantalla padre
                    }
                } else {
                    NotificationHandler.error("Failed to create product: " + resp.getMessage());
                }
            });
        }).start();
    }
}