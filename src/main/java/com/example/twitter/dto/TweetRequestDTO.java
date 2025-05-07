package com.example.twitter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetRequestDTO {
    @NotBlank(message = "Content cannot be empty")
    @Size(max = 280, message = "Tweet cannot exceed 280 characters")
    private String content;
}