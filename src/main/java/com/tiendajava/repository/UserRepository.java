package com.tiendajava.repository;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tiendajava.model.LoginResponse;
import com.tiendajava.model.Session;
import com.tiendajava.model.User;

public class UserRepository {

  private final Gson gson = new Gson();

  private final String urlBasic = "http://localhost:5000/api/";
  private final HttpClient client = HttpClient.newHttpClient();


  public User createUser(String json) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(urlBasic +  "auth/register"))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(json))
        .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if(response.statusCode() == 201) { 
        LoginResponse loginResponse = gson.fromJson(response.body(), LoginResponse.class);
        User userResponse = loginResponse.getUser();

        Session.getInstance().setToken(loginResponse.getToken());
        Session.getInstance().setUserEmail(loginResponse.getUser().getEmail());
        Session.getInstance().setRole(loginResponse.getUser().getRole());
        return userResponse;
      }

      return null;
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

  public boolean  login(String json) {
      try {
          HttpRequest request = HttpRequest.newBuilder()
                  .uri(URI.create(urlBasic + "auth/login"))
                  .header("Content-Type", "application/json")
                  .POST(BodyPublishers.ofString(json))
                  .build();

          HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

          System.out.println(response);

          if (response.statusCode() == 200) {
              LoginResponse loginResponse = gson.fromJson(response.body(), LoginResponse.class);

              Session.getInstance().setToken(loginResponse.getToken());
              Session.getInstance().setUserEmail(loginResponse.getUser().getEmail());
              Session.getInstance().setRole(loginResponse.getUser().getRole());

              return true;
          } else {
              return false;
          }
      } catch (Exception e) {
          System.err.println("Login error: " + e.getMessage());
          return false;
      }
  }

  public User updateUser(String json, int id) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(urlBasic + "user/update/" + id))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .PUT(BodyPublishers.ofString(json))
        .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if(response.body().contains("\"error\"")) {
        System.err.println(response.body());
        return null;
      }
      else{
        User userResponse = gson.fromJson(response.body(), User.class);
        return userResponse;
      }
      
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      return null;
    }
  }

  public User getUserByEmail(String email) {
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(urlBasic + "user/by-email/" + email))
      .header("Content-Type", "application/json")
      .header("Authorization", "Bearer " + Session.getInstance().getToken()) // ✅ token aquí
      .GET()
      .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      System.out.println(response);
      if(response.statusCode() == 200) {
        User userResponse = gson.fromJson(response.body(), User.class);
        return userResponse;
      } else {
        return null;
      }

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      return null;
    }
  }

  public List<User> getUsers() {
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(urlBasic + "users"))
      .header("Content-Type", "application/json")
      .header("Authorization", "Bearer " + Session.getInstance().getToken())
      .GET()
      .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if(response.statusCode() == 200) {
        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
        List<User> users = gson.fromJson(response.body(), userListType);
        return users;
      }else{
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }

  public boolean setStatusUser(String json, int id) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(urlBasic + "user/status/:" + id))
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + Session.getInstance().getToken())
        .PUT(BodyPublishers.ofString(json))
        .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      return response.body().contains("\"msg\"");
      
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      return false;
    }
  }
}
