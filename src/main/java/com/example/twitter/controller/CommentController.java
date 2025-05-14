package com.example.twitter.controller;

import com.example.twitter.dto.CommentRequestDTO;
import com.example.twitter.dto.CommentResponseDTO;
import com.example.twitter.service.CommentService;
import com.example.twitter.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        log.info("Creating new comment for tweet: {}", commentRequestDTO.getTweetId());
        return new ResponseEntity<>(commentService.createComment(commentRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/tweet/{tweetId}")
    public ResponseEntity<List<CommentResponseDTO>> findCommentsByTweetId(@PathVariable Long tweetId) {
        log.info("Fetching comments for tweet: {}", tweetId);
        return ResponseEntity.ok(commentService.findCommentsByTweetId(tweetId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long id, @RequestBody CommentRequestDTO commentRequestDTO) {
        log.info("Updating comment with id: {}", id);
        return ResponseEntity.ok(commentService.updateComment(id, commentRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.info("Deleting comment with id: {}", id);
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

}




























