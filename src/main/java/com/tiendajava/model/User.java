package com.tiendajava.model;

import java.util.List;

public class User {

  private int id;
  private String name;
  private String lastname;
  private String email;
  private String typeDocument;
  private String numDocument; 
  private String address;
  private String phone;
  private boolean status;
  private String password;
  private String role;

  

  private List<Order> orders; // Assuming a user can have multiple orders

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return this.name;
  }

  public String getLastName() {
    return lastname;
  }

  public String getEmail() {
    return email;
  }

  public String getTypeDocument() {
    return typeDocument;
  }

  public String getNumDocument() {
    return numDocument;
  }

  public String getAddress() {
    return address;
  }

  public String getPhone() {
    return phone;
  }

  public boolean getStatus() {
    return status;
  }


  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLastName(String lastname) {
    this.lastname = lastname;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setTypeDocument(String documentType) {
    this.typeDocument = documentType;
  }

  public void setNumDocument(String documentNumber) {
    this.numDocument = documentNumber;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", name=" + name + ", lastName=" + lastname + ", email=" + email + ", typeDocument=" + typeDocument + ", numDocument=" + numDocument + ", address=" + address + ", phone=" + phone + ", status=" + status + "]";
  }

    public String getPassword() {
        return password;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
