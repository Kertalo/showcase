package com.example.showcase.service;

import com.example.showcase.entity.Track;

import java.util.List;

public interface TrackService {
    Track createTrack(Track track);

    Track getTrackById(int trackId);

    List<Track> getAllTracks();

    Track updateTrack(int trackId, Track updateTrack);

    void deleteTrack(int trackId);

    Iterable<Track> save(List<Track> tracks);

    boolean existsByName(String trackName);

    Track getTrackByName(String trackName);
}
