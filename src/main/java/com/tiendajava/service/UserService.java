package com.tiendajava.service;

import com.tiendajava.model.ApiResponse;
import com.tiendajava.model.User;
import com.tiendajava.repository.UserRepository;

public class UserService {
	private final UserRepository userRepository;

	public UserService() {
		this.userRepository = new UserRepository();
	}

	public ApiResponse<User>  Register(User user) {
		String json = "{ \"name\": \"" + user.getName() + "\", \"lastname\": \"" + user.getLastName() + "\", \"email\": \""
				+ user.getEmail() + "\", \"password\": \"" + user.getPassword() + "\", \"typeDocument\": \""
				+ user.getTypeDocument() + "\", \"numDocument\": \"" + user.getNumDocument() + "\", \"adress\": \""
				+ user.getAddress() + "\", \"phone\": \"" + user.getPhone() + "\" }";
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

	public ApiResponse<User>  UpdateUser(User user) {
		String json = "{ \"name\": \"" + user.getName() + "\", \"lastname\": \"" + user.getLastName() + "\", \"email\": \""
				+ user.getEmail() + "\", \"password\": \"" + user.getPassword() + "\", \"typeDocument\": \""
				+ user.getTypeDocument() + "\", \"numDocument\": \"" + user.getNumDocument() + "\", \"adress\": \""
				+ user.getAddress() + "\", \"phone\": \"" + user.getPhone() + "\" }";
		return userRepository.updateUser(json, user.getId());
	}

	public ApiResponse<Boolean>  setStatusUser(User user, boolean status) {
		String json = "{ \"status\": " + status + " }";
		return userRepository.setStatusUser(json, user.getId());
	}
}
