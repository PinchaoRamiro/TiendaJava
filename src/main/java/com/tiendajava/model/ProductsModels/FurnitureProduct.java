package com.tiendajava.model.ProductsModels;

import java.math.BigDecimal;

import com.tiendajava.model.Product;

public class FurnitureProduct extends Product {
    private String Dimensions;
    private String Material;
    private String woodType;

    
    @Override
    public String toString() {
        return "FurnitureProduct [dimensions=" + Dimensions + ", material=" + Material + ", woodType=" + woodType + "]";
    }

    public FurnitureProduct( String name, String description, BigDecimal price, int stock,
            int category_id, String dimensions, String material, String woodType) {
        super( name, description, price, stock, category_id);
        this.Dimensions = dimensions;
        this.Material = material;
        this.woodType = woodType;
    }
    
    public FurnitureProduct() {
    }

    public FurnitureProduct( String name, String description, BigDecimal price, int stock, int category_id, String createdAt) {
        super( name, description, price, stock, category_id);
    }


    public String getDimensions() {
        return Dimensions;
    }
    public void setDimensions(String dimensions) {
        this.Dimensions = dimensions;
    }
    public String getMaterial() {
        return Material;
    }
    public void setMaterial(String material) {
        this.Material = material;
    }
    public String getWoodType() {
        return woodType;
    }
    public void setWoodType(String woodType) {
        this.woodType = woodType;
    }

    // Getters & Setters...
}