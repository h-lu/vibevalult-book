package com.vibevault.service;

import com.vibevault.model.Playlist;
import com.vibevault.model.Song;
import com.vibevault.repository.PlaylistRepository;

public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistRepository repository;
    private Playlist playlist;
    private static final String DEFAULT_PLAYLIST_ID = "my-favorites";

    public PlaylistServiceImpl(PlaylistRepository repository) {
        this.repository = repository;
        // 为了保持简单，我们启动时加载一个默认的播放列表
        this.playlist = this.repository.load(DEFAULT_PLAYLIST_ID); 
    }

    @Override
    public void addSong(Song song) {
        playlist.addSong(song);
    }

    @Override
    public void removeSong(int songIndex) {
        // 注意，UI是1-based，业务是0-based
        playlist.removeSong(songIndex - 1);
    }

    @Override
    public String listSongs() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Playlist: ").append(playlist.getName()).append(" ---\n");
        if (playlist.getSongs().isEmpty()) {
            sb.append("This playlist is empty.\n");
        } else {
            for (int i = 0; i < playlist.getSongs().size(); i++) {
                Song currentSong = playlist.getSongs().get(i);
                sb.append(String.format("%d. %s - %s\n", i + 1, currentSong.title(), currentSong.artist()));
            }
        }
        sb.append("---------------------------------");
        return sb.toString();
    }

    @Override
    public void saveData() {
        repository.save(playlist);
    }
}
