package com.github.zigcat.blogplatform.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private int id;
    private String username;
    private String nickname;
    private String email;
    private String role;
    private String creationDate;
}
