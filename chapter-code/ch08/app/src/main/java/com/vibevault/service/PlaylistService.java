package com.vibevault.service;

import com.vibevault.dto.PlaylistDTO;
import com.vibevault.dto.SongDTO;
import java.util.List;

public interface PlaylistService {
    PlaylistDTO getPlaylistById(long id);
    void addSongToPlaylist(long playlistId, SongDTO songDTO);
    List<PlaylistDTO> getAllPlaylists();
}
