package com.tweetapp.tweetapp_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    private String id;

    private String content;

    @Indexed
    private String userId;

    private List<String> likes = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

    @Indexed
    private LocalDateTime createdAt;

    // Partition key for Cosmos DB
    private String partitionKey;

    public Post(String content, String userId) {
        this.content = content;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.partitionKey = userId;
    }
}
