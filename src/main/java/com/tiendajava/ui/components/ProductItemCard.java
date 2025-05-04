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
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.tiendajava.model.Product;
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
                           Runnable onAddToCart) {

        setLayout(new BorderLayout(10,10));
        setBackground(UITheme.getSecondaryColor());
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        setPreferredSize(new Dimension(400,120));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // --- Imagen ---
        JLabel imageLabel = new JLabel(getPlaceholderIcon());
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(100,100));
        add(imageLabel, BorderLayout.WEST);

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            loadImageAsync(product.getImage(), imageLabel);
        }

        // --- Info ---
        JPanel info = new JPanel(new GridLayout(5,1,0,2));
        info.setBackground(UITheme.getSecondaryColor());
        info.add(createLabel(product.getName(), Fonts.SUBTITLE_FONT, UITheme.getTextColor()));
        info.add(createLabel("Category: "+product.getCategory().getCategory_name(),
                             Fonts.SMALL_FONT, UITheme.getTextColor()));
        info.add(createLabel("$"+product.getPrice(), Fonts.NORMAL_FONT, UITheme.getSuccessColor()));
        info.add(createLabel("Stock: "+product.getStock(), Fonts.SMALL_FONT, UITheme.getTextColor()));
        info.add(createLabel(product.getDescription(), Fonts.SMALL_FONT, UITheme.getTextColor()));
        add(info, BorderLayout.CENTER);

        // --- Acciones ---
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
        actions.setBackground(UITheme.getSecondaryColor());
        if ("admin".equalsIgnoreCase(Session.getInstance().getRole())) {
            actions.add(ButtonFactory.createIconButton(AppIcons.EDIT_ICON, onEdit));
            ImageIcon tintedDelete = UIUtils.tintImage(AppIcons.DELETE_ICON, UITheme.getDangerColor());
            actions.add(ButtonFactory.createIconButton(tintedDelete, onDelete));
        } else {
            actions.add(ButtonFactory.createIconButton(AppIcons.CART_ADD_ICON, onAddToCart));
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
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(10000);
                    System.out.println("" + conn.getResponseCode() + " " + conn.getResponseMessage());
                    System.out.println("Loading image from: " + url);
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
