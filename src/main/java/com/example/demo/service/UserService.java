package com.example.demo.service;

import com.example.demo.dtos.request.RegisterRequest;
import com.example.demo.dtos.request.users.UpdateUserRequest;
import com.example.demo.models.User;
import jakarta.transaction.Transactional;

import java.security.InvalidKeyException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    @Transactional
    Optional<User> findUserById(Long id);

    Optional<User> findByEmail(String email);

    void save(User user);

    User createUser(RegisterRequest registerRequest);

    String login(String email, String password) throws Exception;

    // send Mail
    void sendMail(String to, String subject, String text);

    String generateToken(User user) throws InvalidKeyException;

    User resetPassword(String email, String newPassword);

    User updateUser(User user, UpdateUserRequest updateUserRequest);

    boolean existsByEmail(String email);

    Optional<User> findByName(String name);
}
