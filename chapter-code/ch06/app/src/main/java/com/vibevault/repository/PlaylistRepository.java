package com.vibevault.repository;

import com.vibevault.model.Playlist;

public interface PlaylistRepository {
    void save(Playlist playlist);
    Playlist load(String playlistId);
}
