package com.vibevault.security;

import com.vibevault.repository.PlaylistRepository;
import org.springframework.stereotype.Component;

@Component("securityUtils")
public class SecurityUtils {

    private final PlaylistRepository playlistRepository;

    public SecurityUtils(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public boolean isOwner(Long playlistId, String username) {
        return playlistRepository.findById(playlistId)
                .map(playlist -> playlist.getOwner().getUsername().equals(username))
                .orElse(false);
    }
}
