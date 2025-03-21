package parcial;
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
  private String lastName;
  private String email;
  private String documentType;
  private String documentNumber; 
  private String address;
  private String phone;
  private boolean status;
  private String password;

  // Getters
  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getDocumentType() {
    return documentType;
  }

  public String getDocumentNumber() {
    return documentNumber;
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

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }

  public void setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
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
}
