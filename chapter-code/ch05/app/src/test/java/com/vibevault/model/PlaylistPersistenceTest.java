package com.vibevault.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PlaylistPersistenceTest {

    @Test
    @DisplayName("saveToFile should call Files.write with correct content")
    void saveToFileShouldCallFilesWrite() {
        // Arrange
        Playlist playlist = new Playlist("My Test Playlist");
        playlist.addSong(new Song("Hey Jude", "The Beatles", 431));
        
        // This is the magic: we create a "mocking scope" for the Files class
        try (var mockedFiles = mockStatic(Files.class)) {
            // Act
            playlist.saveToFile("test.csv");

            // Assert
            // We now verify the static method call on the mocked class
            ArgumentCaptor<Path> pathCaptor = ArgumentCaptor.forClass(Path.class);
            ArgumentCaptor<Iterable<String>> contentCaptor = ArgumentCaptor.forClass(Iterable.class);
            
            mockedFiles.verify(() -> Files.write(pathCaptor.capture(), contentCaptor.capture()));

            assertThat(pathCaptor.getValue()).hasToString("test.csv");
            assertThat(contentCaptor.getValue()).containsExactly("Hey Jude,The Beatles,431");
        }
    }

    @Test
    @DisplayName("loadFromFile should update playlist from file content")
    void loadFromFileShouldUpdatePlaylist() {
        // Arrange
        Playlist playlist = new Playlist("My Loaded Playlist");
        Path filePath = Paths.get("existing-playlist.csv");
        List<String> fakeCsvLines = List.of("Yesterday,The Beatles,121", "Let It Be,The Beatles,243");

        try (var mockedFiles = mockStatic(Files.class)) {
            // We "stub" the static method call.
            // When Files.readAllLines is called with our path, return our fake data.
            mockedFiles.when(() -> Files.readAllLines(filePath)).thenReturn(fakeCsvLines);
            
            // Act
            playlist.loadFromFile(filePath.toString());

            // Assert
            // We can now use the getters you created in the previous lessons!
            assertThat(playlist.getSongCount()).isEqualTo(2);
            assertThat(playlist.getSongs())
                .extracting(Song::title) // A cool AssertJ feature to check only the titles
                .containsExactly("Yesterday", "Let It Be");
        }
    }
}