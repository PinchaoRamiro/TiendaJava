package com.tiendajava.service;

import java.util.List;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.DashboardSummary;
import com.tiendajava.model.Product;
import com.tiendajava.model.orders.OrderStatusReport;
import com.tiendajava.repository.ReportRepository;

public class ReportService {

    private final ReportRepository reportRepository = new ReportRepository();

    public ApiResponse<DashboardSummary> getDashboardSummary() {
        return reportRepository.getDashboardSummary();
    }

    public ApiResponse<List<Product>> getOutOfStockProducts() {
        return reportRepository.getOutOfStockProducts();
    }

    public ApiResponse<List<OrderStatusReport>> getOrdersByStatus() {
        return reportRepository.getOrdersByStatus();
    }

    public ApiResponse<List<Product>> getLowStockProducts(int i) {
        ProductService productService = new ProductService();
        ApiResponse<List<Product>> response = productService.getAllProducts();

        if (response.isSuccess()) {
            List<Product> products = response.getData();
            List<Product> lowStockProducts = products.stream()
                    .filter(product -> product.getStock() < i)
                    .toList();
            return new ApiResponse<List<Product>>(true, lowStockProducts, "Low stock products retrieved successfully");
        } else {
            return new ApiResponse<List<Product>>(false, null, "Failed to retrieve products: " + response.getMessage());
        }
    }
}
