package com.tiendajava.ui.screens.admin.products;

import java.awt.BorderLayout;
import java.math.BigDecimal; // No tan necesario si se usa solo super.buildForm() y sus addLabelAndField

import javax.swing.JButton; // No tan necesario si se usa solo super.buildForm() y sus addLabelAndField
import javax.swing.JPanel; // No tan necesario si se usa solo super.buildForm() y sus addLabelAndField
import javax.swing.SwingUtilities;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.ui.components.ButtonFactory; // Importar SwingUtilities para EDT
import com.tiendajava.ui.components.NotificationHandler;
import com.tiendajava.ui.components.dialogs.ConfirmationDialog;
import com.tiendajava.ui.utils.UITheme;

public class EditProductDialog extends IProductDialog {

    // Panel para los botones de acción (Save, Delete, etc.)
    private JPanel actionButtonPanel;

    public EditProductDialog(Product product, String category, Runnable onRunnable) {
        // Llama al constructor de la superclase IProductDialog
        // product ya se pasa para la edición
        super(product, category, onRunnable);
        // buildForm() ya se llama en el constructor de IProductDialog a través de initializeCategoryAndUI()
        // No es necesario llamarlo aquí de nuevo.
    }

    @Override
    protected String getDialogTitle() {
        return "Edit Product: " + (product != null ? product.getName() : "");
    }

    @Override
    protected void buildForm() {
        // Llama a la implementación de la superclase para construir los campos comunes
        // (nameField, priceField, stockField, descriptionField, imageField, selectImageBtn)
        super.buildForm();

        // Configurar el campo de nombre como no editable si no se espera que cambie
        // o editable si es parte de la edición
        // Si el nombre no debe ser editable, puedes configurarlo así:
        // nameField.setEditable(false);
        // Si el nombre debe ser editable, la llamada a super.buildForm() ya lo añade.

        // Configurar y añadir los botones de acción al diálogo principal (BorderLayout.SOUTH)
        if (actionButtonPanel == null) {
            actionButtonPanel = new JPanel(new BorderLayout());
            actionButtonPanel.setBackground(UITheme.getPrimaryColor());
            JButton saveBtn = ButtonFactory.createPrimaryButton("Save Changes", null, () -> onSave()); // Llama a onSave()
            actionButtonPanel.add(saveBtn, BorderLayout.CENTER); // Añadir el botón al centro del panel de acción

            // Si necesitas un botón de eliminar, puedes añadirlo aquí también
            // JButton deleteBtn = ButtonFactory.createDeleteButton("Delete Product", null, e -> onDelete());
            // actionButtonPanel.add(deleteBtn, BorderLayout.WEST); // Ejemplo de posición

            add(actionButtonPanel, BorderLayout.SOUTH); // Añadir el panel de acción al diálogo
        }
    }

    @Override
    protected void populateFields() {
        // Este método se llama en IProductDialog después de cargar la categoría (si es el caso)
        // y si 'product' no es nulo. Asegura que los campos se llenen con los datos del producto.
        super.populateFields(); // Llama a la implementación de la superclase para llenar los campos comunes
        // Si hay campos específicos de una subclase de Product (ej. FurnitureProduct),
        // y este diálogo se usa para editar todos los tipos de producto,
        // deberías hacer un casting seguro aquí y llenar esos campos.
        // Por ejemplo:
        // if (product instanceof FurnitureProduct) {
        //     FurnitureProduct furnitureProduct = (FurnitureProduct) product;
        //     dimensionsField.setText(furnitureProduct.getDimensions());
        //     materialField.setText(furnitureProduct.getMaterial());
        //     woodTypeField.setText(furnitureProduct.getWoodType());
        // }
        // Si EditProductDialog es un diálogo genérico para cualquier Product,
        // y los campos adicionales se gestionan en subclases de EditProductDialog,
        // entonces este método solo necesita llamar a super.populateFields().
    }

    @Override
    protected void onSave() {
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();
        String description = descriptionField.getText().trim();
        // imageFile ya es un campo de IProductDialog y se actualiza en chooseImage()

        if ( priceText.isEmpty() || stockText.isEmpty() || description.isEmpty()) {
            NotificationHandler.warning("Please fill in all required fields (Name, Price, Stock, Description).");
            return;
        }

        // Mostrar diálogo de confirmación
        ConfirmationDialog confirmationDialog = new ConfirmationDialog("Confirmation",
                "Are you sure you want to update the product: " + product.getName() + "?", () -> {
            // Lógica de guardado ejecutada después de la confirmación
            try {
                BigDecimal price = new BigDecimal(priceText);
                int stock = Integer.parseInt(stockText);

                product.setPrice(price);
                product.setStock(stock);
                product.setDescription(description);
                // Si la imagen fue cambiada, product.setImageUrl() necesitará actualizarse
                // con la URL del nuevo archivo en el servidor después de la subida.
                // product.setImageUrl(newImageUrlFromServer);

                // Ejecutar la operación de actualización en un hilo secundario
                new Thread(() -> {
                    // Si imageFile no es null, significa que se seleccionó una nueva imagen.
                    // Asume que updateProduct maneja la subida de la imagen si se le proporciona un File.
                    ApiResponse<Product> response;
                    if (imageFile != null) {
                        // Si se seleccionó una nueva imagen, sube la imagen y actualiza el producto
                        // pasar categoryName a minusculas para evitar problemas de mayúsculas/minúsculas
                        String lowerCaseCategoryName = category.getCategory_name() != null ? category.getCategory_name().toLowerCase() : null;
                        response = productService.createProductWithImage(product, imageFile, lowerCaseCategoryName);
                    } else {
                        // Si no se seleccionó una nueva imagen, solo actualiza los datos del producto
                        response = productService.updateProduct(product);
                    }

                    SwingUtilities.invokeLater(() -> { // Volver al EDT para actualizar la UI
                        if (response.isSuccess()) {
                            NotificationHandler.success("Product updated successfully!");
                            dispose(); // Cerrar el diálogo.
                            if (onRunnable != null) {
                                onRunnable.run(); // Ejecutar la acción de refresco de la pantalla padre
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
        confirmationDialog.setVisible(true); // Mostrar el diálogo de confirmación
    }
}