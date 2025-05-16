package com.tiendajava.service;

import java.util.List;

import com.google.gson.Gson;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.User;
import com.tiendajava.repository.AdminRepository;

public class AdminService {

    private final AdminRepository adminRepository = new AdminRepository();
    private final Gson gson = new Gson();

    public ApiResponse<User> registerAdmin(User admin) {
        String json = gson.toJson(admin);
        return adminRepository.registerAdmin(json);
    }

    public ApiResponse<String> logoutAdmin() {
        return adminRepository.logoutAdmin();
    }

    public ApiResponse<List<User>> getAllUsers() {
        return adminRepository.getAllUsers();
    }

    public ApiResponse<List<User>> getAllAdmins() {
        return adminRepository.getAllAdmins();
    }

    public ApiResponse<String> deleteUser(int userId) {
        return adminRepository.deleteUser(userId);
    }

    public ApiResponse<List<User>> searchAdmins(String name){
        return adminRepository.searchAdmins(name);
    }
}
