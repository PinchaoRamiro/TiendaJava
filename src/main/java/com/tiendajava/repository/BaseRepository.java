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
    protected static final String URL_BASE = "https://tienda-backend-381g.onrender.com/api/";
    protected final HttpClient client = HttpClient.newHttpClient();
    protected final Gson gson = new Gson();

    protected <T> ApiResponse<T> sendRequest(HttpRequest request, Type responseType) {
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body().trim();

            
            System.out.println("Response body: " + body);
            System.out.println("Status code: " + response.statusCode());
    
            if (body.startsWith("\"") && body.endsWith("\"")) {
                String msg = gson.fromJson(body, String.class);
                return new ApiResponse<>(false, null, msg);
            }

            if (body.startsWith("[")) {
                return new ApiResponse<>(true,
                                         gson.fromJson(body, responseType),
                                         "OK");
            }
            return gson.fromJson(body, responseType);
    
        } catch (IOException | InterruptedException e) {
            return new ApiResponse<>(false, null,
                    "Server error: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            return new ApiResponse<>(false, null,
                    "Unexpected response: " + e.getMessage());
        }
    }
    
}
