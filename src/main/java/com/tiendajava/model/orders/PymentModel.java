package com.tiendajava.model.orders;

public class PymentModel {

  int payment_id;
  int order_id;
  Double amount;
  String paid_at;
  String payment_method;

  public PymentModel(int payment_id, int order_id, Double amount, String paid_at, String payment_method) {
    this.payment_id = payment_id;
    this.order_id = order_id;
    this.amount = amount;
    this.paid_at = paid_at;
    this.payment_method = payment_method;
  }

  public PymentModel() {}

  // If we want to create a new payment without an ID
  public PymentModel(int order_id, Double amount, String paid_at, String payment_method) {
    this.order_id = order_id;
    this.amount = amount;
    this.paid_at = paid_at;
    this.payment_method = payment_method;
  }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaid_at() {
        return paid_at;
    }

    public void setPaid_at(String paid_at) {
        this.paid_at = paid_at;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }
}
