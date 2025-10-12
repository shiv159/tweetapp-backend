package com.tweetapp.tweetapp.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Container(containerName = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @org.springframework.data.annotation.Id
    @GeneratedValue
    private String postId;

    private String content;

    @PartitionKey
    private String userId;

    private List<Likes> likes = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime createdAt;

    public Post(String content, String userId) {
        this.content = content;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }
}
