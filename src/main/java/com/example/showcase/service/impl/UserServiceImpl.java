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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    private static final String FOLDER_PATH = "C:\\JAVA\\PROJECTS\\showcase\\user_images\\";

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
        File directory = new File(FOLDER_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = FOLDER_PATH + name + ".png";
        try {
            image.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }

        return filePath;
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
        if (user.getImagePath() != null && !user.getImagePath().isEmpty()) {
            File file = new File(user.getImagePath());
            if (file.exists() && file.isFile()) {
                if (!file.delete()) {
                    throw new RuntimeException("Failed to delete file");
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
            user.setId(userDTO.getId()); //TODO test it (if userDTO.id == 0)
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
        String filePath = user.get().getImagePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
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
    public boolean exitsByFullNameAndCourse(UserDTO userDTO){
        return userRepository.existsUserByFullNameAndCourse(userDTO.getFullName(),userDTO.getCourse());
    }

    @Override
    public User getUserByFullNameAndCourse(String fullName, String course){
        return userRepository.getUserByFullNameAndCourse(fullName,course);
    }

}
