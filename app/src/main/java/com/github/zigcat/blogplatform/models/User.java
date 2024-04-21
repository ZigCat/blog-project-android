package com.github.zigcat.blogplatform.models;

public class User {
    private int id;
    private String username;
    private String nickname;
    private String email;
    private String role;
    private String creationDate;

    public User() {
    }

    public User(int id, String username, String nickname, String email, String role, String creationDate) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
        this.creationDate = creationDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
