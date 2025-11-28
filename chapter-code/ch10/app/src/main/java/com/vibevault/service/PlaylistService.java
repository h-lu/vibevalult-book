package com.vibevault.service;

import com.vibevault.dto.PlaylistDTO;
import com.vibevault.dto.SongCreateDTO;
import java.util.List;

public interface PlaylistService {
    List<PlaylistDTO> getAllPlaylists();

    PlaylistDTO getPlaylistById(Long id);

    PlaylistDTO createPlaylist(String name, String ownerUsername);

    void addSongToPlaylist(Long playlistId, SongCreateDTO songCreateDTO, String currentUsername);

    void removeSongFromPlaylist(Long playlistId, Long songId, String currentUsername);

    void deletePlaylist(Long playlistId, String currentUsername);
}
