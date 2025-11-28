package com.vibevault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.CommandLineRunner;
import com.vibevault.repository.PlaylistRepository;
import com.vibevault.model.Playlist;
import com.vibevault.model.Song;
import java.util.List;

@SpringBootApplication
public class VibeVaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(VibeVaultApplication.class, args);
    }

    @Bean // <--- 将这个CommandLineRunner注册为一个Bean
    @Profile("!test") // <--- 这个Bean只在非"test"环境下生效，避免影响自动化测试
    public CommandLineRunner initData(PlaylistRepository repository) {
        return args -> {
            // 先清空所有数据，防止冲突
            repository.deleteAll();
            System.out.println("🧹 已清空所有旧数据");

            List<SamplePlaylist> samplePlaylists = List.of(
                    new SamplePlaylist(
                            "my-favorites",
                            List.of(
                                    new Song("Bohemian Rhapsody", "Queen", 355),
                                    new Song("Stairway to Heaven", "Led Zeppelin", 482))),
                    new SamplePlaylist(
                            "sunny-drive",
                            List.of(
                                    new Song("Drive", "The Cars", 221),
                                    new Song("Send Me On My Way", "Rusted Root", 215))));

            for (SamplePlaylist sample : samplePlaylists) {
                if (repository.findByName(sample.name()).isEmpty()) {
                    System.out.printf("ℹ️ 播放列表「%s」不存在，正在创建示例数据...%n", sample.name());
                    Playlist playlist = new Playlist(sample.name());
                    sample.songs().forEach(playlist::addSong);
                    repository.save(playlist);
                    System.out.printf("✅ 播放列表「%s」创建完毕！%n", sample.name());
                } else {
                    System.out.printf("ℹ️ 播放列表「%s」已存在，无需创建示例数据。%n", sample.name());
                }
            }
        };
    }

    private record SamplePlaylist(String name, List<Song> songs) {
    }
}