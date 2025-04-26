package com.tiendajava.service;

import java.util.List;

import com.google.gson.Gson;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.repository.ProductRepository;

public class ProductService {

    private final ProductRepository productRepository = new ProductRepository();
    private final Gson gson = new Gson();

    /**
     * Obtener todos los productos
     */
    public ApiResponse<List<Product>> getAllProducts() {
        return productRepository.getAllProducts();
    }

    /**
     * Obtener un producto por su ID
     */
    public ApiResponse<Product> getProductById(int id) {
        return productRepository.getProductById(id);
    }

    /**
     * Crear un nuevo producto
     */
    public ApiResponse<Product> createProduct(Product product) {
        String json = gson.toJson(product);
        return productRepository.createProduct(json);
    }

    /**
     * Actualizar un producto existente
     */
    public ApiResponse<Product> updateProduct(int id, Product product) {
        String json = gson.toJson(product);
        return productRepository.updateProduct(id, json);
    }

    /**
     * Eliminar un producto por su ID
     */
    public ApiResponse<String> deleteProduct(int id) {
        return productRepository.deleteProduct(id);
    }
}
