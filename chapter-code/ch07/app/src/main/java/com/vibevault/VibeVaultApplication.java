package com.vibevault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.CommandLineRunner;
import com.vibevault.repository.PlaylistRepository;
import com.vibevault.model.Playlist;
import com.vibevault.model.Song;

@SpringBootApplication
public class VibeVaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(VibeVaultApplication.class, args);
    }

    @Bean // <--- 将这个CommandLineRunner注册为一个Bean
    @Profile("!test") // <--- 这个Bean只在非"test"环境下生效，避免影响自动化测试
    public CommandLineRunner initData(PlaylistRepository repository) {
        return args -> {
            final String defaultPlaylistId = "my-favorites";
            // 检查默认播放列表是否已存在，避免重复创建
            if (repository.load(defaultPlaylistId).isEmpty()) {
                System.out.println("ℹ️ 默认播放列表不存在，正在创建示例数据...");
                Playlist playlist = new Playlist(defaultPlaylistId);
                playlist.addSong(new Song("Bohemian Rhapsody", "Queen", 355));
                playlist.addSong(new Song("Stairway to Heaven", "Led Zeppelin", 482));
                repository.save(playlist);
                System.out.println("✅ 示例数据创建完毕！");
            } else {
                System.out.println("ℹ️ 默认播放列表已存在，无需创建示例数据。");
            }
        };
    }
}