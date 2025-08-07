package com.vibevault.service;

import com.vibevault.dto.PlaylistDTO;
import com.vibevault.dto.SongDTO;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import com.vibevault.model.Playlist;

public interface PlaylistService {
    PlaylistDTO getPlaylistById(Long id);
    void addSongToPlaylist(Long playlistId, SongDTO songDTO);
    List<PlaylistDTO> getAllPlaylists();

    Playlist createPlaylist(PlaylistDTO playlistDTO, UserDetails userDetails); // 新增方法
}
