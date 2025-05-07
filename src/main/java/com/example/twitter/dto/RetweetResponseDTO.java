package com.example.twitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetweetResponseDTO {
    private Long id;
    private UserResponseDTO user;
    private TweetResponseDTO originalTweet;
    private String additionalContent;
    private LocalDateTime retweetedAt;
}
