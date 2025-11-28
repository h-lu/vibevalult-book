package com.vibevault.dto;

import java.util.List;

public record PlaylistDTO(Long id, String name, List<SongDTO> songs) {
}
