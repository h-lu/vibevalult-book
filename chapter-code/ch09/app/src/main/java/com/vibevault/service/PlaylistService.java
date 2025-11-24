package com.vibevault.service;

import com.vibevault.dto.PlaylistDTO;
import com.vibevault.dto.SongDTO;
import java.util.List;
import com.vibevault.model.Playlist;

public interface PlaylistService {
    PlaylistDTO getPlaylistById(long id);

    void addSongToPlaylist(long playlistId, SongDTO songDTO);

    List<PlaylistDTO> getAllPlaylists();

    Playlist createPlaylist(String name, String username);

    void deletePlaylist(long id);
}
