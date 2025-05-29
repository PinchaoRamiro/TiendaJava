package com.tiendajava.repository;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.List;

import com.google.gson.reflect.TypeToken;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.Category;
import com.tiendajava.model.Session;

public class CategoryRepository extends BaseRepository {

    public ApiResponse<List<Category>> getAllCategories() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "category/get/"))
                .GET()
                .build();

            Type responseType = new TypeToken<ApiResponse<List<Category>>>() {}.getType();
            return sendRequest(request, responseType);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to get all categories");
        }
    }

    public ApiResponse<Category> getCategoryById(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "category/get/" + id))
                .GET()
                .build();

            Type responseType = new TypeToken<ApiResponse<Category>>() {}.getType();
            return sendRequest(request, responseType);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to get category by id");
        }
    }

    public ApiResponse<Category> createCategory(String json) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "category/create/"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Session.getInstance().getToken())
                .POST(BodyPublishers.ofString(json))
                .build();

            Type responseType = new TypeToken<ApiResponse<Category>>() {}.getType();
            return sendRequest(request, responseType);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to create category" );
        }
    }

    public ApiResponse<Category> updateCategory(int id, String json) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "category/update/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Session.getInstance().getToken())
                .PUT(BodyPublishers.ofString(json))
                .build();

            Type responseType = new TypeToken<ApiResponse<Category>>() {}.getType();
            return sendRequest(request, responseType);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to update category" );
        }
    }

    public ApiResponse<String> deleteCategory(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "category/delete/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Session.getInstance().getToken())
                .DELETE()
                .build();

            Type responseType = new TypeToken<ApiResponse<String>>() {}.getType();
            return sendRequest(request, responseType);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to delete category" );
        }
    }

    public ApiResponse<Category> getCategoryByName(String name) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + "category/get/name/" + name))
                .header("Content-Type", "application/json")
                .GET()
                .build();

            Type responseType = new TypeToken<ApiResponse<Category>>() {}.getType();
            return sendRequest(request, responseType);
        } catch (Exception e) {
            return new ApiResponse<>(false, null, "Error to get category by name" );
        }
    }

}
