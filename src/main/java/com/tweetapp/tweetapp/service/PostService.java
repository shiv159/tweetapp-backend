package com.tweetapp.tweetapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.tweetapp.tweetapp.dto.CommentRequest;
import com.tweetapp.tweetapp.dto.CreatePostRequest;
import com.tweetapp.tweetapp.model.Comment;
import com.tweetapp.tweetapp.model.Post;
import com.tweetapp.tweetapp.repository.PostRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    /**
     * Creates a new post for a user.
     * @param request the post creation request containing content and userId
     * @return the saved Post entity
     */
    public Post createPost(CreatePostRequest request) {
        Post post = new Post(request.getContent(), request.getUserId());
        return postRepository.save(post);
    }


    /**
     * Retrieves a post by its ID, using cache if available.
     * @param id the post ID
     * @return an Optional containing the Post if found, or empty otherwise
     */
    @Cacheable(value = "posts", key = "#id")
    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }


    /**
     * Retrieves all posts in the system.
     * @return an Iterable of all Post entities
     */
    public Iterable<Post> getAllPosts() {
        return postRepository.findAll();
    }


    /**
     * Retrieves all posts for a specific user.
     * @param userId the user ID (partition key)
     * @return an Iterable of Post entities for the user
     */
    public Iterable<Post> getPostsByUser(String userId) {
        return postRepository.findByUserId(userId);
    }


    /**
     * Toggles a like for a post by a user. If the user has already liked the post, the like is removed; otherwise, it is added.
     * @param postId the post ID
     * @param userId the user ID
     * @return true if the operation was successful, false if the post was not found
     */
    @CacheEvict(value = "posts", key = "#postId")
    public boolean toggleLike(String postId, String userId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return false;
        }

        Post post = postOpt.get();
        if (post.getLikes().contains(userId)) {
            post.getLikes().remove(userId);
        } else {
            post.getLikes().add(userId);
        }

        postRepository.save(post);
        return true;
    }


    /**
     * Adds a comment to a post, limited to 100 comments per post.
     * @param postId the post ID
     * @param request the comment request containing userId and content
     * @return true if the comment was added, false if the post was not found or the comment limit was reached
     */
    @CacheEvict(value = "posts", key = "#postId")
    public boolean addComment(String postId, CommentRequest request) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return false;
        }

        Post post = postOpt.get();

        // Limit comments to 100 per post
        if (post.getComments().size() >= 100) {
            return false;
        }

        Comment comment = new Comment(request.getUserId(), request.getContent());
        post.getComments().add(comment);

        postRepository.save(post);
        return true;
    }
}
