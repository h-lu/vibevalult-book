package com.vibevault.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // 'user' 是数据库保留字，所以用 'users'
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // 存储加密后的密码

    // Getters, Setters, Constructors
    public User() {}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}