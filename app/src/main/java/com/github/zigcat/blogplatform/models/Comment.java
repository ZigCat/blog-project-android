package com.github.zigcat.blogplatform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private int id;
    private String content;
    private Post post;
    private User user;
    private String creationDate;
}
