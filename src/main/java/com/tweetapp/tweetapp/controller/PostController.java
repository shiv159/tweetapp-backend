package com.tweetapp.tweetapp.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.tweetapp.tweetapp.dto.ApiResponse;
import com.tweetapp.tweetapp.dto.CommentRequest;
import com.tweetapp.tweetapp.dto.CreatePostRequest;
import com.tweetapp.tweetapp.model.Post;
import com.tweetapp.tweetapp.service.PostService;
import com.tweetapp.tweetapp.security.JwtAuthenticatedUser;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * Creates a new post based on the provided request data.
     * Validates the request body and saves the post to the database.
     * Returns the created post with a success message or an error if creation fails.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Post>> createPost(
            @AuthenticationPrincipal JwtAuthenticatedUser currentUser,
            @Valid @RequestBody CreatePostRequest request) {
        try {
            Post post = postService.createPost(currentUser.getUserId(), request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(post, "Post created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("INTERNAL_ERROR", "Failed to create post"));
        }
    }

    /**
     * Retrieves all posts from the database.
     * Returns a list of all posts or an error if retrieval fails.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<Post>>> getAllPosts() {
        try {
            Iterable<Post> posts = postService.getAllPosts();
            return ResponseEntity.ok(ApiResponse.success(posts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("INTERNAL_ERROR", "Failed to retrieve posts"));
        }
    }

    /**
     * Retrieves a specific post by its unique ID.
     * Returns the post if found, or a 404 error if not found, or an internal error if retrieval fails.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Post>> getPostById(@PathVariable String id) {
        try {
            return postService.getPostById(id)
                    .map(post -> ResponseEntity.ok(ApiResponse.success(post)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("NOT_FOUND", "Post not found")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("INTERNAL_ERROR", "Failed to retrieve post"));
        }
    }

    /**
     * Toggles the like status for a post by a specific user.
     * If the user has not liked the post, it adds a like; if already liked, it removes the like.
     * Requires the post ID and user ID as parameters.
     * Returns success if toggled, or 404 if post not found, or error if operation fails.
     */
    @PutMapping("/{id}/like")
    public ResponseEntity<ApiResponse<String>> toggleLike(
            @PathVariable String id,
            @AuthenticationPrincipal JwtAuthenticatedUser currentUser) {
        try {
            boolean success = postService.toggleLike(id, currentUser.getUserId());
            if (success) {
                return ResponseEntity.ok(ApiResponse.success("Like toggled successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("NOT_FOUND", "Post not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("INTERNAL_ERROR", "Failed to toggle like"));
        }
    }

    /**
     * Adds a comment to a specific post.
     * Validates the comment request and associates it with the post.
     * Returns success if added, or 400 if failed (e.g., post not found or comment limit), or error if operation fails.
     */
    @PostMapping("/{id}/comment")
    public ResponseEntity<ApiResponse<String>> addComment(
            @PathVariable String id,
            @AuthenticationPrincipal JwtAuthenticatedUser currentUser,
            @Valid @RequestBody CommentRequest request) {
        try {
            boolean success = postService.addComment(id, currentUser.getUserId(), request);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success("Comment added successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("BAD_REQUEST", "Failed to add comment (post not found or comment limit reached)"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("INTERNAL_ERROR", "Failed to add comment"));
        }
    }
}
