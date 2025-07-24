package com.vibevault.dto;

import java.util.List;

public record PlaylistDTO(String name, List<SongDTO> songs) {
}
