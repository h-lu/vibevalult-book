package com.vibevault.dto;

// 只暴露必要字段，隐藏时长等内部细节。
public record SongDTO(Long id, String title, String artist) {
}