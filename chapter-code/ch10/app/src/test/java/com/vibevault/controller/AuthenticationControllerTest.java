package com.vibevault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibevault.dto.SignUpRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenSignUpWithCorsRequest_shouldReturnOkAndJwt() throws Exception {
        SignUpRequest newUserRequest = new SignUpRequest("testuser_cors", "password123");

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserRequest))
                .header("Origin", "http://localhost:5173")) // 模拟一个CORS跨域请求
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void whenSignUpWithoutOriginHeader_shouldReturnOkAndJwt() throws Exception {
        // 这个测试完全模拟你失败的curl命令场景
        SignUpRequest newUserRequest = new SignUpRequest("testuser_no_origin", "password123");

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUserRequest))) // <-- 注意：这里没有Origin头
                .andExpect(status().isOk()) // 验证在禁用CSRF后，非CORS请求也能成功
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void whenCorsPreflightRequest_shouldReturnOk() throws Exception {
        mockMvc.perform(options("/api/auth/signup")
                .header("Origin", "http://localhost:5173")
                .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk());
    }
}
