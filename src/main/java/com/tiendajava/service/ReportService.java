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
}
