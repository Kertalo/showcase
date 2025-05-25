package com.example.showcase.controllers;

import com.example.showcase.dto.UserDTO;
import com.example.showcase.entity.Project;
import com.example.showcase.entity.User;
import com.example.showcase.service.ProjectService;
import com.example.showcase.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://sfedu-project-showcase.onrender.com"})
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<User> createUser(@ModelAttribute  UserDTO user) {
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int userId, @ModelAttribute UserDTO updateUser) {
        User user = userService.updateUser(userId, updateUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User is deleted");
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable("id") int userId) throws IOException {
        byte[] imageData = userService.downloadImageFromFileSystem(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable("login") String login) {
        User user = userService.getUserByLogin(login);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

//    @GetMapping("/{id}/projects")
//    public ResponseEntity<List<Project>> getProjectsByUserId(@PathVariable("id") int userId) {
//        List<Project> projects = projectService.getProjectsByUserId(userId);
//        return ResponseEntity.ok(projects);
//    }

    @GetMapping("/{fullName}/projects")
    public ResponseEntity<List<Project>> getProjectsByUserFullName(@PathVariable("fullName") String fullName) {
        List<Project> projects = projectService.getProjectsByUserFullName(fullName);
        return ResponseEntity.ok(projects);
    }
}
