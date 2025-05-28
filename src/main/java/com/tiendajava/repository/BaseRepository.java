package com.tiendajava.repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tiendajava.model.ApiResponse;

public abstract class BaseRepository {
    protected static final String URL_BASE = "http://localhost:5000/api/";
    protected final HttpClient client = HttpClient.newHttpClient();
    protected final Gson gson = new Gson();

    protected <T> ApiResponse<T> sendRequest(HttpRequest request, Type responseType) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ApiResponse<T> apiResponse = gson.fromJson(response.body(), responseType);

            System.out.println("Http response " + response);
            System.out.println("Response: " + apiResponse);
            String body = response.body().trim();
    
            if (body.startsWith("\"") && body.endsWith("\"")) {
                // String msg = gson.fromJson(body, String.class);
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