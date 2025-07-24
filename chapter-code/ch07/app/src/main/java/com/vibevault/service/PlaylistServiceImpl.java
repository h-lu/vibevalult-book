package com.vibevault.service;

import org.springframework.stereotype.Service;
import com.vibevault.repository.PlaylistRepository;
import com.vibevault.dto.PlaylistDTO;
import com.vibevault.dto.SongDTO;
import com.vibevault.model.Playlist;
import com.vibevault.model.Song;
import com.vibevault.exception.ResourceNotFoundException;
import java.util.stream.Collectors;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistRepository repository;

    public PlaylistServiceImpl(PlaylistRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public PlaylistDTO getPlaylistById(String id) {
        // 调用repository，如果返回的Optional为空，则立即抛出我们自定义的异常
        Playlist playlist = repository.load(id)
            .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + id));

        // [模型转换] 将内部的Playlist领域模型，安全地转换为外部的PlaylistDTO
        return new PlaylistDTO(
            playlist.getName(),
            playlist.getSongs().stream()
                .map(song -> new SongDTO(song.title(), song.artist())) // 只取需要暴露的字段
                .collect(Collectors.toList())
        );
    }

    @Override
    public void addSongToPlaylist(String playlistId, SongDTO songDTO) {
        // 先加载播放列表，如果不存在则同样会抛出404异常，确保操作的有效性
        Playlist playlist = repository.load(playlistId)
            .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + playlistId));
        
        // [模型转换] 将外部传入的SongDTO，转换为内部的Song领域模型
        // 注意：因为SongDTO没有时长信息，我们在这里使用一个默认值0
        Song newSong = new Song(songDTO.title(), songDTO.artist(), 0);
        
        // 执行业务逻辑并持久化
        playlist.addSong(newSong);
        repository.save(playlist);
    }
}