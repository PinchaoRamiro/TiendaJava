package com.tiendajava.model;

public class Session {
    private static Session instance;
    private String token = null;
    private User user = null;
    private String role = null;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public boolean isLogged(){
        return token != null;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void clearSession() {
        token = null;
        user = null;
        role = null;
    }

    public void setRole(String role) {
      this.role = role;
    }
    
    public String getRole() {
        return role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
