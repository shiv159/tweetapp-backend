package com.tweetapp.tweetapp_backend.repository;

import com.tweetapp.tweetapp_backend.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    // Find posts by userId (partition key)
    Page<Post> findByUserId(String userId, Pageable pageable);

    // Find all posts ordered by createdAt for feed
    @Query("{}")
    Page<Post> findAllPosts(Pageable pageable);

    // Find post by id and partition key for efficient querying
    Optional<Post> findByIdAndPartitionKey(String id, String partitionKey);
}
