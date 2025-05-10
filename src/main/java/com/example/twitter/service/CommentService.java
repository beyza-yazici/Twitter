package com.example.twitter.service;

import com.example.twitter.dto.CommentRequestDTO;
import com.example.twitter.dto.CommentResponseDTO;

import java.util.List;

public interface CommentService {

    CommentResponseDTO createComment (CommentRequestDTO commentRequestDTO);
    CommentResponseDTO updateComment(Long id, CommentRequestDTO commentRequestDTO);
    void deleteComment (Long id);
    List<CommentResponseDTO> findCommentsByTweetId(Long tweetId);
}
