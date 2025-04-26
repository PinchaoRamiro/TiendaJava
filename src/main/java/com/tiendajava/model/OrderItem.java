package com.tiendajava.model;

import java.math.BigDecimal;
import java.util.List;

public class OrderItem {

    private int order_item_id;
    private int order_id;
    private int product_id;
    private int quantity;
    private BigDecimal price;

    private List<Product> products; // Assuming an order item is associated with a product
    private Order order; // Assuming an order item is associated with an order

    public OrderItem() {
    }

    public OrderItem(int order_item_id, int order_id, int product_id, int quantity, BigDecimal price) {
        this.order_item_id = order_item_id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public int getOrder_item_id() {
        return order_item_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    // Setters
    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
