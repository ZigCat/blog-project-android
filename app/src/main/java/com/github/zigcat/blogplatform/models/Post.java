package com.github.zigcat.blogplatform.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Post {
    private int id;
    private String content;
    private String creationDate;
    private User user;
}
