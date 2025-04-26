package com.tiendajava.repository;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.model.Session;

public class ProductRepository extends BaseRepository {

  /**
   * Obtiene todos los productos.
   */
  public ApiResponse<List<Product>> getAllProducts() {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "product/get/"))
        .GET()
        .build();

    Type responseType = new TypeToken<ApiResponse<List<Product>>>() {
    }.getType();
    return sendRequest(request, responseType);
  }

  /**
   * Obtiene un producto por su ID.
   */
  public ApiResponse<Product> getProductById(int id) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "product/get/" + id))
        .GET()
        .build();

    return sendRequest(request, Product.class);
  }

  /**
   * Crea un nuevo producto (requiere token de administrador).
   */
  public ApiResponse<Product> createProduct(String json) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "product/create"))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

    return sendRequest(request, Product.class);
  }

  /**
   * Actualiza un producto existente por ID (requiere token de administrador).
   */
  public ApiResponse<Product> updateProduct(int id, String json) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "product/update/" + id))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .PUT(HttpRequest.BodyPublishers.ofString(json))
        .build();

    return sendRequest(request, Product.class);
  }

  /**
   * Elimina un producto por ID (requiere token de administrador).
   */
  public ApiResponse<String> deleteProduct(int id) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "product/delete/" + id))
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .DELETE()
        .build();

    return sendRequest(request, String.class);
  }
}
