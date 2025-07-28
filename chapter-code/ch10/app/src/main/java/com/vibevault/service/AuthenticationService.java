package com.vibevault.service;

import com.vibevault.dto.JwtAuthenticationResponse;
import com.vibevault.dto.SignUpRequest;
import com.vibevault.model.User;
import com.vibevault.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService; // 注入JwtService

    /**
     * 注册新用户，并直接返回JWT，实现注册后自动登录
     */
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        
        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * 用户登录认证，成功后返回JWT
     */
    public JwtAuthenticationResponse signin(SignUpRequest request) {
        // 触发Spring Security的标准认证流程
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        
        // 认证成功后，直接从Authentication对象中获取UserDetails，无需再次查询数据库
        User user = (User) authentication.getPrincipal();
        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}