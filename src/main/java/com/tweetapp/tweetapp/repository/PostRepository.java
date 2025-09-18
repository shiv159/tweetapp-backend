package com.tweetapp.tweetapp.repository;

import com.tweetapp.tweetapp.model.Post;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends CosmosRepository<Post, String> {

    // Find posts by userId (partition key)
    Iterable<Post> findByUserId(String userId);

    // Find post by id and partition key for efficient querying (if you use partition key)
    Optional<Post> findByIdAndUserId(String id, String userId);
}
