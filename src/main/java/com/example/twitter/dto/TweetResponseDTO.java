package com.example.twitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetResponseDTO {
    private Long id;
    private String content;
    private UserResponseDTO user;
    private List<CommentResponseDTO> comments = new ArrayList<>();
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;
    private int retweetCount;
    private boolean isLiked;
    private boolean isRetweeted;
}