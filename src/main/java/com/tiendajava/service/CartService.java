package com.tiendajava.service;

import java.math.BigDecimal;
import java.util.List;

import com.tiendajava.model.Cart;
import com.tiendajava.model.Product;

public class CartService {

    private final Cart cart;

    public CartService(Cart cart) {
        this.cart = cart;
    }

    public void addToCart(Product product, int quantity) {
        cart.addItem(product, quantity);
    }

    public void removeFromCart(int productId) {
        cart.removeItem(productId);
    }

    public void updateQuantity(int productId, int newQuantity) {
        cart.updateItemQuantity(productId, newQuantity);
    }

    public void clearCart() {
        cart.clearCart();
    }

    public List<Product> getCartItems() {
        return cart.getItems();
    }

    public BigDecimal getTotal() {
        return cart.getTotalSubtotal();
    }

    public boolean isEmpty() {
        return cart.isEmpty();
    }
}
