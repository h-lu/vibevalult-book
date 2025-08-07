package com.vibevault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibevault.dto.SignUpRequest;
import com.vibevault.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 让每个测试都在事务中运行，测试结束后自动回滚，不污染数据库
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    // 在每个测试前，清空用户数据，确保测试环境的纯净
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void whenSignUpWithNewUser_thenReturns200() throws Exception {
        SignUpRequest newUser = new SignUpRequest("testuser1", "password123");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk());
    }

    @Test
    void whenSignInWithValidUser_thenReturns200AndToken() throws Exception {
        // 步骤1: 先注册一个用户
        SignUpRequest user = new SignUpRequest("testuser2", "password123");
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // 步骤2: 使用正确的凭证登录
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists()) // 验证返回的JSON中包含 "token" 字段
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.token").isNotEmpty()); // 验证token字段是非空字符串
    }

    @Test
    void whenSignInWithInvalidPassword_thenReturns403() throws Exception {
        // 步骤1: 先注册一个用户
        SignUpRequest user = new SignUpRequest("testuser3", "password123");
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        // 步骤2: 使用错误的密码登录
        SignUpRequest wrongCredentials = new SignUpRequest("testuser3", "wrongpassword");
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongCredentials)))
                .andExpect(status().isForbidden()); // 或 isForbidden()，取决于具体配置
    }
}