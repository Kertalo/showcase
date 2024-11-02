package com.example.showcase;

import com.example.showcase.entity.Project;
import com.example.showcase.entity.Tag;
import com.example.showcase.entity.Track;
import com.example.showcase.entity.User;
import com.example.showcase.service.ProjectService;
import com.example.showcase.service.TagService;
import com.example.showcase.service.TrackService;
import com.example.showcase.service.UserService;
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

			TypeReference<List<User>> userTypeReference = new TypeReference<List<User>>() {};
			InputStream userInputStream = TypeReference.class.getResourceAsStream("/json/users.json");
			try {
				List<User> users = mapper.readValue(userInputStream, userTypeReference);
				userService.save(users);
				System.out.println("Users Saved!");
			} catch (IOException e) {
				System.out.println("Unable to save Users: " + e.getMessage());
			}

			TypeReference<List<Project>> projectTypeReference = new TypeReference<List<Project>>() {};
			InputStream projectInputStream = TypeReference.class.getResourceAsStream("/json/projects.json");
			try {
				List<Project> projects = mapper.readValue(projectInputStream, projectTypeReference);
				projectService.save(projects);
				System.out.println("Projects Saved!");
			} catch (IOException e) {
				System.out.println("Unable to save Projects: " + e.getMessage());
			}
		};
	}
}
