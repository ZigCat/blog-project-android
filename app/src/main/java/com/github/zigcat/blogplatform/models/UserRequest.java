package com.github.zigcat.blogplatform.models;

public class UserRequest {
    private String username;
    private String nickname;
    private String email;
    private String password;
    private String role;

    public UserRequest(String username, String nickname, String email, String password, String role) {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
