package com.tiendajava.repository;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.model.Session;
import com.tiendajava.model.ProductsModels.ProductFactory;

public class ProductRepository extends BaseRepository {

  public ApiResponse<List<Product>> getAllProducts() {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(URL_BASE + "product/get/"))
          .header("Authorization", "Bearer " + Session.getInstance().getToken())
          .GET()
          .build();

      try {
          HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

          JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

          boolean success = json.get("success").getAsBoolean();
          String message = json.get("message").getAsString();

          List<Product> products = new ArrayList<>();
          if (success) {
              JsonArray data = json.get("data").getAsJsonArray();
              for (JsonElement el : data) {
                  Product p = ProductFactory.createProduct(el.getAsJsonObject());
                  products.add(p);
              }
          }

          return new ApiResponse<>(success, products, message);

      } catch (InterruptedException | IOException e) {
          return new ApiResponse<>(false, null, "Error: " + e.getMessage());
      }
  }

  /**
   * Obtiene un producto por su ID.
   */
  public ApiResponse<Product> getProductById(int id) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "product/get/" + id))
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .GET()
        .build();

    Type type = new TypeToken<ApiResponse<Product>>(){}.getType();
    return sendRequest(request, type);
  }

  /**
   * Crea un nuevo producto (requiere token de administrador).
   */
  public ApiResponse<Product> createProductWithImage(Product prod, File imageFile, String category) throws IOException, InterruptedException {

    System.err.println("Creating product: " + prod);
    System.err.println("Image file: " + imageFile);
    System.err.println("Category: " + category);
    // if image is null, not required to upload
    HttpRequest request;
    if (imageFile == null) {
      request = HttpRequest.newBuilder()
          .uri(URI.create(URL_BASE + "product/create/" + category))
          .header("Authorization", "Bearer " + Session.getInstance().getToken())
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(prod)))
          .build();

    }else{
    // otherwise, upload image and create product
    String boundary = "----TiendajavaBoundary" + System.currentTimeMillis();
    var body = buildMultipartBody(prod, imageFile, boundary);

    request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "product/create"))
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .header("Content-Type", "multipart/form-data; boundary=" + boundary)
        .POST(body)
        .build();
    }

    Type type = new TypeToken<ApiResponse<Product>>(){}.getType();
    return sendRequest(request, type);
  }

  private HttpRequest.BodyPublisher buildMultipartBody(Product p, File img, String boundary) throws IOException {
      var sep = "--" + boundary + "\r\n";
      var crlf = "\r\n";
      var bb = new StringBuilder();

      // campo name
      bb.append(sep)
        .append("Content-Disposition: form-data; name=\"name\"").append(crlf).append(crlf)
        .append(p.getName()).append(crlf);

      // campo price
      bb.append(sep)
        .append("Content-Disposition: form-data; name=\"price\"").append(crlf).append(crlf)
        .append(p.getPrice()).append(crlf);

      // stock
      bb.append(sep)
        .append("Content-Disposition: form-data; name=\"stock\"").append(crlf).append(crlf)
        .append(p.getStock()).append(crlf);

      // category_id
      bb.append(sep)
        .append("Content-Disposition: form-data; name=\"category_id\"").append(crlf).append(crlf)
        .append(p.getCategory_id()).append(crlf);

      // description
      bb.append(sep)
        .append("Content-Disposition: form-data; name=\"description\"").append(crlf).append(crlf)
        .append(p.getDescription()).append(crlf);

      // parte del fichero
      bb.append(sep)
        .append("Content-Disposition: form-data; name=\"image\"; filename=\"")
        .append(img.getName()).append("\"").append(crlf)
        .append("Content-Type: ").append(Files.probeContentType(img.toPath())).append(crlf).append(crlf);

      var byteArrays = new ArrayList<byte[]>();
      byteArrays.add(bb.toString().getBytes(StandardCharsets.UTF_8));
      byteArrays.add(Files.readAllBytes(img.toPath()));
      byteArrays.add(crlf.getBytes(StandardCharsets.UTF_8));

      // cierre del multipart
      byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));

      return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
  }
  /**
   * Actualiza un producto existente por ID (requiere token de administrador).
   */
  public ApiResponse<Product> updateProduct(String json, int id) {
    try {
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "product/update/" + id))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .PUT(HttpRequest.BodyPublishers.ofString(json))
        .build();

        Type type = new TypeToken<ApiResponse<Product>>() {}.getType();
        return sendRequest(request, type);        

    } catch (Exception e) {
        return new ApiResponse<>(false, null, "Error updating product: ");
    }
  }

  /**
   * Elimina un producto por ID (requiere token de administrador).
   */
  public ApiResponse<String> deleteProduct(int id) {
    try{
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "product/delete/" + id))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .DELETE()
        .build();

        Type type = new TypeToken<ApiResponse<String>>() {}.getType();
        return sendRequest(request, type);        

    } catch (Exception e) {
        return new ApiResponse<>(false, null, "Error deleting product: ");
    }

  }

  public ApiResponse<List<Product>> getProductsByCategory(int categoryId) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "product/get/category/" + categoryId))
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .GET()
        .build();

    Type responseType = new TypeToken<ApiResponse<List<Product>>>() {
    }.getType();
    return sendRequest(request, responseType);
  }
  /**
   * Busca productos por nombre (requiere token de administrador).
   */
  public ApiResponse<List<Product>> searchProducts(String search) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "product/search?q=" + search))
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .GET()
        .build();

      Type responseType = new TypeToken<ApiResponse<List<Product>>>() {
      }.getType();
      return sendRequest(request, responseType);
    } catch (Exception e) {
      return new ApiResponse<>(false, null, "Error searching products: " + e.getMessage());
    }
  }
  
  /**
   * Obtiene productos por rango de precio (requiere token de administrador).
   */
  public ApiResponse<List<Product>> getProductsByPriceRange(double minPrice, double maxPrice) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "product/get/price/" + minPrice + "/" + maxPrice))
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .GET()
        .build();

    Type responseType = new TypeToken<ApiResponse<List<Product>>>() {
    }.getType();
    return sendRequest(request, responseType);
  }

}
