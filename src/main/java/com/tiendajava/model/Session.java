package com.tiendajava.model;

public class Session {
    private static Session instance;
    private String token;
    private String userEmail;
    private String role;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void clearSession() {
        token = null;
        userEmail = null;
        role = null;
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setRole(String role) {
      this.role = role;
    }
    
    public String getRole() {
        return role;
    }
}
