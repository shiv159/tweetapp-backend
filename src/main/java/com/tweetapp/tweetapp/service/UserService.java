package com.tweetapp.tweetapp.service;

import lombok.RequiredArgsConstructor;
// import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tweetapp.tweetapp.model.User;
import com.tweetapp.tweetapp.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // For hashing passwords securely

    /**
     * Retrieves a user by username.
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Creates a new user with the given details.
     * The password is hashed before saving for security.
     *
     * @param username the username for the new user
     * @param password the plain text password (will be hashed)
     * @param email the user's email address
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param dateOfBirth the user's date of birth (YYYY-MM-DD format)
     * @return the created User object
     * @throws IllegalArgumentException if username already exists
     */
    public User createUser(String username, String password, String email, String firstName, String lastName, String dateOfBirth) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(username, hashedPassword, email, firstName, lastName, dateOfBirth);
        return userRepository.save(user);
    }

    /**
     * Checks if a user exists by username.
     */
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
