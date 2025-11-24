package com.vibevault.controller;

import com.vibevault.model.User;
import com.vibevault.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody AuthRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }
        // 关键：加密密码
        String encodedPassword = passwordEncoder.encode(request.password());
        userRepository.save(new User(request.username(), encodedPassword));
        return "User registered successfully";
    }
    
    // 简单的DTO record
    public record AuthRequest(String username, String password) {}
}