package com.tiendajava.model.ProductsModels;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tiendajava.model.Product;

public class ProductFactory {
    public static Product createProduct(JsonObject json) {
        String category = json.get("Category").getAsString();

        switch (category) {
            case "Clothing" -> {
                ClothingProduct clothing = new Gson().fromJson(json, ClothingProduct.class);
                return clothing;
            }
            case "Electronics" -> {
                ElectronicsProduct electronics = new Gson().fromJson(json, ElectronicsProduct.class);
                return electronics;
            }
            case "Furniture" -> {
                FurnitureProduct furniture = new Gson().fromJson(json, FurnitureProduct.class);
                return furniture;
            }
            default -> {
                return new Gson().fromJson(json, Product.class);
            }
        }
    }
}
