package com.tiendajava.ui.screens.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent; // Para detectar cambios de tamaño
import java.awt.event.HierarchyEvent;   // Para detectar cambios de tamaño
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.model.Session;
import com.tiendajava.model.User;
import com.tiendajava.service.ProductService;
import com.tiendajava.ui.MainUI;
import com.tiendajava.ui.components.ButtonFactory;
import com.tiendajava.ui.components.productComponents.ProductCard;
import com.tiendajava.ui.utils.AppIcons;
import com.tiendajava.ui.utils.Fonts;
import com.tiendajava.ui.utils.UITheme;
import com.tiendajava.ui.utils.UIUtils;
import com.tiendajava.ui.utils.animations.TypingLabel;

public class DashboardUserScreen extends JPanel {

    private final MainUI parent;
    private final ProductService productService = new ProductService();
    private JScrollPane featuredProductsScrollPane;
    private JPanel featuredProductsPanel;
    private JLabel loadingLabel;

    private JPanel scrollableContentPanel;
    private JPanel statsPanel; 
    private JPanel contentPanel; 

    private boolean isSmallScreen = false;
    private static final int SMALL_SCREEN_THRESHOLD = 600; 

    public DashboardUserScreen(MainUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(UITheme.getPrimaryColor());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initUI();
        loadData();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                checkScreenSizeAndApplyLayout();
            }
        });
    }

    private void initUI() {
        add(createHeader(), BorderLayout.NORTH);

        scrollableContentPanel = new JPanel();
        scrollableContentPanel.setLayout(new BoxLayout(scrollableContentPanel, BoxLayout.Y_AXIS));
        scrollableContentPanel.setBackground(UITheme.getPrimaryColor());
        
        createMainContentPanels();

        JScrollPane mainScrollPane = new JScrollPane(scrollableContentPanel);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setBorder(BorderFactory.createEmptyBorder());

        mainScrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());

        add(mainScrollPane, BorderLayout.CENTER);
        checkScreenSizeAndApplyLayout();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.getPrimaryColor());
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        TypingLabel title = new TypingLabel("RP STORE", 30);
        title.setFont(Fonts.TITLE_FONT);
        title.setForeground(UITheme.getTextColor());
        title.setHorizontalAlignment(SwingConstants.CENTER);

        User user = Session.getInstance().getUser();
        String welcomeText = String.format("Welcome, %s %s!", user.getName(), user.getLastName());
        TypingLabel welcomeLabel = new TypingLabel(welcomeText, 50);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(Fonts.SUBTITLE_FONT);
        welcomeLabel.setForeground(UITheme.getSuccessColor());

        header.add(title, BorderLayout.NORTH);
        header.add(welcomeLabel, BorderLayout.CENTER);

        addHierarchyListener(e -> {
            if (e.getChangeFlags() == HierarchyEvent.SHOWING_CHANGED && isShowing()) {
                title.startTyping();
                welcomeLabel.startTyping();
            }
        });

        return header;
    }

    private void createMainContentPanels() {
        statsPanel = new JPanel(); 
        statsPanel.setBackground(UITheme.getPrimaryColor());
        statsPanel.setBorder(UIUtils.createTitledBorder("Your Statistics"));

        contentPanel = new JPanel();
        contentPanel.setBackground(UITheme.getPrimaryColor());

        contentPanel.add(createQuickActionsPanel());
        contentPanel.add(createFeaturedProductsPanel());

        scrollableContentPanel.add(statsPanel);
        scrollableContentPanel.add(Box.createVerticalStrut(20));
        scrollableContentPanel.add(contentPanel);
        scrollableContentPanel.add(Box.createVerticalStrut(20));
        scrollableContentPanel.add(createOffersPanel());
    }

    private void checkScreenSizeAndApplyLayout() {
        boolean newIsSmallScreen = getWidth() < SMALL_SCREEN_THRESHOLD;

        if (newIsSmallScreen != isSmallScreen) {
            isSmallScreen = newIsSmallScreen;
            updateStatsLayout();    
            updateMainContentLayout();  
            
            scrollableContentPanel.revalidate();
            scrollableContentPanel.repaint();
        }
    }

    private void updateStatsLayout() {
        if (statsPanel != null) {
            statsPanel.removeAll(); 
            
            StatCard ordersCard = new StatCard("Orders", "15", 
                UITheme.getInfoColor(), "Total orders placed");
            
            StatCard favoritesCard = new StatCard("Favorites", "8", 
                UITheme.getDangerColor(), "Saved favorite items");
            
            StatCard pointsCard = new StatCard("Points", "350", 
                UITheme.getWarningColor(), "Loyalty points available");
            
            if (isSmallScreen) {
                statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
                ordersCard.setAlignmentX(CENTER_ALIGNMENT);
                favoritesCard.setAlignmentX(CENTER_ALIGNMENT);
                pointsCard.setAlignmentX(CENTER_ALIGNMENT);
                
                statsPanel.add(ordersCard);
                statsPanel.add(Box.createVerticalStrut(15));
                statsPanel.add(favoritesCard);
                statsPanel.add(Box.createVerticalStrut(15));
                statsPanel.add(pointsCard);
            } else {
                statsPanel.setLayout(new GridLayout(1, 3, 15, 0));
                statsPanel.add(ordersCard);
                statsPanel.add(favoritesCard);
                statsPanel.add(pointsCard);
            }
            statsPanel.revalidate();
            statsPanel.repaint();   
        }
    }
    private void updateMainContentLayout() {
        if (contentPanel != null) {

            JPanel quickActionsPanel = (JPanel) contentPanel.getComponent(0);
            featuredProductsPanel = (JPanel) contentPanel.getComponent(1);

            if (isSmallScreen) {
                contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
                quickActionsPanel.setAlignmentX(CENTER_ALIGNMENT); 
                featuredProductsPanel.setAlignmentX(CENTER_ALIGNMENT); 
                quickActionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, quickActionsPanel.getPreferredSize().height));
                featuredProductsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, featuredProductsPanel.getPreferredSize().height));

            } else {
                contentPanel.setLayout(new GridLayout(1, 2, 20, 0));
                quickActionsPanel.setMaximumSize(null);
                featuredProductsPanel.setMaximumSize(null); 
            }
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }


    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(UIUtils.createTitledBorder("Quick Actions"));

        Dimension buttonSize = new Dimension(200, 45); 
        int buttonSpacing = 10;

        ButtonFactory.ActionButton productsBtn = new ButtonFactory.ActionButton(
                "Explore Products",
                AppIcons.PRODUCTS_ICON,
                () -> parent.showScreen("products-user"));
        productsBtn.setPreferredSize(buttonSize);
        productsBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonSize.height)); 

        ButtonFactory.ActionButton cartBtn = new ButtonFactory.ActionButton(
                "My Cart",
                AppIcons.CART_ICON,
                () -> parent.showScreen("cart-p"));
        cartBtn.setPreferredSize(buttonSize);
        cartBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonSize.height));

        ButtonFactory.ActionButton ordersBtn = new ButtonFactory.ActionButton(
                "Order History",
                AppIcons.INFO_ICON,
                () -> parent.showScreen("order-history"));
        ordersBtn.setPreferredSize(buttonSize);
        ordersBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonSize.height));

        ButtonFactory.ActionButton settingsBtn = new ButtonFactory.ActionButton(
                "Account Settings",
                AppIcons.SETTINGS_ICON,
                () -> parent.showScreen("account-settings"));
        settingsBtn.setPreferredSize(buttonSize);
        settingsBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, buttonSize.height));

        panel.add(productsBtn);
        panel.add(Box.createVerticalStrut(buttonSpacing));
        panel.add(cartBtn);
        panel.add(Box.createVerticalStrut(buttonSpacing));
        panel.add(ordersBtn);
        panel.add(Box.createVerticalStrut(buttonSpacing));
        panel.add(settingsBtn);

        return panel;
    }

    private JPanel createFeaturedProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.getPrimaryColor());
        panel.setBorder(UIUtils.createTitledBorder("Featured Products"));

        featuredProductsPanel = new JPanel();
        featuredProductsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); 
        featuredProductsPanel.setBackground(UITheme.getSecondaryColor());
        featuredProductsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        loadingLabel.setFont(Fonts.NORMAL_FONT);
        loadingLabel.setForeground(UITheme.getTextColor());
        featuredProductsPanel.add(loadingLabel);

        featuredProductsScrollPane = new JScrollPane(featuredProductsPanel);
        featuredProductsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        featuredProductsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        featuredProductsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        featuredProductsScrollPane.setBorder(BorderFactory.createEmptyBorder());

        featuredProductsScrollPane.getVerticalScrollBar().setUI(UIUtils.createDarkScrollBar());
        panel.add(featuredProductsScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createOffersPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(UITheme.getSecondaryColor());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.getAccentColor()),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        OfferBadge offer1 = new OfferBadge("20% OFF in Electronics",null);
        OfferBadge offer2 = new OfferBadge("Free shipping on orders +$50", null);
        OfferBadge offer3 = new OfferBadge("Buy 1 Get 1 Free", null);

        panel.add(offer1);
        panel.add(offer2);
        panel.add(offer3);

        return panel;
    }

    private void loadData() {
        loadingLabel.setVisible(true);
        featuredProductsPanel.revalidate();
        featuredProductsPanel.repaint();

        new SwingWorker<List<Product>, Void>() {
            @Override
            protected List<Product> doInBackground() throws Exception {
                ApiResponse<List<Product>> response = productService.getFeaturedProducts();
                if (response.isSuccess()) {
                    return response.getData();
                } else {
                    System.err.println("Error al cargar productos destacados: " + response.getMessage());
                    return null;
                }
            }

            @Override
            protected void done() {
                loadingLabel.setVisible(false);
                featuredProductsPanel.removeAll();

                try {
                    List<Product> products = get();
                    if (products == null || products.isEmpty()) {
                        JLabel noProducts = new JLabel("No featured products available", SwingConstants.CENTER);
                        noProducts.setFont(Fonts.NORMAL_FONT);
                        noProducts.setForeground(UITheme.getTextColor());
                        featuredProductsPanel.add(noProducts);
                    } else {
                        for (Product product : products) {
                            ProductCard card = new ProductCard(product);
                            card.setPreferredSize(new Dimension(10, 10));
                            featuredProductsPanel.add(card);
                        }
                    }
                } catch (InterruptedException | ExecutionException e) {
                    JLabel errorLabel = new JLabel("Error loading products.", SwingConstants.CENTER);
                    errorLabel.setFont(Fonts.NORMAL_FONT);
                    errorLabel.setForeground(UITheme.getDangerColor());
                    featuredProductsPanel.add(errorLabel);
                } finally {
                    featuredProductsPanel.revalidate();
                    featuredProductsPanel.repaint();
                }
            }
        }.execute();
    }

    private static class StatCard extends JPanel {
        public StatCard(String title, String value, Color color, String tooltip) {
            setLayout(new BorderLayout(10, 10));
            setBackground(UITheme.getSecondaryColor());
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color,  1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
            setToolTipText(tooltip);

            JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
            valueLabel.setFont(Fonts.TITLE_FONT.deriveFont(24f));
            valueLabel.setForeground(color);
            add(valueLabel, BorderLayout.CENTER);

            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(Fonts.SMALL_FONT);
            titleLabel.setForeground(UITheme.getTextColor());
            add(titleLabel, BorderLayout.SOUTH);
        }
    }

    private static class OfferBadge extends JPanel {
        public OfferBadge(String text, ImageIcon icon) {
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
            setBackground(UITheme.getPrimaryColor());
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(UITheme.getSuccessColor(), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));

            JLabel iconLabel = new JLabel(icon);
            add(iconLabel);

            JLabel textLabel = new JLabel(text);
            textLabel.setFont(Fonts.SMALL_FONT);
            textLabel.setForeground(UITheme.getSuccessColor());
            add(textLabel);
        }
    }
}