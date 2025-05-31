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
import com.tiendajava.ui.utils.Fonts; 
import com.tiendajava.ui.utils.UITheme;

public class ProductCard extends JPanel {

    public ProductCard(Product product) {
        setLayout(new BorderLayout(5, 5)); 
        setBackground(UITheme.getSecondaryColor()); 
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.getPrimaryColor(), 1), 
            BorderFactory.createEmptyBorder(10, 10, 10, 10) 
        ));
        setPreferredSize(new Dimension(80, 100)); 
        setMaximumSize(new Dimension(100,110)); 

        JLabel nameLabel = new JLabel(product.getName(), SwingConstants.CENTER);
        nameLabel.setFont(Fonts.SUBTITLE_FONT);
        nameLabel.setForeground(UITheme.getTextColor());
        add(nameLabel, BorderLayout.NORTH);
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setIcon(AppIcons.PRODUCTS_ICON); 
        add(imageLabel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        footerPanel.setBackground(UITheme.getSecondaryColor());

        JLabel priceLabel = new JLabel("$" + String.format("%.2f", product.getPrice()));
        priceLabel.setFont(Fonts.TITLE_FONT.deriveFont(18f));
        priceLabel.setForeground(UITheme.getAccentColor());
        footerPanel.add(priceLabel);

        add(footerPanel, BorderLayout.SOUTH);

        setToolTipText(product.getDescription());
    }
}