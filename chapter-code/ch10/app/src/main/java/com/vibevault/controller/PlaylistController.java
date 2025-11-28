package com.vibevault.controller;

import com.vibevault.dto.PlaylistDTO;
import com.vibevault.dto.SongCreateDTO;
import com.vibevault.service.PlaylistService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    public PlaylistDTO getPlaylist(@PathVariable Long id) {
        return playlistService.getPlaylistById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlaylistDTO createPlaylist(@RequestBody CreatePlaylistRequest request,
                                      Authentication authentication) {
        String currentUsername = authentication.getName();
        return playlistService.createPlaylist(request.name(), currentUsername);
    }

    @PostMapping("/{id}/songs")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@securityUtils.isPlaylistOwner(#id, authentication.name)")
    public void addSongToPlaylist(@PathVariable Long id,
                                  @RequestBody SongCreateDTO songCreateDTO,
                                  Authentication authentication) {
        playlistService.addSongToPlaylist(id, songCreateDTO, authentication.getName());
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@securityUtils.isPlaylistOwner(#playlistId, authentication.name)")
    public void removeSong(@PathVariable Long playlistId,
                           @PathVariable Long songId,
                           Authentication authentication) {
        playlistService.removeSongFromPlaylist(playlistId, songId, authentication.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@securityUtils.isPlaylistOwner(#id, authentication.name)")
    public void deletePlaylist(@PathVariable Long id, Authentication authentication) {
        playlistService.deletePlaylist(id, authentication.getName());
    }

    public record CreatePlaylistRequest(String name) {
    }
}