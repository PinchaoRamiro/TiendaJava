package com.tiendajava.ui.screens.user.products;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.tiendajava.model.Product;
import com.tiendajava.model.ProductsModels.ClothingProduct;
import com.tiendajava.model.ProductsModels.ElectronicsProduct;
import com.tiendajava.model.ProductsModels.FurnitureProduct;
import com.tiendajava.model.Session;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.screens.user.cart.AddCartDialog;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class InfoProductDialog extends JDialog {

    private static final int DIALOG_WIDTH = 500;
    private static final int DIALOG_HEIGHT = 600;
    private static final int CONTENT_PADDING = 25;
    private static final int ELEMENT_GAP = 15;

    private final MainUI parent;
    private final Product product;

    public InfoProductDialog( MainUI parent, Product product) {
        this.parent = parent;
        this.product = product;
        configureDialog();
        initUI(product);
        setLocationRelativeTo(null);
    }

    private void configureDialog() {
        setTitle("Product Information");
        setModal(true);
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setMinimumSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        getContentPane().setBackground(UITheme.getPrimaryColor());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initUI(Product product) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.getPrimaryColor());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(CONTENT_PADDING, CONTENT_PADDING, CONTENT_PADDING, CONTENT_PADDING));

        mainPanel.add(createHeaderPanel(product), BorderLayout.NORTH);

        mainPanel.add(createInfoPanel(product), BorderLayout.CENTER);

        mainPanel.add(createActionPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel(Product product) {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(UITheme.getPrimaryColor());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, ELEMENT_GAP, 0));

        JLabel imageLabel = new JLabel(product.getName().substring(0, 1), SwingConstants.CENTER);
        imageLabel.setFont(Fonts.TITLE_FONT.deriveFont(48f));
        imageLabel.setForeground(Color.WHITE);
        imageLabel.setBackground(UITheme.getAccentColor());
        imageLabel.setOpaque(true);
        imageLabel.setPreferredSize(new Dimension(80, 80));
        imageLabel.setBorder(BorderFactory.createLineBorder(UITheme.getTertiaryColor(), 2));

        JPanel imageContainer = new JPanel(new GridBagLayout());
        imageContainer.setBackground(UITheme.getPrimaryColor());
        imageContainer.add(imageLabel);

        JLabel titleLabel = new JLabel(product.getName(), SwingConstants.CENTER);
        titleLabel.setFont(Fonts.TITLE_FONT);
        titleLabel.setForeground(UITheme.getTextColor());

        headerPanel.add(imageContainer, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JScrollPane createInfoPanel(Product product) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(UITheme.getSecondaryColor());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Información básica
        infoPanel.add(createInfoSection("Basic Information", new String[] {
            "Description: " + product.getDescription(),
            "Price: $" + product.getPrice(),
            "Stock: " + product.getStock(),
            "Category: " + product.getCategory()
        }));

        if (product instanceof ClothingProduct || product instanceof ElectronicsProduct || product instanceof FurnitureProduct) {
            infoPanel.add(Box.createVerticalStrut(ELEMENT_GAP));
            infoPanel.add(createCategorySpecificSection(product));
        }

        JScrollPane scrollPane = new JScrollPane(infoPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        scrollPane.getViewport().setBackground(UITheme.getSecondaryColor());

        return scrollPane;
    }

    private JPanel createInfoSection(String title, String[] items) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(UITheme.getSecondaryColor());
        sectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(Fonts.SUBTITLE_FONT);
        sectionTitle.setForeground(UITheme.getAccentColor());
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        sectionPanel.add(sectionTitle);
    
        for (String item : items) {
            JLabel label = new JLabel(item);
            label.setFont(Fonts.NORMAL_FONT);
            label.setForeground(UITheme.getTextColor());
            label.setBorder(BorderFactory.createEmptyBorder(3, 15, 3, 0));
            sectionPanel.add(label);
        }

        return sectionPanel;
    }

    private JPanel createCategorySpecificSection(Product product) {
        String sectionTitle = "";
        String[] items = {};

        if (product instanceof ClothingProduct clothing) {
            sectionTitle = "Details on Clothing";
            items = new String[] {
                "Size: " + clothing.getSize(),
                "Color: " + clothing.getColor(),
                "Material: " + clothing.getMaterial()
            };
        } else if (product instanceof ElectronicsProduct electronics) {
            sectionTitle = "Especifications of Electronics";
            items = new String[] {
                "Brand: " + electronics.getBrand(),
                "Voltage: " + electronics.getVoltage(),
                "Warranty: " + electronics.getWarranty()
            };
        } else if (product instanceof FurnitureProduct furniture) {
            sectionTitle = "Details on Furniture";
            items = new String[] {
                "Dimensions: " + furniture.getDimensions(),
                "Material: " + furniture.getMaterial(),
                "Type of Wood: " + furniture.getWoodType()
            };
        }

        return createInfoSection(sectionTitle, items);
    }

    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        actionPanel.setBackground(UITheme.getPrimaryColor());
        actionPanel.setBorder(BorderFactory.createEmptyBorder(ELEMENT_GAP, 0, 0, 0));

        // Botón de cerrar
        JButton closeBtn = ButtonFactory.createSecondaryButton("Close", AppIcons.CANCEL_ICON, this::dispose);
        closeBtn.setPreferredSize(new Dimension(120, 40));

        if(Session.getInstance().getRole().equals("user")){
            JButton addToCartBtn = ButtonFactory.createPrimaryButton("Add to Cart", AppIcons.CART_ICON, () -> {
                new AddCartDialog(parent.getCart(), product).setVisible(true);
            });
            addToCartBtn.setPreferredSize(new Dimension(180, 40));
            actionPanel.add(addToCartBtn);
        }

        actionPanel.add(closeBtn);

        return actionPanel;
    }

    @Override
    public MainUI getParent() {
        return parent;
    }

    public Product getProduct() {
        return product;
    }
}