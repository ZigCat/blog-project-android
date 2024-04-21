package com.github.zigcat.blogplatform.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String nickname;
    private String email;
    private String password;
    private String role;
}
