package com.tiendajava.repository;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Order;
import com.tiendajava.model.Session;

public class OrderRepository extends BaseRepository {

    public ApiResponse<Order> createOrder(String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "orders"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Session.getInstance().getToken())
                .POST(BodyPublishers.ofString(json))
                .build();

        Type type = new TypeToken<ApiResponse<Order>>() {}.getType();
        return sendRequest(request, type);
    }

    public ApiResponse<List<Order>> getMyOrders() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "orders"))
                .header("Authorization", "Bearer " + Session.getInstance().getToken())
                .GET()
                .build();

        Type type = new TypeToken<ApiResponse<List<Order>>>() {}.getType();
        return sendRequest(request, type);
    }

    public ApiResponse<List<Order>> getAllOrders() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "orders/all"))
                .header("Authorization", "Bearer " + Session.getInstance().getToken())
                .GET()
                .build();

        Type type = new TypeToken<ApiResponse<List<Order>>>() {}.getType();
        return sendRequest(request, type);
    }

    public ApiResponse<Order> getOrderById(int id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "orders/" + id))
                .header("Authorization", "Bearer " + Session.getInstance().getToken())
                .GET()
                .build();

        Type type = new TypeToken<ApiResponse<Order>>() {}.getType();
        return sendRequest(request, type);
    }

    public ApiResponse<Order> updateOrderStatus(int id, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "orders/" + id + "/status"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Session.getInstance().getToken())
                .method("PATCH", BodyPublishers.ofString(json))
                .build();

        Type type = new TypeToken<ApiResponse<Order>>() {}.getType();
        return sendRequest(request, type);
    }
}
