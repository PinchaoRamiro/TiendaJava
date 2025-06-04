package com.tiendajava.repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tiendajava.utils.ApiResponse;

public abstract class BaseRepository {
    protected static final String URL_BASE = "https://tienda-backend-381g.onrender.com/api/";
    protected final HttpClient client = HttpClient.newHttpClient();
    protected final Gson gson = new Gson();

    protected <T> ApiResponse<T> sendRequest(HttpRequest request, Type responseType) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ApiResponse<T> apiResponse = gson.fromJson(response.body(), responseType);

            if(response.statusCode() == 404)
                return new ApiResponse<>(false, null, "Resource not found");
            if (response.statusCode() >= 300) {
                return new ApiResponse<>(false, null,
                        "Error: " + response.statusCode() + " - " + apiResponse.getMessage());
            }
            
            String body = response.body().trim();
    
            if (body.startsWith("\"") && body.endsWith("\"")) {
                apiResponse.setStatusCode(response.statusCode());
                return apiResponse;
            }

            if ((body.startsWith("[") && body.endsWith("]")) || (body.startsWith("{") && body.endsWith("}"))) {
                apiResponse.setStatusCode(response.statusCode());
                return apiResponse;
            }
    
            return new ApiResponse<>(false, null,
                    "Unexpected response: " + response.body());
    
        } catch (IOException | InterruptedException e) {
            return new ApiResponse<>(false, null,
                    "Server error: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            return new ApiResponse<>(false, null,
                    "Unexpected response: " + e.getMessage());
        }
    }
    
}