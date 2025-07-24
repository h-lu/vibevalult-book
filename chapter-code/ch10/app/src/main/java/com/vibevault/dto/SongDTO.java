package com.vibevault.dto;

// 我们决定只在API中暴露歌曲的标题和艺术家，
// 隐藏`durationInSeconds`这个内部实现细节。
public record SongDTO(String title, String artist) {
}