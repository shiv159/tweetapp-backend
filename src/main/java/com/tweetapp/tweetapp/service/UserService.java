package com.tweetapp.tweetapp.service;

import lombok.RequiredArgsConstructor;
// import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tweetapp.tweetapp.model.User;
import com.tweetapp.tweetapp.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User(username);
        return userRepository.save(user);
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
