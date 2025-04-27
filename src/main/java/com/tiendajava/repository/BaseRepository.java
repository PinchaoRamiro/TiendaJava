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
            String body = response.body().trim();

            System.out.println("Response: " + body);
            System.out.println("Status code: " + response.statusCode());
    
            // 1) Si llega algo entre comillas: lo tomamos como mensaje de error/aviso
            if (body.startsWith("\"") && body.endsWith("\"")) {
                String msg = gson.fromJson(body, String.class);
                return new ApiResponse<>(false, null, msg);
            }
    
            // 2) Si viene un array y esperas objeto: también puedes detectarlo
            if (body.startsWith("[")) {
                // Por ejemplo, podrías convertirlo en data=list y dejar success=true
                // ó bien envolverlo en un ApiResponse manual:
                return new ApiResponse<>(true,
                                         gson.fromJson(body, responseType),
                                         "OK");
            }
    
            // 3) Finalmente, el caso normal de objeto JSON
            return gson.fromJson(body, responseType);
    
        } catch (IOException | InterruptedException e) {
            return new ApiResponse<>(false, null,
                    "Server error: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            // Si falló el parseo, devolvemos el texto crudo como message
            return new ApiResponse<>(false, null,
                    "Unexpected response: " + e.getMessage());
        }
    }
    
}
