package com.tiendajava.service;

import java.util.List;

import com.google.gson.Gson;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.orders.OrderItem;
import com.tiendajava.repository.OrderItemRepository;

public class OrderItemService {
  private final OrderItemRepository repository = new OrderItemRepository();
  private final Gson gson = new Gson();

  public ApiResponse<OrderItem> createOrderItem(OrderItem item) {
    String json = gson.toJson(item);
    return repository.createOrderItem(json);
  }

  public ApiResponse<List<OrderItem>> getOrderItems(int orderId) {
    return repository.getOrderItems(orderId);
  }

  public ApiResponse<OrderItem> updateOrderItem(OrderItem item, int itemId) {
    String json = gson.toJson(item);
    return repository.updateOrderItem(json, itemId);
  }

  public ApiResponse<String> deleteOrderItem(int itemId) {
    return repository.deleteOrderItem(itemId);
  }
}
