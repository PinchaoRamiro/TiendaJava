package com.tiendajava.service;

import java.util.List;

import com.tiendajava.model.User;
import com.tiendajava.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepository();
    }

    public User Register(User user){
        String json = "{ \"name\": \"" + user.getName() + "\", \"lastname\": \"" + user.getLastName() + "\", \"email\": \"" + user.getEmail() + "\", \"password\": \"" + user.getPassword() + "\", \"typeDocument\": \"" + user.getTypeDocument() + "\", \"numDocument\": \"" + user.getNumDocument() + "\", \"adress\": \"" + user.getAddress() + "\", \"phone\": \"" + user.getPhone() + "\" }";
        return userRepository.createUser(json);
    }

    public boolean login(String email, String password) {
        String json = "{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }";
        return userRepository.login(json);
    }

    public void Logout() {
        userRepository.logout();
    }

    public User findUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public User UpdateUser(User user){
        String json = "{ \"name\": \"" + user.getName() + "\", \"lastname\": \"" + user.getLastName() + "\", \"email\": \"" + user.getEmail() + "\", \"password\": \"" + user.getPassword() + "\", \"typeDocument\": \"" + user.getTypeDocument() + "\", \"numDocument\": \"" + user.getNumDocument() + "\", \"adress\": \"" + user.getAddress() + "\", \"phone\": \"" + user.getPhone() + "\" }";
        return userRepository.updateUser(json, user.getId());
    }

    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    public boolean setStatusUser(User user, boolean status) {
        String json = "{ \"status\": " + status + " }";
        return userRepository.setStatusUser(json, user.getId());
    }
}
