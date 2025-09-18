package com.tweetapp.tweetapp_backend.service;

import com.tweetapp.tweetapp_backend.dto.CommentRequest;
import com.tweetapp.tweetapp_backend.dto.CreatePostRequest;
import com.tweetapp.tweetapp_backend.model.Comment;
import com.tweetapp.tweetapp_backend.model.Post;
import com.tweetapp.tweetapp_backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post createPost(CreatePostRequest request) {
        Post post = new Post(request.getContent(), request.getUserId());
        return postRepository.save(post);
    }

    @Cacheable(value = "posts", key = "#id")
    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    public Page<Post> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAllPosts(pageable);
    }

    public Page<Post> getPostsByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByUserId(userId, pageable);
    }

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
