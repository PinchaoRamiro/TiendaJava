package com.tiendajava.service;

import com.google.gson.Gson;
import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.User;
import com.tiendajava.repository.UserRepository;

public class UserService {
	private final UserRepository userRepository;
	private final Gson gson = new Gson();

	public UserService() {
		this.userRepository = new UserRepository();
	}

	public ApiResponse<User>  Register(User user) {
		String json = gson.toJson(user);
		return userRepository.createUser(json);
	}

	public ApiResponse<User> login(String email, String password) {
		String json = "{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }";
		return userRepository.login(json);
	}

	public void Logout() {
		userRepository.logout();
	}

	public ApiResponse<User>  findUserByEmail(String email) {
		return userRepository.getUserByEmail(email);
	}

	public ApiResponse<User>  findUserById(int id) {
		return userRepository.getUserById(id);
	}

	public ApiResponse<User>  UpdateUser(User user) {
		String json = gson.toJson(user);
		return userRepository.updateUser(json, user.getId());
	}

	public ApiResponse<User> changePassword(User user, String currentPassword,  String newPassword) {
		String json = "{ \"currentPassword\": \"" + currentPassword + "\", \"newPassword\": \"" + newPassword + "\" }";
		return userRepository.updatePassword(json, user.getId());
	}

	public ApiResponse<Boolean>  setStatusUser(User user, boolean status) {
		String json = gson.toJson(user);
		return userRepository.setStatusUser(json, user.getId());
	}
}
