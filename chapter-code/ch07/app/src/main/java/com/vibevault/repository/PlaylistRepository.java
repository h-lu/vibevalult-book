package com.vibevault.repository;

import com.vibevault.model.Playlist;
import java.util.Optional;

public interface PlaylistRepository {
    void save(Playlist playlist);
    Optional<Playlist> load(String playlistId);
}
