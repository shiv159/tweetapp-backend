package com.tweetapp.tweetapp_backend.controller;

import com.tweetapp.tweetapp_backend.dto.ApiResponse;
import com.tweetapp.tweetapp_backend.dto.CommentRequest;
import com.tweetapp.tweetapp_backend.dto.CreatePostRequest;
import com.tweetapp.tweetapp_backend.model.Post;
import com.tweetapp.tweetapp_backend.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<Post>> createPost(@Valid @RequestBody CreatePostRequest request) {
        try {
            Post post = postService.createPost(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(post, "Post created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("INTERNAL_ERROR", "Failed to create post"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Post>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Page<Post> posts = postService.getAllPosts(page, size);
            return ResponseEntity.ok(ApiResponse.success(posts));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("INTERNAL_ERROR", "Failed to retrieve posts"));
        }
    }

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

    @PutMapping("/{id}/like")
    public ResponseEntity<ApiResponse<String>> toggleLike(
            @PathVariable String id,
            @RequestParam String userId) {
        try {
            boolean success = postService.toggleLike(id, userId);
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

    @PostMapping("/{id}/comment")
    public ResponseEntity<ApiResponse<String>> addComment(
            @PathVariable String id,
            @Valid @RequestBody CommentRequest request) {
        try {
            boolean success = postService.addComment(id, request);
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
