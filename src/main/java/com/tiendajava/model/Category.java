package com.tiendajava.model;

import java.util.List;

public class Category {

    private int category_id;
    private String category_name;
    private List<Product> products; // Assuming a category can have multiple products

    public Category() {
    }

    public Category(int category_id, String category_name) {
        this.category_id = category_id;
        this.category_name = category_name;
    }

    // Getters
    public int getCategory_id() {
        return category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    // Setters
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return "Category{" +
               "category_id=" + category_id +
               ", category_name='" + category_name + '\'' +
               '}';
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
