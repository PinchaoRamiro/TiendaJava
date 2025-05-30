package com.tiendajava.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.tiendajava.model.Product;
import com.tiendajava.model.ProductsModels.ClothingProduct;
import com.tiendajava.model.ProductsModels.ElectronicsProduct;
import com.tiendajava.model.ProductsModels.FurnitureProduct;
import com.tiendajava.model.Session;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;

public class ProductItemCard extends JPanel {
    private static final Map<String,ImageIcon> imageCache = new ConcurrentHashMap<>();

    public ProductItemCard(Product product,
                           Runnable onEdit,
                           Runnable onDelete,
                            Runnable onClick,
                           Runnable onAddToCart) {

        setLayout(new BorderLayout(10,10));
        setBackground(UITheme.getSecondaryColor());
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setPreferredSize(new Dimension(400,120));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // --- Clickable area ---
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                onClick.run();
            }
        });

        // --- Imagen ---
        JLabel imageLabel = new JLabel(getPlaceholderIcon());
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(100,100));
        add(imageLabel, BorderLayout.WEST);

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            loadImageAsync(product.getImage(), imageLabel);
        }

        JPanel infoContainer = new JPanel(new GridLayout(1, 2)); // Divide en dos columnas
        infoContainer.setBackground(UITheme.getSecondaryColor());

        // --- Columna 1: info general ---
        JPanel infoLeft = new JPanel();
        infoLeft.setLayout(new BoxLayout(infoLeft, BoxLayout.Y_AXIS));
        infoLeft.setBackground(UITheme.getSecondaryColor());

        infoLeft.add(createLabel("Name: " + product.getName(), Fonts.SUBTITLE_FONT, UITheme.getTextColor()));
        infoLeft.add(createLabel("Category: " + product.getCategory(), Fonts.SMALL_FONT, UITheme.getTextColor()));
        infoLeft.add(createLabel("Price: $" + product.getPrice(), Fonts.NORMAL_FONT, UITheme.getSuccessColor()));
        infoLeft.add(createLabel("Stock: " + product.getStock(), Fonts.SMALL_FONT, UITheme.getTextColor()));
        infoLeft.add(createLabel("Description: " + product.getDescription(), Fonts.SMALL_FONT, UITheme.getTextColor()));

        // --- Columna 2: atributos específicos ---
        JPanel infoRight = new JPanel();
        infoRight.setLayout(new BoxLayout(infoRight, BoxLayout.Y_AXIS));
        infoRight.setBackground(UITheme.getSecondaryColor());

        switch (product) {
            case ClothingProduct clothing -> {
                infoRight.add(createLabel("Size: " + clothing.getSize(), Fonts.SMALL_FONT, UITheme.getTextColor()));
                infoRight.add(createLabel("Color: " + clothing.getColor(), Fonts.SMALL_FONT, UITheme.getTextColor()));
                infoRight.add(createLabel("Material: " + clothing.getMaterial(), Fonts.SMALL_FONT, UITheme.getTextColor()));
            }
            case ElectronicsProduct electronics -> {
                infoRight.add(createLabel("Voltage: " + electronics.getVoltage(), Fonts.SMALL_FONT, UITheme.getTextColor()));
                infoRight.add(createLabel("Warranty: " + electronics.getWarranty(), Fonts.SMALL_FONT, UITheme.getTextColor()));
                infoRight.add(createLabel("Brand: " + electronics.getBrand(), Fonts.SMALL_FONT, UITheme.getTextColor()));
            }
            case FurnitureProduct furniture -> {
                infoRight.add(createLabel("Dimensions: " + furniture.getDimensions(), Fonts.SMALL_FONT, UITheme.getTextColor()));
                infoRight.add(createLabel("Material: " + furniture.getMaterial(), Fonts.SMALL_FONT, UITheme.getTextColor()));
                infoRight.add(createLabel("Wood Type: " + furniture.getWoodType(), Fonts.SMALL_FONT, UITheme.getTextColor()));
            }
            default -> {}
        }

        infoContainer.add(infoLeft);
        infoContainer.add(infoRight);

        add(infoContainer, BorderLayout.CENTER);



        // --- Acciones ---
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        actions.setBackground(UITheme.getSecondaryColor());
        if ("admin".equalsIgnoreCase(Session.getInstance().getRole())) {
            actions.add(ButtonFactory.createIconButton(AppIcons.EDIT_ICON, "Edit Product", onEdit));
            ImageIcon tintedDelete = UIUtils.tintImage(AppIcons.DELETE_ICON, UITheme.getDangerColor());
            actions.add(ButtonFactory.createIconButton(tintedDelete, "Delete", onDelete));
        } else {
            actions.add(ButtonFactory.createIconButton(AppIcons.CART_ADD_ICON, "Add to Cart", onAddToCart));
        }
        add(actions, BorderLayout.EAST);
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        return lbl;
    }

    private ImageIcon getPlaceholderIcon() {
        return AppIcons.PRODUCT_ICON;
    }

    private void loadImageAsync(String imagePath, JLabel target) {
        String url = "http://localhost:5000" + imagePath;
        if (imageCache.containsKey(url)) {
            target.setIcon(imageCache.get(url));
            return;
        }
        new SwingWorker<ImageIcon,Void>() {
            @Override
            protected ImageIcon doInBackground() {
                try {
                    URL imageUrl = new URI(url).toURL();
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    try (InputStream in = conn.getInputStream()) {
                        byte[] data = in.readAllBytes();
                        ImageIcon raw = new ImageIcon(data);
                        Image scaled = raw.getImage()
                                        .getScaledInstance(100,100,Image.SCALE_SMOOTH);
                        return new ImageIcon(scaled);
                    }
                } catch (IOException | URISyntaxException e) {
                    return getPlaceholderIcon();
                }
            }
            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    imageCache.put(url, icon);
                    target.setIcon(icon);
                } catch (InterruptedException | ExecutionException ignored) {
                }
            }
        }.execute();
    }

}
