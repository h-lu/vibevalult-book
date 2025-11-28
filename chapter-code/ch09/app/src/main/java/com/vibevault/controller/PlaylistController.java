package com.vibevault.controller;

import com.vibevault.dto.PlaylistCreateDTO;
import com.vibevault.dto.PlaylistDTO;
import com.vibevault.dto.SongCreateDTO;
import com.vibevault.service.PlaylistService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "http://localhost:5173")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public List<PlaylistDTO> getAllPlaylists() {
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/{id}")
    public PlaylistDTO getPlaylist(@PathVariable long id) {
        return playlistService.getPlaylistById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistDTO createPlaylist(@RequestBody PlaylistCreateDTO createDTO) {
        return playlistService.createPlaylist(createDTO);
    }

    @PostMapping("/{id}/songs")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSongToPlaylist(@PathVariable long id, @RequestBody SongCreateDTO songCreateDTO) {
        playlistService.addSongToPlaylist(id, songCreateDTO);
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSong(@PathVariable long playlistId, @PathVariable long songId) {
        playlistService.removeSongFromPlaylist(playlistId, songId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlaylist(@PathVariable long id) {
        playlistService.deletePlaylist(id);
    }
}