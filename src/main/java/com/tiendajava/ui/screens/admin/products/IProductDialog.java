package com.tiendajava.ui.screens.admin.products;

import java.awt.GridBagConstraints;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Category;
import com.tiendajava.model.Product;
import com.tiendajava.service.CategoryService;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.NotificationHandler;
import com.tiendajava.ui.utils.UITheme;

abstract class IProductDialog extends JDialog {

    JTextField nameField = new JTextField(20);
    JTextField priceField = new JTextField(20);
    JTextField stockField = new JTextField(20);
    JComboBox<Category> categoryComboBox = new JComboBox<>();
    JTextField descriptionField = new JTextField(20);

    ProductService productService = new ProductService();
    Product product;
    Runnable onRunnable;

    public IProductDialog(Product product, Runnable onRunnable) {
        this.product = product;
        this.onRunnable = onRunnable;
        setModal(true);
        setSize(400, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.getPrimaryColor());
        initialize();
    }

    protected  void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel label = new JLabel(labelText);
        label.setForeground(UITheme.getTextColor());
        label.setFont(Fonts.NORMAL_FONT);
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    protected void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComboBox<?> comboBox) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel label = new JLabel(labelText);
        label.setForeground(UITheme.getTextColor());
        label.setFont(Fonts.NORMAL_FONT);
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(comboBox, gbc);
    }

    protected final void loadCategories() {
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

    private void initialize() {
        loadCategories();
    }
}

