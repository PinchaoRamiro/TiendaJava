package com.tiendajava.repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.LoginResponse;
import com.tiendajava.model.Session;
import com.tiendajava.model.User;

public class AdminRepository implements IRepository {

  public LoginResponse createAdmin(String json) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(URL_BASE + "admin/register-admin"))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + Session.getInstance().getToken())
          .POST(BodyPublishers.ofString(json))
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      LoginResponse loginResponse = gson.fromJson(response.body(), LoginResponse.class);

      if (response.statusCode() == 201) {
        loginResponse.setStatus(201);
        return loginResponse;
      }

      loginResponse.setStatus(response.statusCode());

      return loginResponse;
    } catch (java.net.http.HttpTimeoutException e) {
      System.err.println("Timeout Error: " + e.getMessage());
      return null;
    } catch (java.io.IOException e) {
      System.err.println("IO Error: " + e.getMessage());
      return null;
    } catch (InterruptedException e) {
      System.err.println("Interrupted Error: " + e.getMessage());
      return null;
    }

  }

    public ApiResponse<List<User>> getUsers() {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "users"))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .GET()
        .build();
  
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
  
      if (response.statusCode() == 200) {
        Type userListType = new TypeToken<ArrayList<User>>() {}.getType();
        List<User> users = gson.fromJson(response.body(), userListType);
        return new ApiResponse<>(true, users, "Usuarios cargados correctamente");
      } else {
        String msg = gson.fromJson(response.body(), JsonObject.class).get("msg").getAsString();
        return new ApiResponse<>(false, null, msg);
      }
    } catch (JsonSyntaxException | IOException | InterruptedException e) {
      return new ApiResponse<>(false, null, "Error al obtener usuarios: " + e.getMessage());
    }
  }
}
