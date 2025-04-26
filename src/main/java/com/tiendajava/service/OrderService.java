package com.tiendajava.service;

import java.util.List;

import com.google.gson.Gson;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Order;
import com.tiendajava.repository.OrderRepository;

public class OrderService {

    private final OrderRepository repository = new OrderRepository();
    private final Gson gson = new Gson();

    public ApiResponse<Order> createOrder(Order order) {
        String json = gson.toJson(order);
        return repository.createOrder(json);
    }

    public ApiResponse<List<Order>> getMyOrders() {
        return repository.getMyOrders();
    }

    public ApiResponse<List<Order>> getAllOrders() {
        return repository.getAllOrders();
    }

    public ApiResponse<Order> getOrderById(int id) {
        return repository.getOrderById(id);
    }

    public ApiResponse<Order> updateOrderStatus(int id, String newStatus) {
        String json = String.format("{\"status\": \"%s\"}", newStatus);
        return repository.updateOrderStatus(id, json);
    }
}
