package com.vibevault.service;

import com.vibevault.dto.PlaylistDTO;
import com.vibevault.dto.SongDTO;

public interface PlaylistService {
    PlaylistDTO getPlaylistById(String id);
    void addSongToPlaylist(String playlistId, SongDTO songDTO);
}
