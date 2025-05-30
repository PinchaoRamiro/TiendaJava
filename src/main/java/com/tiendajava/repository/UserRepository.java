package com.tiendajava.repository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.LoginResponse;
import com.tiendajava.model.Session;
import com.tiendajava.model.User;

public class UserRepository extends BaseRepository {

  public ApiResponse<User> createUser(String json) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(URL_BASE + "auth/register"))
          .header("Content-Type", "application/json")
          .POST(BodyPublishers.ofString(json))
          .build();
  
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
  
      if (response.statusCode() == 201) {
        LoginResponse loginResponse = gson.fromJson(response.body(), LoginResponse.class);
        User user = loginResponse.getUser();
        String token = loginResponse.getToken();
      

        if(Session.getInstance().isLogged()){
          return new ApiResponse<>(true, user, "User Registred");
        }   
          Session.getInstance().setToken(token);
          Session.getInstance().setUser(user);
          Session.getInstance().setRole(user.getRole());
    
          return new ApiResponse<>(true, user, "User Registred");
      } else {
        String msg = gson.fromJson(response.body(), JsonObject.class).get("msg").getAsString();
        return new ApiResponse<>(false, null, msg);
      }
    } catch (JsonSyntaxException | IOException | InterruptedException e) {
      return new ApiResponse<>(false, null, "Conexion error");
    }
  }
  
  public ApiResponse<User> login(String json) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(URL_BASE + "auth/login"))
          .header("Content-Type", "application/json")
          .POST(BodyPublishers.ofString(json))
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        LoginResponse loginResponse = gson.fromJson(response.body(), LoginResponse.class);
        User user = loginResponse.getUser();
        String token = loginResponse.getToken();

        Session.getInstance().setToken(token);
        Session.getInstance().setUser(user);
        Session.getInstance().setRole(user.getRole());

        return new ApiResponse<>(true, user, loginResponse.getMsg());
      } else {
        String msg = gson.fromJson(response.body(), JsonObject.class).get("msg").getAsString();
        return new ApiResponse<>(false, null, msg);
      }
    } catch (JsonSyntaxException | IOException | InterruptedException e) {
      return new ApiResponse<>(false, null, "Connection error");
    }
  }

  public void logout() {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(URL_BASE + "auth/logout"))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + Session.getInstance().getToken())
          .POST(BodyPublishers.noBody())
          .build();
      System.out.println("Logout");
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      Session.getInstance().clearSession();

      System.out.println(response);

    } catch (IOException | InterruptedException e) {
      System.err.println("Logout error: " + e.getMessage());

    }
  }


  public ApiResponse<User> getUserById(int id) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "user/" + id))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .GET()
        .build();
    try {
      Type responseType = new TypeToken<ApiResponse<User>>() {}.getType();
      ApiResponse<User> response = sendRequest(request, responseType);
      response.setSuccess(true);
      return response;
    } catch (JsonSyntaxException e) {
      return new ApiResponse<>(false, null, "Error to get user");
    }
  }

  public ApiResponse<User> updateUser(String json, int id) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(URL_BASE + "user/update/" + id))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + Session.getInstance().getToken())
          .PUT(BodyPublishers.ofString(json))
          .build();

      Type responseType = new TypeToken<ApiResponse<User>>() {}.getType();

      ApiResponse<User> response =  sendRequest(request, responseType);
      response.setSuccess(true);
      return response;
    } catch (JsonSyntaxException  e) {
      return new ApiResponse<>(false, null, "Error to update user");
    }
  }

  public ApiResponse<User> updatePassword(String json, int id) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(URL_BASE + "user/password/" + id))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + Session.getInstance().getToken())
          .PUT(BodyPublishers.ofString(json))
          .build();
  
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
  
      if (response.statusCode() == 200) {
        User userResponse = gson.fromJson(response.body(), User.class);
        return new ApiResponse<>(true, userResponse, "Successfully updated password");
      } else {
        String msg = gson.fromJson(response.body(), JsonObject.class).get("msg").getAsString();
        return new ApiResponse<>(false, null, msg);
      }
    } catch (JsonSyntaxException | IOException | InterruptedException e) {
      return new ApiResponse<>(false, null, "Error to update password");
    }
  }

  public ApiResponse<User> getUserByEmail(String email) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL_BASE + "user/by-email/" + email))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .GET()
        .build();
  
    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
  
      if (response.statusCode() == 200) {
        User userResponse = gson.fromJson(response.body(), User.class);
        return new ApiResponse<>(true, userResponse, "User found");
      } else {
        String msg = gson.fromJson(response.body(), JsonObject.class).get("msg").getAsString();
        return new ApiResponse<>(false, null, msg);
      }
    } catch (JsonSyntaxException | IOException | InterruptedException e) {
      return new ApiResponse<>(false, null, "Error to get user by email");
    }
  }
  
  public ApiResponse<Boolean> setStatusUser(String json, int id) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(URL_BASE + "user/status/" + id))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + Session.getInstance().getToken())
          .PUT(BodyPublishers.ofString(json))
          .build();
  
      Type responseType = new TypeToken<ApiResponse<User>>() {}.getType();

      ApiResponse<User> response = sendRequest(request, responseType);
      response.setSuccess(true);

  
      if (response.isSuccess()) {
        return new ApiResponse<>(true, true, "User status changed successfully");
      } else {
        return new ApiResponse<>(false, false, response.getMessage());
      }
    } catch (JsonSyntaxException e) {
      return new ApiResponse<>(false, false, "Error al cambiar estado");
    }
  }
  
}
