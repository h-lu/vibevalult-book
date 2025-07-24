package com.vibevault.repository;

import com.vibevault.model.Playlist;
import com.vibevault.model.Song;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository // <--- 添加标签
public class FilePlaylistRepository implements PlaylistRepository {
    private static final String DATA_DIRECTORY = "data";
    private static final Path DATA_PATH = Paths.get(DATA_DIRECTORY);

    @Override
    public void save(Playlist playlist) {
        // 让文件名与播放列表的ID动态关联
        Path path = DATA_PATH.resolve(playlist.getName() + ".csv");
        
        // 使用Stream API，代码更简洁
        List<String> csvLines = playlist.getSongs().stream()
            .map(Song::toCsvString)
            .toList();

        try {
            // 确保 "data" 目录存在
            if (Files.notExists(DATA_PATH)) {
                 Files.createDirectories(DATA_PATH);
            }
            Files.write(path, csvLines);
        } catch (IOException e) {
            // 抛出运行时异常，让错误在更高层级被处理
            throw new RuntimeException("Error saving playlist to " + path, e);
        }
    }

    @Override
    public Optional<Playlist> load(String playlistId) { // <--- 返回类型改为Optional<Playlist>
        Path path = DATA_PATH.resolve(playlistId + ".csv");

        if (Files.notExists(path)) {
            return Optional.empty(); // <--- 如果文件不存在，明确返回“空”
        }

        Playlist playlist = new Playlist(playlistId);
        try {
            List<String> csvLines = Files.readAllLines(path);
            for (String line : csvLines) {
                // 复用Song中的静态工厂方法，保持代码干净
                playlist.addSong(Song.fromCsvString(line));
            }
        } catch (IOException | NumberFormatException e) {
             throw new RuntimeException("Error loading playlist from " + path, e);
        }
        return Optional.of(playlist); // <--- 用Optional包装返回结果
    }
}
