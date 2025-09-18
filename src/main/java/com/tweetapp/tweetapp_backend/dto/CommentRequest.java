package com.tweetapp.tweetapp_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Content cannot be empty")
    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String content;

    @NotBlank(message = "User ID cannot be empty")
    private String userId;
}
