package com.vibevault.repository;

import com.vibevault.model.Playlist;
import com.vibevault.model.Song;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class FilePlaylistRepository implements PlaylistRepository {
    private static final String FILE_PATH = "data/playlist.csv";

    @Override
    public void save(Playlist playlist) {
        List<String> csvLines = new ArrayList<>();
        for (Song song : playlist.getSongs()) {
            csvLines.add(String.format("%s,%s,%d", song.title(), song.artist(), song.durationInSeconds()));
        }

        Path path = Paths.get(FILE_PATH);
        try {
            Path parentDir = path.getParent();
            if (parentDir != null && Files.notExists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            Files.write(path, csvLines);
        } catch (IOException e) {
            System.err.println("❌ 错误：无法保存播放列表。");
            e.printStackTrace();
        }
    }

    @Override
    public Playlist load(String playlistId) {
        // 为了简化，我们暂时让文件名与playlistId无关
        // 在更复杂的系统中，这里可能会是 "data/" + playlistId + ".csv"
        final String FILE_PATH = "data/playlist.csv";
        Playlist playlist = new Playlist(playlistId);
        Path path = Paths.get(FILE_PATH);

        if (Files.notExists(path)) {
            return playlist; // 文件不存在，返回新的空列表
        }

        try {
            List<String> csvLines = Files.readAllLines(path);
            for (String line : csvLines) {
                String[] fields = line.split(",");
                if (fields.length == 3) {
                    playlist.addSong(new Song(fields[0], fields[1], Integer.parseInt(fields[2])));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("❌ 错误：无法加载播放列表。");
            e.printStackTrace();
        }
        return playlist;
    }
}
