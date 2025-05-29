package com.tiendajava.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tiendajava.model.orders.OrderItem;

public class Cart {

    private final List<Product> items = new ArrayList<>();

    public void addItem(Product productToAdd, int quantity) {
        if (productToAdd == null || quantity <= 0) return;

        for (Product item : items) {
            if (item.getProduct_id() == productToAdd.getProduct_id()) {
                if (item.getStock() + quantity > productToAdd.getStock()) return;
                item.setStock(item.getStock() + quantity);
                return;
            }
        }

        Product newItem = new Product(
            productToAdd.getName(),
            productToAdd.getDescription(),
            productToAdd.getPrice(),
            quantity,
            productToAdd.getCategory_id()
        );
        newItem.setProduct_id(productToAdd.getProduct_id());
        items.add(newItem);
    }

    public void removeItem(int productId) {
        items.removeIf(item -> item.getProduct_id() == productId);
    }

    public void updateItemQuantity(int productId, int newQuantity) {
        if (newQuantity <= 0) {
            removeItem(productId);
        } else {
            for (Product item : items) {
                if (item.getProduct_id() == productId) {
                    item.setStock(newQuantity);
                    return;
                }
            }
        }
    }

    public void clearCart() {
        items.clear();
    }

    public List<Product> getItems() {
        return Collections.unmodifiableList(items);
    }

    public BigDecimal getTotalSubtotal() {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<OrderItem> toOrderItems() {
        return items.stream()
                .map(item -> new OrderItem(  item.getProduct_id(), item.getStock(), item.getPrice()))
                .toList();
    }
}
