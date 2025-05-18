package com.tiendajava.repository;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Session;
import com.tiendajava.model.User;

public class AdminRepository extends BaseRepository {

    public ApiResponse<User> registerAdmin(String json) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "admin/register-admin"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

            Type type = new TypeToken<ApiResponse<User>>() {}.getType();
            return sendRequest(request, type);        
            
        } catch ( Exception e) {
            return new ApiResponse<>(false, null, "Error to register admin: " + e.getMessage() );
        }

    }

    public ApiResponse<String> logoutAdmin() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "admin/logout"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();

            Type type = new TypeToken<ApiResponse<String>>() {}.getType();
            return sendRequest(request, type);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to logout admin");
        }
    }

    public ApiResponse<List<User>> getAllUsers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "admin/users"))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .GET()
            .build();

            Type listType = new TypeToken<ApiResponse<List<User>>>(){}.getType();
            return sendRequest(request, listType);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to get all users");
        }
    }

    public ApiResponse<List<User>> getAllAdmins() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "admin/admins"))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .GET()
            .build();

            Type listType = new TypeToken<ApiResponse<List<User>>>(){}.getType();
            return sendRequest(request, listType);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to get all admins");
        }
    }

    public ApiResponse<String> deleteUser(int userId) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "admin/user/" + userId))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .DELETE()
            .build();

            Type type = new TypeToken<ApiResponse<String>>() {}.getType();
            return sendRequest(request, type);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to delete user");
        }
    }

    public ApiResponse<List<User>> searchAdmins(String name){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "admin/search-admin?q=" + name))
                .header("Authorization", "Bearer " + Session.getInstance().getToken())
                .GET()
                .build();

            Type responseType = new TypeToken<ApiResponse<List<User>>>() {
            }.getType();
            return sendRequest(request, responseType);
        } catch (Exception e) {
        return new ApiResponse<>(false, null, "Error searching products: " + e.getMessage());
        }
    }

    public ApiResponse<List<User>> searchUsers(String name){
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "admin/search-user?q=" + name))
                .header("Authorization", "Bearer " + Session.getInstance().getToken())
                .GET()
                .build();

            Type responseType = new TypeToken<ApiResponse<List<User>>>() {
            }.getType();
            return sendRequest(request, responseType);
        } catch (Exception e) {
        return new ApiResponse<>(false, null, "Error searching products: " + e.getMessage());
        }
    }
}
