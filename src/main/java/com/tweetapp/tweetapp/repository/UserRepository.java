package com.tweetapp.tweetapp.repository;

import com.tweetapp.tweetapp.model.User;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CosmosRepository<User, String> {

    // Find user by username (unique index)
    Optional<User> findByUsername(String username);

    // Check if username exists
    boolean existsByUsername(String username);
}
