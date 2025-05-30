package com.tiendajava.ui.screens.user;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.model.Session;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;
import com.tiendajava.ui.utils.animations.TypingLabel;

public class DashboardUserScreen extends JPanel {

    private MainUI parent;

    public DashboardUserScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        createHeader();
        createMainContent();
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.getPrimaryColor());
        headerPanel.setBorder(UIUtils.getDefaultPadding(20, 20, 10, 20));

        // Título de la tienda
        JLabel title = new JLabel("RP STORE", AppIcons.HOME_ICON, SwingConstants.CENTER);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());

        // Mensaje de bienvenida personalizado
        TypingLabel welcomeLabel = new TypingLabel(
            "Welcome, " + Session.getInstance().getUser().getName() + "! Enjoy your shopping experience.",
            80
        );
        welcomeLabel.setFont(Fonts.SUBTITLE_FONT);
        welcomeLabel.setForeground(UITheme.getSuccessColor());
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomeLabel.startTyping();

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createMainContent() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UITheme.getPrimaryColor());
        mainPanel.setBorder(UIUtils.getDefaultPadding());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel de estadísticas del usuario
        JPanel statsPanel = createStatsPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(statsPanel, gbc);

        // Panel de accesos rápidos
        JPanel quickActionsPanel = createQuickActionsPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        mainPanel.add(quickActionsPanel, gbc);

        // Panel de productos destacados
        JPanel featuredPanel = createFeaturedProductsPanel();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        mainPanel.add(featuredPanel, gbc);

        // Panel de ofertas especiales
        JPanel offersPanel = createOffersPanel();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        mainPanel.add(offersPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 0));
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UITheme.getBorderCardsColor(), 2),
            "Your Statistics",
            0, 0, Fonts.NORMAL_FONT, UITheme.getTextColor()
        ));

        // Estadística: Pedidos realizados
        JPanel ordersPanel = createStatCard("Orders", "12", "📦");
        ordersPanel.setPreferredSize(new java.awt.Dimension(200, 100));
        
        // Estadística: Productos favoritos
        JPanel favoritesPanel = createStatCard("Favorites", "8", "❤️");
        favoritesPanel.setPreferredSize(new java.awt.Dimension(200, 100));
        
        // Estadística: Puntos de fidelidad
        JPanel pointsPanel = createStatCard("Points", "350", "⭐");
        pointsPanel.setPreferredSize(new java.awt.Dimension(200, 100));

        panel.add(ordersPanel);
        panel.add(favoritesPanel);
        panel.add(pointsPanel);

        return panel;
    }

    private JPanel createStatCard(String title, String value, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.getSecondaryColor());
        // empity border
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setLayout(new BorderLayout());

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(Fonts.NORMAL_FONT.deriveFont(24f));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(Fonts.SUBTITLE_FONT);
        valueLabel.setForeground(UITheme.getSuccessColor());

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(Fonts.SMALL_FONT);
        titleLabel.setForeground(UITheme.getTextColor());

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 10));
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UITheme.getBorderCardsColor(), 1),
            "Fast Access",
            0, 0, Fonts.NORMAL_FONT, UITheme.getTextColor()
        ));

        // Botón para ver categorías
        JButton categoriesBtn = new JButton("🏷️ Explore Products");
        categoriesBtn.setFont(Fonts.NORMAL_FONT);
        categoriesBtn.setBackground(UITheme.getSecondaryColor());
        categoriesBtn.setForeground(UITheme.getTextColor());
        categoriesBtn.addActionListener(e -> {
            parent.showScreen("products-user");
        });

        // Botón para ver carrito
        JButton cartBtn = new JButton("🛒 My Cart");
        cartBtn.setFont(Fonts.NORMAL_FONT);
        cartBtn.setBackground(UITheme.getSuccessColor());
        cartBtn.setForeground(UITheme.getTextColor());
        cartBtn.addActionListener(e -> {
            parent.showScreen("cart-p");
        });

        // Botón para ver pedidos
        JButton ordersBtn = new JButton("📋 My Orders");
        ordersBtn.setFont(Fonts.NORMAL_FONT);
        ordersBtn.setBackground(UITheme.getInfoColor());
        ordersBtn.setForeground(UITheme.getTextColor());
        ordersBtn.addActionListener(e -> {
            parent.showScreen("order-history");
        });

        panel.add(categoriesBtn);
        panel.add(cartBtn);
        panel.add(ordersBtn);

        return panel;
    }

    private JPanel createFeaturedProductsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 0, 5));
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UITheme.getBorderCardsColor(), 1),
            "Featured Products",
            0, 0, Fonts.NORMAL_FONT, UITheme.getTextColor()
        ));

        // Extraemos algunos productos destacados
        ProductService productService = new ProductService();
        ApiResponse<List<Product>> response = productService.getFeaturedProducts();
        if (!response.isSuccess()) {
            JLabel errorLabel = new JLabel("Error loading products: " + response.getMessage());
            errorLabel.setFont(Fonts.SMALL_FONT);
            errorLabel.setForeground(UITheme.getErrorColor());
            panel.add(errorLabel);
            return panel;
        }

        String[] products = response.getData().stream()
            .map(product -> product.getName() + " - $" + product.getPrice())
            .toArray(String[]::new);

        for (String product : products) {
            JLabel productLabel = new JLabel(product);
            productLabel.setFont(Fonts.SMALL_FONT);
            productLabel.setForeground(UITheme.getTextColor());
            productLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            panel.add(productLabel);
        }

        return panel;
    }

    private JPanel createOffersPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UITheme.getSuccessColor(), 1),
            "🔥 Special offers",
            0, 0, Fonts.NORMAL_FONT, UITheme.getTextColor()
        ));

        JLabel offer1 = new JLabel("🎯 20% OFF in Electronics");
        offer1.setFont(Fonts.NORMAL_FONT);
        offer1.setForeground(UITheme.getWarningColor());

        JLabel separator = new JLabel(" | ");
        separator.setForeground(UITheme.getTextColor());

        JLabel offer2 = new JLabel("📦 Free shipping on purchases+$50");
        offer2.setFont(Fonts.NORMAL_FONT);
        offer2.setForeground(UITheme.getWarningColor());

        JLabel separator2 = new JLabel(" | ");
        separator2.setForeground(UITheme.getTextColor());

        JLabel offer3 = new JLabel("⭐ Buy 1 Get 1 Free on selected items");
        offer3.setFont(Fonts.NORMAL_FONT);
        offer3.setForeground(UITheme.getWarningColor());

        panel.add(offer1);
        panel.add(separator);
        panel.add(offer2);
        panel.add(separator2);
        panel.add(offer3);

        return panel;
    }
}