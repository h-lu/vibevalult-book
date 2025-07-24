package com.vibevault.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlaylistTest {

    private Playlist playlist;

    @BeforeEach
    void setUp() {
        // This method runs before each @Test method in this class.
        // It ensures that every test starts with a fresh, empty playlist.
        playlist = new Playlist("My Favorite Songs");
    }

    @Test
    @DisplayName("should have song count of 1 after adding the first song")
    void songCountShouldBeOneAfterAddingFirstSong() {
        // Arrange
        Song newSong = new Song("Stairway to Heaven", "Led Zeppelin", 482);
        
        // Act
        playlist.addSong(newSong);

        // Assert (using JUnit's assertEquals for simple cases is still fine)
        assertEquals(1, playlist.getSongCount());
    }

    @Test
    @DisplayName("should contain the added song")
    void shouldContainAddedSong() {
        // Arrange
        Song newSong = new Song("Bohemian Rhapsody", "Queen", 355);

        // Act
        playlist.addSong(newSong);

        // Assert (using AssertJ for more expressive, fluent assertions)
        // "Assert that the playlist's songs list contains the new song."
        assertThat(playlist.getSongs()).contains(newSong);
    }
}