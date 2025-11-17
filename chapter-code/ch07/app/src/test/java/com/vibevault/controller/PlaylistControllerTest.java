package com.vibevault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibevault.dto.SongDTO;
import com.vibevault.exception.ResourceNotFoundException;
import com.vibevault.repository.PlaylistRepository;
import com.vibevault.service.PlaylistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlaylistController.class) // <--- 只加载 Web 层，不加载完整的 Spring 上下文
class PlaylistControllerTest {

    @Autowired
    private MockMvc mockMvc; // <--- Spring 提供的 MockMvc，用于模拟 HTTP 请求

    private final ObjectMapper objectMapper = new ObjectMapper(); // <--- 手动创建 ObjectMapper 用于 JSON 序列化和反序列化

    @MockBean // <--- 创建 PlaylistService 的 Mock 对象，替代真实的 Service
    private PlaylistService playlistService;

    @MockBean // <--- 创建 PlaylistRepository 的 Mock 对象，避免加载真实的 Repository
    private PlaylistRepository playlistRepository;

    @Test
    void testAddSongToPlaylist_Success() throws Exception {
        // Arrange: 准备测试数据
        String playlistId = "my-favorites";
        SongDTO songDTO = new SongDTO("Imagine", "John Lennon");
        String requestBody = objectMapper.writeValueAsString(songDTO);

        // 配置 Mock 行为：当调用 addSongToPlaylist 时，不抛出异常（表示成功）
        doNothing().when(playlistService).addSongToPlaylist(eq(playlistId), any(SongDTO.class));

        // Act & Assert: 执行请求并验证结果
        mockMvc.perform(post("/api/playlists/{id}/songs", playlistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated()) // <--- 验证 HTTP 状态码为 201
                .andExpect(content().string("")); // <--- 验证响应体为空

        // 验证 Service 方法被正确调用
        verify(playlistService, times(1)).addSongToPlaylist(eq(playlistId), any(SongDTO.class));
    }

    @Test
    void testAddSongToPlaylist_PlaylistNotFound() throws Exception {
        // Arrange: 准备测试数据
        String playlistId = "non-existent";
        SongDTO songDTO = new SongDTO("Imagine", "John Lennon");
        String requestBody = objectMapper.writeValueAsString(songDTO);

        // 配置 Mock 行为：当播放列表不存在时，抛出异常
        doThrow(new ResourceNotFoundException("Playlist not found with id: " + playlistId))
                .when(playlistService).addSongToPlaylist(eq(playlistId), any(SongDTO.class));

        // Act & Assert: 执行请求并验证返回 404
        mockMvc.perform(post("/api/playlists/{id}/songs", playlistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound()); // <--- 验证 HTTP 状态码为 404

        verify(playlistService, times(1)).addSongToPlaylist(eq(playlistId), any(SongDTO.class));
    }

    @Test
    void testRemoveSongFromPlaylist_Success() throws Exception {
        // Arrange: 准备测试数据
        String playlistId = "my-favorites";
        String songTitle = "Bohemian Rhapsody";

        // 配置 Mock 行为：删除操作成功
        doNothing().when(playlistService).removeSongFromPlaylist(eq(playlistId), eq(songTitle));

        // Act & Assert: 执行 DELETE 请求并验证结果
        mockMvc.perform(delete("/api/playlists/{id}/songs", playlistId)
                        .param("title", songTitle)) // <--- 添加查询参数
                .andExpect(status().isNoContent()) // <--- 验证 HTTP 状态码为 204
                .andExpect(content().string("")); // <--- 验证响应体为空

        // 验证 Service 方法被正确调用
        verify(playlistService, times(1)).removeSongFromPlaylist(eq(playlistId), eq(songTitle));
    }

    @Test
    void testRemoveSongFromPlaylist_PlaylistNotFound() throws Exception {
        // Arrange: 准备测试数据
        String playlistId = "non-existent";
        String songTitle = "Some Song";

        // 配置 Mock 行为：播放列表不存在时抛出异常
        doThrow(new ResourceNotFoundException("Playlist not found with id: " + playlistId))
                .when(playlistService).removeSongFromPlaylist(eq(playlistId), eq(songTitle));

        // Act & Assert: 执行请求并验证返回 404
        mockMvc.perform(delete("/api/playlists/{id}/songs", playlistId)
                        .param("title", songTitle))
                .andExpect(status().isNotFound()); // <--- 验证 HTTP 状态码为 404

        verify(playlistService, times(1)).removeSongFromPlaylist(eq(playlistId), eq(songTitle));
    }

    @Test
    void testRemoveSongFromPlaylist_SongNotFound() throws Exception {
        // Arrange: 准备测试数据
        String playlistId = "my-favorites";
        String songTitle = "Non-existent Song";

        // 配置 Mock 行为：歌曲不存在时抛出异常
        doThrow(new ResourceNotFoundException("Song with title '" + songTitle + "' not found in playlist: " + playlistId))
                .when(playlistService).removeSongFromPlaylist(eq(playlistId), eq(songTitle));

        // Act & Assert: 执行请求并验证返回 404
        mockMvc.perform(delete("/api/playlists/{id}/songs", playlistId)
                        .param("title", songTitle))
                .andExpect(status().isNotFound()); // <--- 验证 HTTP 状态码为 404

        verify(playlistService, times(1)).removeSongFromPlaylist(eq(playlistId), eq(songTitle));
    }

    @Test
    void testRemoveSongFromPlaylist_WithUrlEncodedTitle() throws Exception {
        // Arrange: 准备测试数据（标题包含空格）
        String playlistId = "my-favorites";
        String songTitle = "Stairway to Heaven";

        // 配置 Mock 行为
        doNothing().when(playlistService).removeSongFromPlaylist(eq(playlistId), eq(songTitle));

        // Act & Assert: 测试 URL 编码的标题参数
        mockMvc.perform(delete("/api/playlists/{id}/songs", playlistId)
                        .param("title", songTitle))
                .andExpect(status().isNoContent());

        verify(playlistService, times(1)).removeSongFromPlaylist(eq(playlistId), eq(songTitle));
    }
}

