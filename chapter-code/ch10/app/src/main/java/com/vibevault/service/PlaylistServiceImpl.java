package com.vibevault.service;

import com.vibevault.dto.PlaylistDTO;
import com.vibevault.dto.SongCreateDTO;
import com.vibevault.dto.SongDTO;
import com.vibevault.exception.ResourceNotFoundException;
import com.vibevault.model.Playlist;
import com.vibevault.model.Song;
import com.vibevault.model.User;
import com.vibevault.repository.PlaylistRepository;
import com.vibevault.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository,
                               UserRepository userRepository) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<PlaylistDTO> getAllPlaylists() {
        return playlistRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PlaylistDTO getPlaylistById(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + id));
        return mapToDto(playlist);
    }

    @Override
    @Transactional
    public PlaylistDTO createPlaylist(String name, String ownerUsername) {
        User owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "用户不存在: " + ownerUsername
                ));

        Playlist playlist = new Playlist(name, owner);
        Playlist saved = playlistRepository.save(playlist);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public void addSongToPlaylist(Long playlistId, SongCreateDTO songCreateDTO, String currentUsername) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + playlistId));

        enforceOwnership(playlist, currentUsername);

        Song newSong = new Song(songCreateDTO.title(), songCreateDTO.artist(), 0);
        playlist.addSong(newSong);
        playlistRepository.save(playlist);
    }

    @Override
    @Transactional
    public void removeSongFromPlaylist(Long playlistId, Long songId, String currentUsername) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + playlistId));

        enforceOwnership(playlist, currentUsername);

        Song targetSong = playlist.getSongs().stream()
                .filter(song -> song.getId() != null && song.getId().equals(songId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + songId));

        playlist.removeSong(targetSong);
        playlistRepository.save(playlist);
    }

    @Override
    @Transactional
    public void deletePlaylist(Long playlistId, String currentUsername) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + playlistId));

        enforceOwnership(playlist, currentUsername);
        playlistRepository.delete(playlist);
    }

    private void enforceOwnership(Playlist playlist, String currentUsername) {
        if (!playlist.getOwner().getUsername().equals(currentUsername)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "您无权操作此歌单"
            );
        }
    }

    private PlaylistDTO mapToDto(Playlist playlist) {
        List<SongDTO> songs = playlist.getSongs().stream()
                .map(this::mapToSongDto)
                .collect(Collectors.toList());
        return new PlaylistDTO(playlist.getId(), playlist.getName(), songs);
    }

    private SongDTO mapToSongDto(Song song) {
        return new SongDTO(song.getId(), song.getTitle(), song.getArtist());
    }
}