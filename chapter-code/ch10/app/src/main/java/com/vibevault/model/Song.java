package com.vibevault.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity // <--- 1. 声明为实体
@Table(name = "songs") // <--- 2. 映射到'songs'表
public class Song {

    @Id // <--- 3. 标记主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <--- 4. 主键生成策略：由数据库自增
    private Long id;

    private String title;
    private String artist;
    private int durationInSeconds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id") // <-- 外键列改为 playlist_id
    private Playlist playlist;
    
    public Song(String title, String artist, int durationInSeconds) {
        this.title = title;
        this.artist = artist;
        this.durationInSeconds = durationInSeconds;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getDurationInSeconds() { return durationInSeconds; }
    public Playlist getPlaylist() { return playlist; }

    // Setter for the bidirectional relationship
    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    // 移除了 toCsvString 和 fromCsvString，因为我们不再需要它们
}