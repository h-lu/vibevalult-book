package com.vibevault;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vibevault.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtService jwtService;

    @Test
    void unauthenticated_request_should_fail() throws Exception {
        mockMvc.perform(get("/api/playlists"))
                .andExpect(status().isForbidden()); // 403 Forbidden
    }

    @Test
    void authenticated_request_should_succeed() throws Exception {
        // 1. 生成一个合法的Token (注意：这里假设"testuser"不需要真实存在于数据库，只要Token签名正确即可通过Filter)
        String token = jwtService.generateToken("testuser");

        // 2. 带着Token去访问
        mockMvc.perform(get("/api/playlists")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
