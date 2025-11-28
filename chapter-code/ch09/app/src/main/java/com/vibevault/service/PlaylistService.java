package com.vibevault.service;

import com.vibevault.dto.PlaylistCreateDTO;
import com.vibevault.dto.PlaylistDTO;
import com.vibevault.dto.SongCreateDTO;
import java.util.List;

public interface PlaylistService {
    List<PlaylistDTO> getAllPlaylists();
    PlaylistDTO getPlaylistById(long id);
    PlaylistDTO createPlaylist(PlaylistCreateDTO playlistCreateDTO);
    void addSongToPlaylist(long playlistId, SongCreateDTO songCreateDTO);
    void removeSongFromPlaylist(long playlistId, long songId);
    void deletePlaylist(long playlistId);
}
