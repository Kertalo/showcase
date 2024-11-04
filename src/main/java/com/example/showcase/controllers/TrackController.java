package com.example.showcase.controllers;

import com.example.showcase.entity.Track;
import com.example.showcase.service.TrackService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/tracks")
public class TrackController {
    private TrackService trackService;

    @PostMapping
    public ResponseEntity<Track> createTrack(@RequestBody Track track) {
        Track savedTrack = trackService.createTrack(track);
        return new ResponseEntity<>(savedTrack, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Track> getTrackById(@PathVariable("id") int trackId) {
        Track track = trackService.getTrackById(trackId);
        return ResponseEntity.ok(track);
    }

    @GetMapping
    public ResponseEntity<List<Track>> getAllTracks() {
        List<Track> tracks = trackService.getAllTracks();
        return ResponseEntity.ok(tracks);
    }

    @PutMapping("{id}")
    public ResponseEntity<Track> updateTrack(@PathVariable("id") int trackId, @RequestBody Track updatetrack) {
        Track track = trackService.updateTrack(trackId, updatetrack);
        return ResponseEntity.ok(track);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTrack(@PathVariable("id") int trackId) {
        trackService.deleteTrack(trackId);
        return ResponseEntity.ok("Track is deleted");
    }
}
