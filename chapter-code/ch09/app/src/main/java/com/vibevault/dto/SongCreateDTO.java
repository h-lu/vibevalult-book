package com.vibevault.dto;

/**
 * 用于前端提交的新歌曲请求，只包含需要的字段。
 */
public record SongCreateDTO(String title, String artist) {
}

