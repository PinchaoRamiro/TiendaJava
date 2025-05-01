package com.tiendajava.ui.utils;

import java.awt.MediaTracker;

import javax.swing.ImageIcon;

public class AppIcons {

    public static final String ICON_PATH = "/icons/store.png";
    public static final String LOGO_PATH = "/icons/logo.png";
    public static final String HOME_PATH = "/icons/home.png";

    public static final String ADMIN_PATH = "/icons/admin-alt.png";
    public static final String USERS_PATH = "/icons/users.png";
    public static final String USER_PATH = "/icons/user.png";
    public static final String USER_CHECK_PATH = "/icons/user-check.png";
    public static final String USER_PLUS_PATH = "/icons/user-add.png";

    public static final String SETTINGS_PATH = "/icons/settings.png";
    public static final String DELETE_PATH = "/icons/trash.png";
    public static final String PRODUCTS_PATH = "/icons/selling.png";
    public static final String PRODUCT_PATH = "/icons/product.png";
    public static final String BOX_PATH = "/icons/box.png";
    public static final String PRODUCT_CONFIG_PATH = "/icons/config-product.png";
    public static final String SEARCH_PATH = "/icons/search.png";

    public static final String ORDER_HISTORY_PATH = "/icons/order-history.png";
    public static final String CART_PATH = "/icons/Cart.png";
    public static final String CART_ADD_PATH = "/icons/cart-add.png";


    public static final String EXIT_PATH = "/icons/exit.png";
    public static final String ENTER_PATH = "/icons/enter.png";
    public static final String BACK_PATH = "/icons/back.png";


    public static final String ADD_PATH = "/icons/add.png";
    public static final String EDIT_PATH = "/icons/edit.png";
    public static final String KEYS_PATH = "/icons/door-key.png";
    public static final String UNACTIVE_PATH = "/icons/defuse.png";

    public static final String LINKEDIN_PATH = "/icons/linkedin.png";
    public static final String GITHUB_PATH = "/icons/github.png";



    public static final ImageIcon APP_ICON = LoadIcon(ICON_PATH);
    public static final ImageIcon LOGO_ICON = LoadIcon(LOGO_PATH);
    public static final ImageIcon HOME_ICON = LoadIcon(HOME_PATH);
    public static final ImageIcon ADMIN_ICON = LoadIcon(ADMIN_PATH);
    public static final ImageIcon USERS_ICON = LoadIcon(USERS_PATH);
    public static final ImageIcon USER_ICON = LoadIcon(USER_PATH);
    public static final ImageIcon USER_CHECK_ICON = LoadIcon(USER_CHECK_PATH);
    public static final ImageIcon USER_PLUS_ICON = LoadIcon(USER_PLUS_PATH);
    public static final ImageIcon SETTINGS_ICON = LoadIcon(SETTINGS_PATH);
    public static final ImageIcon DELETE_ICON = LoadIcon(DELETE_PATH);
    public static final ImageIcon PRODUCTS_ICON = LoadIcon(PRODUCTS_PATH);
    public static final ImageIcon PRODUCT_ICON = LoadIcon(PRODUCT_PATH);
    public static final ImageIcon BOX_ICON = LoadIcon(BOX_PATH);
    public static final ImageIcon PRODUCT_CONFIG_ICON = LoadIcon(PRODUCT_CONFIG_PATH);
    public static final ImageIcon SEARCH_ICON = LoadIcon(SEARCH_PATH);
    public static final ImageIcon ORDER_HISTORY_ICON = LoadIcon(ORDER_HISTORY_PATH);
    public static final ImageIcon CART_ICON = LoadIcon(CART_PATH);
    public static final ImageIcon CART_ADD_ICON = LoadIcon(CART_ADD_PATH);
    public static final ImageIcon EXIT_ICON = LoadIcon(EXIT_PATH);
    public static final ImageIcon ENTER_ICON = LoadIcon(ENTER_PATH);
    public static final ImageIcon BACK_ICON = LoadIcon(BACK_PATH);
    public static final ImageIcon ADD_ICON = LoadIcon(ADD_PATH);
    public static final ImageIcon EDIT_ICON = LoadIcon(EDIT_PATH);
    public static final ImageIcon KEYS_ICON = LoadIcon(KEYS_PATH);
    public static final ImageIcon UNACTIVE_ICON = LoadIcon(UNACTIVE_PATH);
    public static final ImageIcon LINKEDIN_ICON = LoadIcon(LINKEDIN_PATH);
    public static final ImageIcon GITHUB_ICON = LoadIcon(GITHUB_PATH);

    public static ImageIcon LoadIcon(String resourcePath) {
        try {
            ImageIcon icon = new ImageIcon(UIUtils.class.getResource(resourcePath));
            if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
                System.err.println("Error loading icon: " + resourcePath);
                return null;
            }
            return icon;
        } catch (Exception e) {
            System.err.println("Error loading icon: " + resourcePath);
            return null;
        }
    }

    public static ImageIcon getIcon(String iconPath) {
        return LoadIcon(iconPath);
    }

    
}