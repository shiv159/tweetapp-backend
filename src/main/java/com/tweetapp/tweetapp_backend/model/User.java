package com.tweetapp.tweetapp_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    // Partition key for Cosmos DB
    private String partitionKey;

    public User(String username) {
        this.username = username;
        this.partitionKey = id; // Will be set after ID generation
    }
}
