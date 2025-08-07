package com.vibevault.model;

import jakarta.persistence.*; // 引入JPA注解
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity // <--- 1. 声明这是一个JPA实体
@Table(name = "playlists") // <--- 2. 映射到数据库的'playlists'表
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- 改为主键自增
    private Long id;

    @Column(nullable = false) // <-- name变为普通字段
    private String name;

    // --- 4. 定义一对多关系 ---
    @OneToMany(
        mappedBy = "playlist", // 指明这个关系由Song实体的'playlist'字段来维护
        cascade = CascadeType.ALL, // 级联操作：对Playlist的操作（如保存、删除）会自动应用到其关联的Songs
        orphanRemoval = true // 孤儿移除：从songs列表中移除的Song实体，将自动从数据库中删除
    )
    private List<Song> songs = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // 关键！防止序列化死循环
    private User user;

    // --- 构造函数和方法 ---
    public Playlist(String name) {
        this.name = name;
    }

    // JPA需要一个无参的构造函数
    protected Playlist() {}

    public String getName() {
        return name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        songs.add(song);
        song.setPlaylist(this); // 关键：维护双向关系
    }

    public void removeSong(Song song) {
        songs.remove(song);
        song.setPlaylist(null);
    }
}