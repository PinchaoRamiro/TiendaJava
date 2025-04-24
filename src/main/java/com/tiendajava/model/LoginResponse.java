package com.tiendajava.model;


public class LoginResponse {
    private String token;
    private User user;
    private String msg;
    private int status;

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public String getMsg() {
        return msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
