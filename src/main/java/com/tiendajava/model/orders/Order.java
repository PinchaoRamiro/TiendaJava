package com.tiendajava.model.orders;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Order {

    private int order_id;
    private int user_id;
    private OrderStatusEnum status;
    private BigDecimal total_amount;
    @SerializedName("created_at")
    private String createdAt;
    private String shipping_address;
    private String payment_method; 

    @SerializedName("OrderItems")
    private List<OrderItem> orderItems;

    public Order(int order_id, int user_id, OrderStatusEnum status, BigDecimal total_amount, String created_at, String shipping_address, String payment_method, List<OrderItem> OrderItems) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.status = status;
        this.total_amount = total_amount;
        this.createdAt = created_at;
        this.shipping_address = shipping_address;
        this.payment_method = payment_method;
        this.orderItems = OrderItems;
    }

    // Constructor sin order_id y order_date para la creación inicial (serán asignados por el backend)
    public Order(int user_id, BigDecimal total_amount, OrderStatusEnum status, String shipping_address, String payment_method, List<OrderItem> order_items) {
        this.user_id = user_id;
        this.total_amount = total_amount;
        this.status = status;   
        this.shipping_address = shipping_address;
        this.payment_method = payment_method; 
        this.orderItems = order_items;
    }

    public Order() {
    }

    // Getters
    public int getOrder_id() {
        return order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }
    // Setters
    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = OrderStatusEnum.valueOf(status);
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getCreatedAt() {
            // Formato esperado: "2023-10-01T12:00:00Z"
            // mejorar el formato si es necesario
        if (createdAt != null) {
            String[] parts = createdAt.split("T");
                if (parts.length > 1) {
                    return parts[0] ;
                }
        }

        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public enum OrderStatusEnum {
        Approved, Rejected, Pending
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_id=" + order_id +
                ", user_id=" + user_id +
                ", status=" + status +
                ", total_amount=" + total_amount +
                ", createdAt='" + createdAt + '\'' +
                ", shipping_address='" + shipping_address + '\'' +
                ", payment_method='" + payment_method + '\'' +
                ", orderItems=" + orderItems +
                '}';
    }
}
