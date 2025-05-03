package com.example.showcase.service.impl;

import com.example.showcase.exception.ResourceNotFoundException;
import com.example.showcase.entity.Track;
import com.example.showcase.repository.TrackRepository;
import com.example.showcase.service.TrackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TrackServiceImpl implements TrackService {
    private TrackRepository trackRepository;
    
    @Override
    public Track createTrack(Track track) {
        return trackRepository.save(track);
    }

    @Override
    public Track getTrackById(int trackId) {
        Track track = trackRepository.findById(trackId)
            .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
        return track;
    }

    @Override
    public List<Track> getAllTracks() {
        return trackRepository.findAll();
    }

    @Override
    public Track updateTrack(int trackId, Track updateTrack) {
        Track track = trackRepository.findById(trackId)
            .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
        track.setName(updateTrack.getName());
        return trackRepository.save(track);
    }

    @Override
    public void deleteTrack(int trackId) {
        Track track = trackRepository.findById(trackId)
            .orElseThrow(() -> new ResourceNotFoundException("Track not found"));
        trackRepository.delete(track);
    }

    @Override
    public Iterable<Track> save(List<Track> tracks) {
        return trackRepository.saveAll(tracks);
    }

    @Override
    public boolean existsByName(String trackName){return trackRepository.existsByName(trackName);}

    @Override
    public Track getTrackByName(String trackName){return trackRepository.getTrackByName(trackName);}

}
