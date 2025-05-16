package com.tiendajava.model.ProductsModels;

import java.math.BigDecimal;

import com.tiendajava.model.Product;

public class ElectronicsProduct extends Product {
    private String Voltage;
    private String Warranty;
    private String Brand;

    public ElectronicsProduct(int product_id, String name, String description, BigDecimal price, int stock,
        int category_id, String createdAt, String brand) {
        super(product_id, name, description, price, stock, category_id, createdAt);
        this.Brand = brand;
    }

    public ElectronicsProduct() {
    }

    public ElectronicsProduct(int product_id, String name, String description, BigDecimal price, int stock, int category_id, String createdAt) {
        super(product_id, name, description, price, stock, category_id, createdAt);
    }

    @Override
    public String toString() {
        return "ElectronicsProduct [voltage=" + Voltage + ", warranty=" + Warranty + ", brand=" + Brand + "]";
    }

    public String getVoltage() {
        return Voltage;
    }
    public void setVoltage(String voltage) {
        this.Voltage = voltage;
    }
    public String getWarranty() {
        return Warranty;
    }
    public void setWarranty(String warranty) {
        this.Warranty = warranty;
    }
    public String getBrand() {
        return Brand;
    }
    public void setBrand(String brand) {
        this.Brand = brand;
    }

    // Getters & Setters...
}
