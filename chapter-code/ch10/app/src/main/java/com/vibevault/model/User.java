package com.vibevault.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // 这里将存储BCrypt加密后的哈希值

    // --- UserDetails 方法实现 ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 在本章我们暂不涉及角色和权限，因此返回一个空列表
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 为简化起见，我们假设账户永不过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 账户永不锁定
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 凭证永不过期
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 账户永远启用
        return true;
    }
}