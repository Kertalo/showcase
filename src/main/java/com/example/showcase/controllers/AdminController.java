package com.example.showcase.controllers;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.entity.Date;
import com.example.showcase.entity.Project;
import com.example.showcase.entity.Tag;
import com.example.showcase.entity.Track;
import com.example.showcase.service.DateService;
import com.example.showcase.service.ProjectService;
import com.example.showcase.service.TagService;
import com.example.showcase.service.TrackService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://sfedu-project-showcase.onrender.com"})
@RestController
@RequestMapping("/admin")
public class AdminController {
    private TrackService trackService;
    private TagService tagService;
    private ProjectService projectService;
    private DateService dateService;

    @PostMapping("tracks")
    public ResponseEntity<Track> createTrack(@RequestBody Track track) {
        Track savedTrack = trackService.createTrack(track);
        return new ResponseEntity<>(savedTrack, HttpStatus.CREATED);
    }

    @GetMapping("tracks/{id}")
    public ResponseEntity<Track> getTrackById(@PathVariable("id") int trackId) {
        Track track = trackService.getTrackById(trackId);
        return ResponseEntity.ok(track);
    }

    @GetMapping("tracks")
    public ResponseEntity<List<Track>> getAllTracks() {
        List<Track> tracks = trackService.getAllTracks();
        return ResponseEntity.ok(tracks);
    }

    @PostMapping("tags")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag savedTag = tagService.createTag(tag);
        return new ResponseEntity<>(savedTag, HttpStatus.CREATED);
    }

    @GetMapping("tags/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable("id") int tagId) {
        Tag tag = tagService.getTagById(tagId);
        return ResponseEntity.ok(tag);
    }

    @GetMapping("tags")
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @PostMapping("projects")
    public ResponseEntity<Project> createProject(@ModelAttribute ProjectDTO project) {
        Project savedProject = projectService.createProject(project);
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping("projects/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable("id") int projectId) {
        Project project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    @GetMapping("projects")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @PostMapping("dates")
    public ResponseEntity<Date> createDate(@RequestBody Date date) {
        Date saveDate = dateService.createDate(date);
        return new ResponseEntity<>(saveDate, HttpStatus.CREATED);
    }

    @GetMapping("dates/{id}")
    public ResponseEntity<Date> getDate(@PathVariable("id")  int dateId) {
        Date date = dateService.getDateById(dateId);
        return ResponseEntity.ok(date);
    }

    @GetMapping("dates")
    public ResponseEntity<List<Date>> getAllDates() {
        List<Date> dates = dateService.getAllDates();
        return ResponseEntity.ok(dates);
    }


}
