package com.vibevault.model;

import jakarta.persistence.*;

@Entity // <--- 1. 声明为实体
@Table(name = "songs") // <--- 2. 映射到'songs'表
public class Song {

    @Id // <--- 3. 标记主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <--- 4. 主键生成策略：由数据库自增
    private Long id;

    private String title;
    private String artist;
    private int durationInSeconds;

    // --- 5. 定义多对一关系 ---
    @ManyToOne(fetch = FetchType.LAZY) // LAZY: 懒加载，只有在实际访问playlist时才从数据库加载
    @JoinColumn(name = "playlist_name") // 定义外键列的列名为'playlist_name'
    private Playlist playlist;
    
    // --- 构造函数和方法 ---

    // JPA需要无参构造函数
    public Song() {}
    
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