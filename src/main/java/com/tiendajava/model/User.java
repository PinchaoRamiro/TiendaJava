package com.tiendajava.model;

public class User {
  /*
   * Crear una clase llamada Usuario con los siguientes atributos o propiedades
privadas y con sus respectivos métodos getters y setters:
• id de tipo numérico
• nombre de tipo cadena
• apellido de tipo cadena
• email de tipo cadena
• tipo de documento de identificación de tipo cadena
• número de documento de identificación de tipo cadena
• dirección de tipo cadena
• teléfono de tipo cadena
• estado activo o inactivo de tipo boolean.
• password de tipo cadena
   */

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

  // Getters
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

  public String getPassword() {
    return password;
  }



  // Setters
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
    return "User [id=" + id + ", name=" + name + ", lastName=" + lastname + ", email=" + email + ", typeDocument=" + typeDocument + ", numDocument=" + numDocument + ", address=" + address + ", phone=" + phone + ", status=" + status + ", password=" + password + "]";
  }
}
