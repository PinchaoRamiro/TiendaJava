package com.tiendajava.repository;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Product;
import com.tiendajava.model.OrderStatusReport;
import com.tiendajava.model.DashboardSummary;
import com.tiendajava.model.Session;

public class ReportRepository extends BaseRepository {

    private static final String BASE_ENDPOINT = URL_BASE + "admin/report/";

    public ApiResponse<DashboardSummary> getDashboardSummary() {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_ENDPOINT + "dashboard"))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .GET()
            .build();

        Type type = new TypeToken<ApiResponse<DashboardSummary>>(){}.getType();
        return sendRequest(request, type);
    }

    public ApiResponse<List<Product>> getOutOfStockProducts() {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_ENDPOINT + "products/out-of-stock"))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .GET()
            .build();

        Type type = new TypeToken<ApiResponse<List<Product>>>(){}.getType();
        return sendRequest(request, type);
    }

    public ApiResponse<List<OrderStatusReport>> getOrdersByStatus() {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_ENDPOINT + "orders/status"))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .GET()
            .build();

        Type type = new TypeToken<ApiResponse<List<OrderStatusReport>>>(){}.getType();
        return sendRequest(request, type);
    }
}
