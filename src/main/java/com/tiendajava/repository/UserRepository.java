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
import com.tiendajava.model.User;

public class UserRepository {

  private final Gson gson = new Gson();

  private final String urlBasic = "http://localhost:5000/api/";
  private final HttpClient client = HttpClient.newHttpClient();


  public User createUser(User user) {
    System.out.println("User: " + user.toString());
    String json = "{ \"name\": \"" + user.getName() + "\", \"lastname\": \"" + user.getLastName() + "\", \"email\": \"" + user.getEmail() + "\", \"password\": \"" + user.getPassword() + "\", \"typeDocument\": \"" + user.getTypeDocument() + "\", \"numDocument\": \"" + user.getNumDocument() + "\", \"adress\": \"" + user.getAddress() + "\", \"phone\": \"" + user.getPhone() + "\" }";
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(urlBasic +  "auth/register"))
        .header("Content-Type", "application/json")
        .POST(BodyPublishers.ofString(json))
        .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if(response.statusCode() == 201) {        
        User userResponse = gson.fromJson(response.body(), User.class);
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

  public String updateUser(User user) {
    String json = "{ \"name\": \"" + user.getName() + "\", \"lastname\": \"" + user.getLastName() + "\", \"email\": \"" + user.getEmail() + "\", \"password\": \"" + user.getPassword() + "\", \"typeDocument\": \"" + user.getTypeDocument() + "\", \"numDocument\": \"" + user.getNumDocument() + "\", \"adress\": \"" + user.getAddress() + "\", \"phone\": \"" + user.getPhone() + "\" }";
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(urlBasic + "user/update/:" + user.getId()))
        .header("Content-Type", "application/json")
        .PUT(BodyPublishers.ofString(json))
        .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if(response.body().contains("\"error\"")) {
        return "Error: " + response.body();
      }
      else{
        User userResponse = gson.fromJson(response.body(), User.class);
        return userResponse.toString();
      }
      
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      return "Error: " + e.getMessage();
    }
  }

  public User getUserByEmail(String email) {
    HttpRequest request = HttpRequest.newBuilder()
      .uri(URI.create(urlBasic + "user/" + email))
      .header("Content-Type", "application/json")
      .GET()
      .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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

  public boolean setStatusUser(int id, boolean status) {
    String json = "{ \"status\": " + status + " }";
    try {
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(urlBasic + "user/status/:" + id))
        .header("Content-Type", "application/json")
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
