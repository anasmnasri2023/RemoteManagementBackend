package com.anas.back.service;

import com.anas.back.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User getUserByEmail(String email);
    User createUser(User user);
    User updateUser(Long id, User userModifie);
    void deleteUser(Long id);
}