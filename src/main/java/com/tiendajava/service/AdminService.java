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

    public ApiResponse<User> updateUserRole(int userId, String newRole) {
        String json = gson.toJson(new RolePayload(newRole));
        return adminRepository.updateUserRole(json, userId);
    }

    public ApiResponse<String> deleteUser(int userId) {
        return adminRepository.deleteUser(userId);
    }

    // Clase interna para representar la carga útil del cambio de rol
    private static class RolePayload {
        @SuppressWarnings("unused")
        private final String role;

        public RolePayload(String role) {
            this.role = role;
        }
    }
}
