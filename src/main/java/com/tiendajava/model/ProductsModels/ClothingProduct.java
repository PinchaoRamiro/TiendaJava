package com.tiendajava.model.ProductsModels;

import java.math.BigDecimal;

import com.tiendajava.model.Product;

public class ClothingProduct extends Product {
    private String Size;
    private String Color;
    private String Material;

    public ClothingProduct() {
    }

    public ClothingProduct(int product_id, String name, String description, BigDecimal price, int stock,
                        int category_id, String createdAt, String size, String color, String material) {
        super(product_id, name, description, price, stock, category_id, createdAt);
        this.Size = size;
        this.Color = color;
        this.Material = material;
    }

    public ClothingProduct(int product_id, String name, String description, BigDecimal price, int stock, int category_id, String createdAt) {
        super(product_id, name, description, price, stock, category_id, createdAt);
    }


    public String getSize() {
        return Size;
    }
    public void setSize(String size) {
        this.Size = size;
    }
    public String getColor() {
        return Color;
    }
    public void setColor(String color) {
        this.Color = color;
    }
    public String getMaterial() {
        return Material;
    }
    public void setMaterial(String material) {
        this.Material = material;
    }

    @Override
    public String toString() {
        return "ClothingProduct [size=" + Size + ", color=" + Color + ", material=" + Material + "]";
    }

    // Getters & Setters...
}
