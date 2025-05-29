package com.tiendajava.model.ProductsModels;

import java.math.BigDecimal;

import com.tiendajava.model.Product;

public class ClothingProduct extends Product {
    private String Size;
    private String Color;
    private String Material;

    public ClothingProduct() {
    }

    public ClothingProduct( String name, String description, BigDecimal price, int stock,
                        int category_id, String size, String color, String material) {
        super( name, description, price, stock, category_id);
        this.Size = size;
        this.Color = color;
        this.Material = material;
    }

    public ClothingProduct( String name, String description, BigDecimal price, int stock, int category_id, String createdAt) {
        super( name, description, price, stock, category_id);
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
