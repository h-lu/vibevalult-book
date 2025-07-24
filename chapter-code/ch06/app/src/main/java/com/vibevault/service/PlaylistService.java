package com.vibevault.service;

import com.vibevault.model.Song;

public interface PlaylistService {
    void addSong(Song song);
    void removeSong(int songIndex);
    String listSongs();
    void saveData();
}
