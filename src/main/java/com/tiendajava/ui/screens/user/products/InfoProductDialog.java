package com.tiendajava.ui.screens.user.products;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tiendajava.model.Product;
import com.tiendajava.model.ProductsModels.ClothingProduct;
import com.tiendajava.model.ProductsModels.ElectronicsProduct;
import com.tiendajava.model.ProductsModels.FurnitureProduct;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;

public class InfoProductDialog extends JDialog {

    public InfoProductDialog(Product product) {
        setTitle("Product Details");
        setModal(true);
        setSize(400, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.getPrimaryColor());

        JPanel panel = new JPanel();
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel titleLabel = new JLabel("Product Information", AppIcons.INFO_ICON, JLabel.CENTER);
        titleLabel.setFont(Fonts.TITLE_FONT);
        titleLabel.setForeground(UITheme.getTextColor());
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));

        panel.add(createLabel("Name: " + product.getName()));
        panel.add(createLabel("Description: " + product.getDescription()));
        panel.add(createLabel("Price: $" + product.getPrice()));
        panel.add(createLabel("Stock: " + product.getStock()));
        panel.add(createLabel("Category: " + product.getCategory()));

        if (product instanceof ClothingProduct || product instanceof ElectronicsProduct || product instanceof FurnitureProduct) {
            loadCategorySpecificAttributes(panel, product);
        }

        panel.add(Box.createVerticalStrut(20));
        JButton closeBtn = ButtonFactory.createSecondaryButton("Close", AppIcons.CANCEL_ICON, this::dispose);
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(20));
        panel.add(closeBtn);

        add(panel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Fonts.NORMAL_FONT);
        label.setForeground(UITheme.getTextColor());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void loadCategorySpecificAttributes(JPanel panel, Product product) {
        switch (product) {
            case ClothingProduct clothing -> {
                panel.add(createLabel("Size: " + clothing.getSize()));
                panel.add(createLabel("Color: " + clothing.getColor()));
                panel.add(createLabel("Material: " + clothing.getMaterial()));
            }
            case ElectronicsProduct electronics -> {
                panel.add(createLabel("Brand: " + electronics.getBrand()));
                panel.add(createLabel("Voltage: " + electronics.getVoltage()));
                panel.add(createLabel("Warranty: " + electronics.getWarranty()));
            }
            case FurnitureProduct furniture -> {
                panel.add(createLabel("Dimensions: " + furniture.getDimensions()));
                panel.add(createLabel("Material: " + furniture.getMaterial()));
                panel.add(createLabel("Wood Type: " + furniture.getWoodType()));
            }
            default -> {
            }
        }
    }
}
