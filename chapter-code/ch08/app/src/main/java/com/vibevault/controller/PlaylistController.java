package com.vibevault.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import com.vibevault.service.PlaylistService;
import com.vibevault.dto.PlaylistDTO;
import com.vibevault.dto.SongDTO;

@RestController // <--- 关键标签：声明这是一个RESTful控制器
@RequestMapping("/api/playlists") // <--- 声明这个控制器下所有API的URL基础路径
public class PlaylistController {

    private final PlaylistService playlistService;

    // <--- 通过构造函数注入Service Bean
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/{id}") // <--- 将此方法映射到 GET /api/playlists/{id} 请求
    public PlaylistDTO getPlaylist(@PathVariable String id) {
        // @PathVariable会将URL路径中的{id}部分，绑定到方法的id参数上
        return playlistService.getPlaylistById(id);
    }

    @PostMapping("/{id}/songs") // <--- 将此方法映射到 POST /api/playlists/{id}/songs 请求
    @ResponseStatus(HttpStatus.CREATED) // <--- 设定成功响应的HTTP状态码为201 Created，这是RESTful设计的最佳实践
    public void addSongToPlaylist(@PathVariable String id, @RequestBody SongDTO songDTO) {
        // @RequestBody会将HTTP请求体中的JSON内容，自动反序列化并绑定到SongDTO对象上
        playlistService.addSongToPlaylist(id, songDTO);
    }
}