package com.tiendajava.service;

import java.util.List;

import com.google.gson.Gson;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.orders.Order;
import com.tiendajava.repository.OrderRepository;

public class OrderService {

    private final OrderRepository repository = new OrderRepository();
    private Gson gson = new Gson();

    private final OrderRepository orderRepository;

    public OrderService() {
        this.orderRepository = new OrderRepository();
        this.gson = new Gson();
    }

    // Método para crear una orden, ahora acepta un objeto Order
    public ApiResponse<Order> createOrder(Order order) {
        if (order == null) {
            return new ApiResponse<>(false, null, "Order object cannot be null.");
        }
        // Convertir el objeto Order a JSON
        String orderJson = gson.toJson(order);
        System.out.println("Creating order with JSON: " + orderJson);
        return orderRepository.createOrder(orderJson);
    }

    public ApiResponse<List<Order>> getMyOrders() {
        return repository.getMyOrders();
    }

    public ApiResponse<List<Order>> getOrdersByUser(int userId) {
        return repository.getOrdersByUser(userId);
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
