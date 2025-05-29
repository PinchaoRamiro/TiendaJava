package com.tiendajava.repository;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Session;
import com.tiendajava.model.orders.OrderItem;

public class OrderItemRepository extends BaseRepository {

  private final String BASE_PATH = URL_BASE + "order-items/";

  public ApiResponse<OrderItem> createOrderItem(String json) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(BASE_PATH + "create/"))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + Session.getInstance().getToken())
          .POST(BodyPublishers.ofString(json))
          .build();

      Type type = new TypeToken<ApiResponse<OrderItem>>() {
      }.getType();
      return sendRequest(request, type);
    } catch (Exception e) {
      return new ApiResponse<>(false, null, "Error to create order item: " + e.getMessage());
    }
  }

  public ApiResponse<List<OrderItem>> getOrderItems(int orderId) {
    try {
        
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(BASE_PATH + "get/" + orderId))
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .GET()
        .build();

      Type type = new TypeToken<ApiResponse<List<OrderItem>>>() {
      }.getType();
      return sendRequest(request, type);
    } catch (Exception e) {
      return new ApiResponse<>(false, null, "Error to get order items");
    }
  }

  public ApiResponse<OrderItem> updateOrderItem(String json, int orderItemId) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(BASE_PATH + "update/" + orderItemId))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + Session.getInstance().getToken())
          .PUT(BodyPublishers.ofString(json))
          .build();

      Type type = new TypeToken<ApiResponse<OrderItem>>() {
      }.getType();
      return sendRequest(request, type);
    } catch (Exception e) {
      return new ApiResponse<>(false, null, "Error to update order item");
    }
  }

  public ApiResponse<String> deleteOrderItem(int orderItemId) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(BASE_PATH + "delete/" + orderItemId))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + Session.getInstance().getToken())
          .DELETE()
          .build();

      Type type = new TypeToken<ApiResponse<String>>() {
      }.getType();
      return sendRequest(request, type);
    } catch (Exception e) {
      return new ApiResponse<>(false, null, "Error to delete order item");
    }
  }
}
