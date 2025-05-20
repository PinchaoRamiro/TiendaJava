package com.tiendajava.model;

public class ApiResponse<T> {
    private boolean success;
    private int statusCode;
    private final T data;
    private final String message;

    public ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}'
                + ", statusCode: " + statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
