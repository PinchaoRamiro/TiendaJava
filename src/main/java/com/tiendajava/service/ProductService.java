package com.tiendajava.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.tiendajava.model.Product;
import com.tiendajava.repository.ProductRepository;
import com.tiendajava.utils.ApiResponse;

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
        ApiResponse<Product> response = productRepository.getProductById(id);
        return response;
    }

    /**
     * Crear un nuevo producto
     */
    public ApiResponse<Product> createProductWithImage(Product p, File image, String category) {

         // Crear el producto con la imagen
        try {
            return productRepository.createProductWithImage(p, image, category);
        } catch (IOException | InterruptedException e) {
            return new ApiResponse<>(false, null, "Error al subir imagen: " + e.getMessage());
        }
    }

    /**
     * Actualizar un producto existente
     */
    public ApiResponse<Product> updateProduct(Product product) {
        String json = gson.toJson(product);
        return productRepository.updateProduct(json, product.getProduct_id());
    }    

    /**
     * Eliminar un producto por su ID
     */
    public ApiResponse<String> deleteProduct(int id) {
        return productRepository.deleteProduct(id);
    }


    /**
     * Obtener productos por nombre
     */
    public ApiResponse<List<Product>> getProductsByName(String name) {
        return productRepository.searchProducts(name);
    }

    /**
     * Obtener productos por categoria
     */
    public ApiResponse<List<Product>> getProductsByCategory(int categoryId) {
        return productRepository.getProductsByCategory(categoryId);
    }

    /*
     * obtener productos ente un rango de precios
     */
    public ApiResponse<List<Product>> getProductsByPriceRange(double minPrice, double maxPrice) {
        return productRepository.getProductsByPriceRange(minPrice, maxPrice);
    }

    /**
     * Obtener Stock de un producto por su ID
     */
    public int getStockByProductId(int productId) {
        ApiResponse<Product> response = productRepository.getProductById(productId);
        if (response.isSuccess() && response.getData() != null) {
            return response.getData().getStock();
        } else {
            return 0;
        }
    }

    public ApiResponse<List<Product>> getFeaturedProducts() {
        ApiResponse<List<Product>> response = productRepository.getAllProducts();
        if (response.isSuccess() && response.getData() != null) {
            List<Product> products = response.getData();
            if (products.size() > 5) {
                products = products.subList(0, 5);
            }
            return new ApiResponse<>(true, products, "Productos destacados obtenidos correctamente");
        } else {
            return new ApiResponse<>(false, null, "Error al obtener productos destacados");
        }
    }

    public ApiResponse<List<Product>> getUserProducts() {
        ApiResponse<List<Product>> response = productRepository.getAllProducts();
        if (response.isSuccess() && response.getData() != null) {
            List<Product> products = response.getData().stream().filter(p -> p.getStock() > 0).toList();
            return new ApiResponse<>(true, products, "Productos obtenidos correctamente");
        } else {
            return new ApiResponse<>(false, null, "Error al obtener productos");
        }
    }
}
