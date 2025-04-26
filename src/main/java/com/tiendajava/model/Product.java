package com.tiendajava.model;

import java.math.BigDecimal;

public class Product {

    private int product_id;
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private int category_id;
    private String createdAt;

    public Product() {
    }

    public Product(int product_id, String name, String description, BigDecimal price, int stock, int category_id, String createdAt) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category_id = category_id;
        this.createdAt = createdAt;
    }

    // Getters
    public int getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getCategory_id() {
        return category_id;
    }

    // Setters
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
