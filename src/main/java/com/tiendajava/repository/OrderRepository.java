package com.tiendajava.repository;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Session;
import com.tiendajava.model.orders.Order;

public class OrderRepository extends BaseRepository {

    public ApiResponse<Order> createOrder(String json) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "order/create"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .POST(BodyPublishers.ofString(json))
            .build();

            Type type = new TypeToken<ApiResponse<Order>>() {}.getType();
            return sendRequest(request, type);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to create order");
        }
    }

    public ApiResponse<List<Order>> getMyOrders() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "order/me"))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .GET()
            .build();

            Type type = new TypeToken<ApiResponse<List<Order>>>() {}.getType();
            return sendRequest(request, type);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to get my orders");
        }
    }

    public ApiResponse<List<Order>> getOrdersByUser(int userId) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "order/user/" + userId))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .GET()
            .build();

        Type responseType = new TypeToken<ApiResponse<List<Order>>>() {}.getType();
        return sendRequest(request, responseType);
    }

    public ApiResponse<List<Order>> getAllOrders() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "order/all"))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .GET()
            .build();

            Type type = new TypeToken<ApiResponse<List<Order>>>() {}.getType();
            return sendRequest(request, type);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to get all orders");
        }
    }

    public ApiResponse<Order> getOrderById(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "order/" + id))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .GET()
            .build();   

            Type type = new TypeToken<ApiResponse<Order>>() {}.getType();
            return sendRequest(request, type);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to get order by id");
        }
    }

    public ApiResponse<Order> updateOrderStatus(int id, String json) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "order/" + id))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .PUT(BodyPublishers.ofString(json))
            .build();

            Type type = new TypeToken<ApiResponse<Order>>() {}.getType();
            return sendRequest(request, type);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to update order status");
        }
    }
}
