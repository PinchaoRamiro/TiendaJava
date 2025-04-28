package com.tiendajava.ui.screens.admin.products;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Category;
import com.tiendajava.model.Product;
import com.tiendajava.service.CategoryService;
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
    private final JComboBox<Category> categoryComboBox = new JComboBox<>();
    private final JTextField descriptionField = new JTextField(20);

    private File imageFile;
    private final JTextField imageField = new JTextField(20);


    private final ProductService productService = new ProductService();
    private final Runnable onProductCreated;

    private final JButton selectImageBtn = ButtonFactory.createSecondaryButton("Choose Image", null, this::chooseImage);

    public CreateProductDialog(Runnable onProductCreated) {
        this.onProductCreated = onProductCreated;
        setTitle("Create New Product");
        setModal(true);
        setSize(400, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.getPrimaryColor());

        buildForm();
        loadCategories();
    }

    private void buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(UIUtils.getDefaultPadding());

        UIUtils.styleComboBox(categoryComboBox);


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
        addLabelAndField(panel, gbc, row++, "Image:", imageField);
        gbc.gridx = 1; gbc.gridy = row++;
        panel.add(selectImageBtn, gbc);

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

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComboBox<?> comboBox) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel label = new JLabel(labelText);
        label.setForeground(UITheme.getTextColor());
        label.setFont(Fonts.NORMAL_FONT);
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(comboBox, gbc);
    }

    private void createProduct() {
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();
        String description = descriptionField.getText().trim();

        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        if (selectedCategory == null) {
            NotificationHandler.warning("Please select a category.");
            return;
        }


        if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
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
            int category_id = selectedCategory.getCategory_id();

            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
            product.setCategory_id(category_id);
            product.setDescription(description);
        
            // ahora delegamos en el service
            ApiResponse<Product> resp = productService.createProductWithImage(product, imageFile);
            if (resp.isSuccess()) {
                NotificationHandler.success("Producto creado"); 
                dispose();
                onProductCreated.run();
            } else {
                NotificationHandler.error("Error: " + resp.getMessage());
            }

        } catch (NumberFormatException e) {
            NotificationHandler.error("Invalid number format. Check price, stock, and category ID.");
        }
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("images", "jpg","png","jpeg","gif"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            imageFile = chooser.getSelectedFile();
            imageField.setText(imageFile.getName());
        }
    }

    private void loadCategories() {
    CategoryService categoryService = new CategoryService();
    ApiResponse<List<Category>> response = categoryService.getAllCategories();
    if (response.isSuccess() && response.getData() != null) {
        for (Category category : response.getData()) {
            categoryComboBox.addItem(category); // Agrega cada categoría al combo
        }
    } else {
        NotificationHandler.warning("Failed to load categories: " + response.getMessage());
    }
}

}
