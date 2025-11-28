package com.vibevault.service;

import com.vibevault.dto.PlaylistCreateDTO;
import com.vibevault.dto.PlaylistDTO;
import com.vibevault.dto.SongCreateDTO;
import com.vibevault.dto.SongDTO;
import com.vibevault.exception.ResourceNotFoundException;
import com.vibevault.model.Playlist;
import com.vibevault.model.Song;
import com.vibevault.repository.PlaylistRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    private final PlaylistRepository repository;

    public PlaylistServiceImpl(PlaylistRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PlaylistDTO> getAllPlaylists() {
        return repository.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    @Override
    public PlaylistDTO getPlaylistById(long id) {
        Playlist playlist = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + id));
        return mapToDto(playlist);
    }

    @Override
    @Transactional
    public PlaylistDTO createPlaylist(PlaylistCreateDTO playlistCreateDTO) {
        Playlist playlist = new Playlist(playlistCreateDTO.name());
        Playlist saved = repository.save(playlist);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public void addSongToPlaylist(long playlistId, SongCreateDTO songCreateDTO) {
        Playlist playlist = repository.findById(playlistId)
            .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + playlistId));
        Song newSong = new Song(songCreateDTO.title(), songCreateDTO.artist(), 0);
        playlist.addSong(newSong);
        repository.save(playlist);
    }

    @Override
    @Transactional
    public void removeSongFromPlaylist(long playlistId, long songId) {
        Playlist playlist = repository.findById(playlistId)
            .orElseThrow(() -> new ResourceNotFoundException("Playlist not found with id: " + playlistId));
        Song songToRemove = playlist.getSongs().stream()
            .filter(song -> Objects.equals(song.getId(), songId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Song not found with id: " + songId));
        playlist.removeSong(songToRemove);
        repository.save(playlist);
    }

    @Override
    @Transactional
    public void deletePlaylist(long playlistId) {
        if (!repository.existsById(playlistId)) {
            throw new ResourceNotFoundException("Playlist not found with id: " + playlistId);
        }
        repository.deleteById(playlistId);
    }

    private PlaylistDTO mapToDto(Playlist playlist) {
        Long playlistId = Objects.requireNonNull(playlist.getId(), "Playlist id should not be null");
        List<SongDTO> songs = playlist.getSongs().stream()
            .map(this::mapToSongDto)
            .collect(Collectors.toList());
        return new PlaylistDTO(playlistId, playlist.getName(), songs);
    }

    private SongDTO mapToSongDto(Song song) {
        Long songId = Objects.requireNonNull(song.getId(), "Song id should not be null");
        return new SongDTO(songId, song.getTitle(), song.getArtist());
    }
}