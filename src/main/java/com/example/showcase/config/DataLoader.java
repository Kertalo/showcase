package com.example.showcase.config;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.dto.UserDTO;
import com.example.showcase.entity.Date;
import com.example.showcase.entity.Role;
import com.example.showcase.entity.Tag;
import com.example.showcase.entity.Track;
import com.example.showcase.primary_filling.PrimaryFillingLoader;
import com.example.showcase.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {
    @Autowired
    private TrackService trackService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DateService dateService;

    //Загрузка данных из json-файлов в /json
    /*@Bean
    @Order(1)
    CommandLineRunner runner() {
        return _ -> {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Track>> trackTypeReference = new TypeReference<List<Track>>() {};
            InputStream trackInputStream = TypeReference.class.getResourceAsStream("/json/tracks.json");
            try {
                List<Track> tracks = mapper.readValue(trackInputStream, trackTypeReference);
                trackService.save(tracks);
                System.out.println("Tracks Saved!");
            } catch (IOException e) {
                System.out.println("Unable to save Tracks: " + e.getMessage());
            }

            TypeReference<List<Tag>> tagTypeReference = new TypeReference<List<Tag>>() {};
            InputStream tagInputStream = TypeReference.class.getResourceAsStream("/json/tags.json");
            try {
                List<Tag> tags = mapper.readValue(tagInputStream, tagTypeReference);
                tagService.save(tags);
                System.out.println("Tags Saved!");
            } catch (IOException e) {
                System.out.println("Unable to save Tags: " + e.getMessage());
            }

            TypeReference<List<Role>> roleTypeReference = new TypeReference<List<Role>>() {};
            InputStream roleInputStream = TypeReference.class.getResourceAsStream("/json/roles.json");
            try {
                List<Role> roles = mapper.readValue(roleInputStream, roleTypeReference);
                roleService.save(roles);
                System.out.println("Roles Saved!");
            } catch (IOException e) {
                System.out.println("Unable to save Roles: " + e.getMessage());
            }

            TypeReference<List<UserDTO>> userTypeReference = new TypeReference<List<UserDTO>>() {};
            InputStream userInputStream = TypeReference.class.getResourceAsStream("/json/users.json");
            try {
                List<UserDTO> users = mapper.readValue(userInputStream, userTypeReference);
                userService.saveUsersFromDTO(users);
                System.out.println("Users Saved!");
            } catch (IOException e) {
                System.out.println("Unable to save Users: " + e.getMessage());
            }

            TypeReference<List<Date>> dateTypeReference = new TypeReference<List<Date>>() {};
            InputStream dateInputStream = TypeReference.class.getResourceAsStream("/json/dates.json");
            try {
                List<Date> dates = mapper.readValue(dateInputStream, dateTypeReference);
                dateService.save(dates);
                System.out.println("Dates Saved!");
            } catch (IOException e) {
                System.out.println("Unable to save Dates: " + e.getMessage());
            }

            TypeReference<List<ProjectDTO>> projectTypeReference = new TypeReference<List<ProjectDTO>>() {};
            InputStream projectInputStream = TypeReference.class.getResourceAsStream("/json/projects.json");
            try {
                List<ProjectDTO> projects = mapper.readValue(projectInputStream, projectTypeReference);
                projectService.saveProjectsFromDTO(projects);
                System.out.println("Projects Saved!");
            } catch (IOException e) {
                System.out.println("Unable to save Projects: " + e.getMessage());
            }

        };
    }*/

    //Загрузка данных из csv-таблиц и json-файлов в primary_filling
    @Bean
    @Order(1)
    CommandLineRunner runner() {
        return _ -> {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<Track>> trackTypeReference = new TypeReference<List<Track>>() {
            };
            InputStream trackInputStream = TypeReference.class.getResourceAsStream("/primary_filling/tracks.json");
            try {
                List<Track> tracks = mapper.readValue(trackInputStream, trackTypeReference);
                trackService.save(tracks);
                System.out.println("Tracks Saved!");
            } catch (IOException e) {
                System.out.println("Unable to save Tracks: " + e.getMessage());
            }

            TypeReference<List<Tag>> tagTypeReference = new TypeReference<List<Tag>>() {
            };
            InputStream tagInputStream = TypeReference.class.getResourceAsStream("/primary_filling/tags.json");
            try {
                List<Tag> tags = mapper.readValue(tagInputStream, tagTypeReference);
                tagService.save(tags);
                System.out.println("Tags Saved!");
            } catch (IOException e) {
                System.out.println("Unable to save Tags: " + e.getMessage());
            }

            TypeReference<List<Role>> roleTypeReference = new TypeReference<List<Role>>() {
            };
            InputStream roleInputStream = TypeReference.class.getResourceAsStream("/primary_filling/roles.json");
            try {
                List<Role> roles = mapper.readValue(roleInputStream, roleTypeReference);
                roleService.save(roles);
                System.out.println("Roles Saved!");
            } catch (IOException e) {
                System.out.println("Unable to save Roles: " + e.getMessage());
            }

            TypeReference<List<Date>> dateTypeReference = new TypeReference<List<Date>>() {
            };
            InputStream dateInputStream = TypeReference.class.getResourceAsStream("/primary_filling/dates.json");
            try {
                List<Date> dates = mapper.readValue(dateInputStream, dateTypeReference);
                dateService.save(dates);
                System.out.println("Dates Saved!");
            } catch (IOException e) {
                System.out.println("Unable to save Dates: " + e.getMessage());
            }

            List<UserDTO> users = new ArrayList<>();
            List<ProjectDTO> projects = new ArrayList<>();
            PrimaryFillingLoader.scrapTables(users, projects);

            if (!users.isEmpty()) {
                userService.saveUsersFromDTO(users);
                System.out.println("Users Saved!");
            } else {
                System.out.println("Unable to save Users");
            }

            if (!projects.isEmpty()) {
                projectService.saveProjectsFromDTO(projects);
                System.out.println("Projects Saved!");
            } else {
                System.out.println("Unable to save Projects");
            }

            System.out.println("Primary Filling completed");
        };
    }
}
