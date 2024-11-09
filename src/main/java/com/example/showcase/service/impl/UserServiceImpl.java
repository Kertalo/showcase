package com.example.showcase.service.impl;

import com.example.showcase.exception.ResourceNotFoundException;
import com.example.showcase.entity.User;
import com.example.showcase.repository.UserRepository;
import com.example.showcase.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(int userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(int userId, User updateUser) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setFullName(updateUser.getFullName());
        user.setRole(updateUser.getRole());
        user.setLogin(updateUser.getLogin());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public Iterable<User> save(List<User> users) {
        return userRepository.saveAll(users);
    }
}
