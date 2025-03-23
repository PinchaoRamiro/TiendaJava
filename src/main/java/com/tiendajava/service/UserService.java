package com.tiendajava.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;

import com.tiendajava.model.User;
import com.tiendajava.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;
    private final  String urlBasic = "http://localhost:5000/api/";
    private final HttpClient client = HttpClient.newHttpClient();

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public boolean login(String email, String password) {
        String json = "{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlBasic + "auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (java.net.http.HttpTimeoutException e) {
            System.err.println("Timeout error: " + e.getMessage());
            return false;
        } catch (java.io.IOException e) {
            System.err.println("IO error: " + e.getMessage());
            return false;
        } catch (InterruptedException e) {
            System.err.println("Interrupted error: " + e.getMessage());
            return false;
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public boolean setStatusUser(int id, boolean status) {
        return userRepository.setStatusUser(id, status);
    }
}
