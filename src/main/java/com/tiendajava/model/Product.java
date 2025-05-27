package com.tiendajava.model;

import java.math.BigDecimal;

public class Product {

    private int product_id;
    private int category_id;
    private Category Category; // ← Campo exacto que espera el backend
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private String image;
    private String created_at;

    public Product() {}

    public Product(String name, String description, BigDecimal price, int stock, int category_id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category_id = category_id;
    }

    // Getters y Setters

    public int getProduct_id() { return product_id; }
    public void setProduct_id(int product_id) { this.product_id = product_id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getCategory_id() { return category_id; }
    public void setCategory_id(int category_id) { this.category_id = category_id; }

    public Category getCategory() { return Category; }
    public void setCategory(Category category) { this.Category = category; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    @Override
    public String toString() {
        return "Product{" +
                "product_id=" + product_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", category_id=" + category_id +
                ", created_at='" + created_at + '\'' +
                ", category=" + (Category != null ? Category.getCategory_name() : "null") +
                '}';
    }
}
