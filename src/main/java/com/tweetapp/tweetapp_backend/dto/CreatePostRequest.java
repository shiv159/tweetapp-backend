package com.tweetapp.tweetapp_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    @NotBlank(message = "Content cannot be empty")
    @Size(max = 280, message = "Content cannot exceed 280 characters")
    private String content;

    @NotBlank(message = "User ID cannot be empty")
    private String userId;
}
