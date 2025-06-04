package com.tiendajava.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tiendajava.model.Category;
import com.tiendajava.model.Product;
import com.tiendajava.model.ProductsModels.ClothingProduct;
import com.tiendajava.model.ProductsModels.ElectronicsProduct;
import com.tiendajava.model.ProductsModels.FurnitureProduct;

public class ProductFactory {
    public static Product createProduct(JsonObject json) {
        Category objCaregory = new Gson().fromJson(json.get("Category"), Category.class);
        String category = objCaregory.getCategory_name();

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
