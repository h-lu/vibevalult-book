package com.vibevault.controller;

import com.vibevault.model.User;
import com.vibevault.repository.UserRepository;
import com.vibevault.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public String signup(@RequestBody SignupRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "用户名已被注册: " + request.username());
        }

        String encryptedPassword = passwordEncoder.encode(request.password());
        User newUser = new User(request.username(), encryptedPassword);
        userRepository.save(newUser);
        return "注册成功！用户名: " + request.username();
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "用户名或密码错误"
                ));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "用户名或密码错误"
            );
        }

        String token = jwtService.generateToken(user.getUsername());
        return new LoginResponse(token, user.getUsername());
    }

    public record SignupRequest(String username, String password) {
    }

    public record LoginRequest(String username, String password) {
    }

    public record LoginResponse(String token, String username) {
    }
}

