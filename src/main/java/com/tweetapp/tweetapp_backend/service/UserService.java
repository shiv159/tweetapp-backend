package com.tweetapp.tweetapp_backend.service;

import com.tweetapp.tweetapp_backend.model.User;
import com.tweetapp.tweetapp_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Cacheable(value = "users", key = "#username")
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User(username);
        User savedUser = userRepository.save(user);
        savedUser.setPartitionKey(savedUser.getId()); // Set partition key after ID generation
        return userRepository.save(savedUser);
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
