package com.github.zigcat.blogplatform.models;

public class Post {
    private int id;
    private String content;
    private String creationDate;
    private User user;

    public Post() {
    }

    public Post(int id, String content, String creationDate, User user) {
        this.id = id;
        this.content = content;
        this.creationDate = creationDate;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
