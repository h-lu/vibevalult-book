package com.vibevault.service;


import com.vibevault.model.Playlist;
import com.vibevault.model.Song;
import com.vibevault.repository.PlaylistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 启用Mockito扩展
    class PlaylistServiceImplTest {

        @Mock // 创建一个PlaylistRepository的模拟对象
        private PlaylistRepository repository;

        @InjectMocks // 创建PlaylistServiceImpl实例，并自动注入上面@Mock标记的对象
        private PlaylistServiceImpl playlistService;

        @BeforeEach
        void setUp() {
            // 当repository.load()被以任意字符串参数调用时，都返回一个包含一首歌的新Playlist
            Playlist initialPlaylist = new Playlist("My Test Playlist");
            initialPlaylist.addSong(new Song("Bohemian Rhapsody", "Queen", 355));
            when(repository.load(anyString())).thenReturn(initialPlaylist);
            
            // 重新初始化service，以确保load在每个测试前都被正确地stub
            playlistService = new PlaylistServiceImpl(repository);
        }

        @Test
        @DisplayName("添加新歌曲后，歌曲列表应包含该歌曲")
        void addSong_shouldContainTheNewSong() {
            // Arrange
            Song newSong = new Song("Stairway to Heaven", "Led Zeppelin", 482);

            // Act
            playlistService.addSong(newSong);
            String songList = playlistService.listSongs();

            // Assert
            assertThat(songList).contains("Stairway to Heaven");
            assertThat(songList).contains("Bohemian Rhapsody"); // 确认旧歌还在
        }

        @Test
        @DisplayName("保存数据时，应该调用repository的save方法")
        void saveData_shouldCallRepositorySave() {
            // Act
            playlistService.saveData();

            // Assert
            // 验证repository.save()方法是否被调用了，并且是带着我们期望的Playlist对象调用的
            verify(repository, times(1)).save(any(Playlist.class));
        }
    }