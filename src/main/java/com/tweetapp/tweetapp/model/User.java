package com.tweetapp.tweetapp.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Container(containerName = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @org.springframework.data.annotation.Id
    @GeneratedValue
    private String id;

    @PartitionKey
    private String username;

    public User(String username) {
        this.username = username;
    }
}
