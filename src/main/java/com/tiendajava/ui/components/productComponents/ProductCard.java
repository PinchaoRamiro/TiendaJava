package com.tiendajava.ui.components.productComponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tiendajava.model.Product;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts; // Asegúrate de que esta ruta sea correcta
import com.tiendajava.ui.utils.UITheme;

public class ProductCard extends JPanel {

    public ProductCard(Product product) {
        setLayout(new BorderLayout(5, 5)); // Espaciado interno
        setBackground(UITheme.getSecondaryColor()); // Fondo de la tarjeta
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.getPrimaryColor(), 1), // Borde delgado
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Padding interno
        ));
        setPreferredSize(new Dimension(80, 100)); // Tamaño preferido de la tarjeta
        setMaximumSize(new Dimension(100,110)); 

        // Nombre del producto
        JLabel nameLabel = new JLabel(product.getName(), SwingConstants.CENTER);
        nameLabel.setFont(Fonts.SUBTITLE_FONT);
        nameLabel.setForeground(UITheme.getTextColor());
        add(nameLabel, BorderLayout.NORTH);

        // Imagen del producto (simulada o cargada)
        // Idealmente, aquí cargarías la imagen real del producto.
        // Por ahora, usaremos un icono de ejemplo o un espacio vacío.
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // Si Product tiene una URL de imagen:
        // ImageIcon productImage = UIUtils.loadImageIcon(product.getImageUrl(), 100, 100);
        // imageLabel.setIcon(productImage != null ? productImage : AppIcons.PRODUCT_PLACEHOLDER_ICON);
        // Por ahora, un icono de placeholder si no tienes imágenes de productos:
        imageLabel.setIcon(AppIcons.PRODUCTS_ICON); // O un icono genérico de producto
        add(imageLabel, BorderLayout.CENTER);

        // Panel para precio y botón (opcional, para futuras interacciones)
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        footerPanel.setBackground(UITheme.getSecondaryColor());

        JLabel priceLabel = new JLabel("$" + String.format("%.2f", product.getPrice()));
        priceLabel.setFont(Fonts.TITLE_FONT.deriveFont(18f));
        priceLabel.setForeground(UITheme.getAccentColor());
        footerPanel.add(priceLabel);

        // Puedes añadir un botón "Add to Cart" aquí
        // JButton addToCartBtn = new JButton("Add");
        // addToCartBtn.setBackground(UITheme.getSuccessColor());
        // addToCartBtn.setForeground(UITheme.getTextColor());
        // footerPanel.add(addToCartBtn);

        add(footerPanel, BorderLayout.SOUTH);

        // Añadir un tooltip con la descripción del producto
        setToolTipText(product.getDescription());
    }
}