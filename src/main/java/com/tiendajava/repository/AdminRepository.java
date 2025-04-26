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
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "admin/register-admin"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        return sendRequest(request, User.class);
    }

    public ApiResponse<String> logoutAdmin() {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "admin/logout"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();

        return sendRequest(request, String.class);
    }

    public ApiResponse<List<User>> getAllUsers() {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "admin/users"))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .GET()
            .build();

        Type listType = new TypeToken<ApiResponse<List<User>>>(){}.getType();
        return sendRequest(request, listType);
    }

    public ApiResponse<List<User>> getAllAdmins() {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "admin/admins"))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .GET()
            .build();

        Type listType = new TypeToken<ApiResponse<List<User>>>(){}.getType();
        return sendRequest(request, listType);
    }

    public ApiResponse<User> updateUserRole(String json, int userId) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "admin/user-role/" + userId))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
            .build();

        return sendRequest(request, User.class);
    }

    public ApiResponse<String> deleteUser(int userId) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(URL_BASE + "admin/user/" + userId))
            .header("Authorization", "Bearer " + Session.getInstance().getToken())
            .DELETE()
            .build();

        return sendRequest(request, String.class);
    }
}
