package com.example.twitter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {
    @NotNull(message = "Tweet ID is required")
    private Long tweetId;

    @NotBlank(message = "Comment content cannot be empty")
    @Size(max = 280, message = "Comment cannot exceed 280 characters")
    private String content;
}