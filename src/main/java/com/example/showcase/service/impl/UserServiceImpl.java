package com.example.showcase.service.impl;

import com.example.showcase.dto.UserDTO;
import com.example.showcase.entity.Role;
import com.example.showcase.entity.User;
import com.example.showcase.exception.ResourceNotFoundException;
import com.example.showcase.repository.RoleRepository;
import com.example.showcase.repository.UserRepository;
import com.example.showcase.service.UserService;
import com.example.showcase.storage_service.FileNameGenerator;
import com.example.showcase.storage_service.Prefix;
import com.example.showcase.storage_service.S3Service;
import io.minio.errors.MinioException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    private S3Service storageService;


    @Override
    public User createUser(UserDTO userDTO) {
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        String imagePath = saveImage(userDTO.getImage(), userDTO.getFullName());

        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setLogin(userDTO.getLogin());
        user.setCourse(userDTO.getCourse());
        user.setGroup(userDTO.getGroup());
        user.setRole(role);
        user.setImagePath(imagePath);
        user.setEmail(userDTO.getEmail());

        return userRepository.save(user);
    }

    private String saveImage(MultipartFile image, String name) {
        String fileName = FileNameGenerator.generateFileName(Prefix.USER, name);

        try {
            storageService.uploadFile(fileName, image);
        } catch (IOException | MinioException e) {
            throw new RuntimeException("Error uploading image " + e.getMessage());
        }

        return fileName;
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
        user.setCourse(updateUserDTO.getCourse());
        user.setGroup(updateUserDTO.getGroup());
        user.setRole(role);
        user.setEmail(updateUserDTO.getEmail());

        if (updateUserDTO.getImage() != null && !updateUserDTO.getImage().isEmpty()) {
            String imagePath = saveImage(updateUserDTO.getImage(), user.getFullName());
            user.setImagePath(imagePath);
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //удаление файла из users.image
        if (user.getImagePath() != null && !user.getImagePath().isEmpty()) {
            String fileName = user.getImagePath();
            if (storageService.fileExists(fileName)) {
                try {
                    storageService.deleteFile(fileName);
                } catch (MinioException e) {
                    throw new RuntimeException("Failed to delete user image" + e.getMessage());
                }
            }
        }
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
            user.setEmail(userDTO.getEmail());
            user.setGroup(userDTO.getGroup());
            user.setCourse(userDTO.getCourse());

            if (userDTO.getImage() != null && !userDTO.getImage().isEmpty()) {
                String imagePath = saveImage(userDTO.getImage(), user.getFullName());
                user.setImagePath(imagePath);
            }

            users.add(user);
        }
        return userRepository.saveAll(users);
    }

    @Override
    public byte[] downloadImageFromFileSystem(Integer userId) throws IOException {
        Optional<User> user = userRepository.findById(userId);
        String fileName = user.get().getImagePath();
        byte[] images = null;
        try {
            images = storageService.getFile(fileName);
        } catch (MinioException e) {
            throw new RuntimeException(e);
        }
        return images;
    }

    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean exitsByFullNameAndCourse(UserDTO userDTO) {
        return userRepository.existsUserByFullNameAndCourse(userDTO.getFullName(), userDTO.getCourse());
    }

    @Override
    public User getUserByFullNameAndCourse(String fullName, String course) {
        return userRepository.getUserByFullNameAndCourse(fullName, course);
    }

}
