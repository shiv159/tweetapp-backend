package com.tweetapp.tweetapp_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private String error;
    private String message;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, null, "Success");
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, null, message);
    }

    public static <T> ApiResponse<T> error(String error, String message) {
        return new ApiResponse<>(null, error, message);
    }
}
