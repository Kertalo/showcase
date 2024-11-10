package com.example.showcase.service.impl;

import com.example.showcase.dto.UserDTO;
import com.example.showcase.entity.Role;
import com.example.showcase.exception.ResourceNotFoundException;
import com.example.showcase.entity.User;
import com.example.showcase.repository.RoleRepository;
import com.example.showcase.repository.UserRepository;
import com.example.showcase.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Override
    public User createUser(UserDTO userDTO) {
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        User user = new User();
        user.setRole(role);
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
    public User updateUser(int userId, UserDTO updateUserDTO) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Role role = roleRepository.findById(updateUserDTO.getRoleId())
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        user.setFullName(updateUserDTO.getFullName());
        user.setLogin(updateUserDTO.getLogin());
        user.setRole(role);
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

    @Override
    public Iterable<User> saveUsersFromDTO(List<UserDTO> userDTOs) {
        List<User> users = new ArrayList<>();

        for (UserDTO userDTO : userDTOs) {
            User user = new User();

            Role role = roleRepository.findById(userDTO.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

            user.setRole(role);
            user.setFullName(userDTO.getFullName());
            user.setLogin(userDTO.getLogin());
            user.setId(userDTO.getId());

            users.add(user);
        }
        return userRepository.saveAll(users);
    }

}
