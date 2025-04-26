package com.tiendajava.repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.tiendajava.model.ApiResponse;

public abstract class BaseRepository {
  protected static final String URL_BASE = "http://localhost:5000/api/";
  protected final HttpClient client = HttpClient.newHttpClient();
  protected final Gson gson = new Gson();

  protected <T> ApiResponse<T> sendRequest(HttpRequest request, Type responseType) {
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return gson.fromJson(response.body(), responseType);
    } catch (IOException | InterruptedException e) {
      return new ApiResponse<>(false, null, "Server error: " + e.getMessage());
    }
  }
}
