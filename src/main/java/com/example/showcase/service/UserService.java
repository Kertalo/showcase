package com.example.showcase.service;

import com.example.showcase.dto.UserDTO;
import com.example.showcase.entity.User;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User createUser(UserDTO userDTO);

    User getUserById(int userId);

    List<User> getAllUsers();

    User updateUser(int userId, UserDTO updateUserDTO);

    void deleteUser(int userId);

    Iterable<User> save(List<User> users);

    Iterable<User> saveUsersFromDTO(List<UserDTO> userDTOs);

    byte[] downloadImageFromFileSystem(Integer userId) throws IOException;

    User getUserByLogin(String email);
}
