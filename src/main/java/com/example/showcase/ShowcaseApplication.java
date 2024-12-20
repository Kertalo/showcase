package com.example.showcase;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.dto.UserDTO;
import com.example.showcase.entity.Role;
import com.example.showcase.entity.Tag;
import com.example.showcase.entity.Track;
import com.example.showcase.service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootApplication
public class ShowcaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShowcaseApplication.class, args);
	}

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

	@Bean
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
	}
}
