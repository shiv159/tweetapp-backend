package com.tweetapp.tweetapp_backend.repository;

import com.tweetapp.tweetapp_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // Find user by username (unique index)
    Optional<User> findByUsername(String username);

    // Check if username exists
    boolean existsByUsername(String username);
}
