package com.tweetapp.tweetapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private String userId;
    private String content;
    private LocalDateTime createdAt;
    private String username;


    public Comment(String userId, String content, String username) {
        this.userId = userId;
        this.content = content;
        this.username = username;
        this.createdAt = LocalDateTime.now();
    }
}
