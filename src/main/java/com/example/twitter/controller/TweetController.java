package com.example.twitter.controller;

import com.example.twitter.dto.*;
import com.example.twitter.service.CommentService;
import com.example.twitter.service.LikeService;
import com.example.twitter.service.RetweetService;
import com.example.twitter.service.TweetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweet")
@RequiredArgsConstructor
@Slf4j
public class TweetController {

    private final TweetService tweetService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final RetweetService retweetService;

    @PostMapping
    public ResponseEntity<TweetResponseDTO> createTweet(@RequestBody TweetRequestDTO tweetRequestDTO){
        log.info("Creating new tweet");
        return new ResponseEntity<>(tweetService.createTweet(tweetRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetResponseDTO> findTweetById(@PathVariable Long id){
        log.info("Fetching tweet with id: {}", id);
        return ResponseEntity.ok(tweetService.findTweetById(id));
    }

    @GetMapping("/findByUserId")
    public ResponseEntity<List<TweetResponseDTO>> findTweetsByUserId(@RequestParam Long userId){
        log.info("Fetching tweets for user: {}", userId);
        return ResponseEntity.ok(tweetService.findTweetsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetResponseDTO> updateTweet(
            @PathVariable Long id,
            @RequestBody TweetRequestDTO tweetRequestDTO){
        log.info("Updating tweet with id: {}", id);
        return ResponseEntity.ok(tweetService.updateTweet(id, tweetRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id){
        log.info("Deleting tweet with id: {}", id);
        tweetService.deleteTweet(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tweetId}/comments")
    public ResponseEntity<CommentResponseDTO> addComment(@PathVariable Long tweetId, @RequestBody CommentRequestDTO commentRequestDTO){
        log.info("Adding comment to tweet: {}", tweetId);
        commentRequestDTO.setTweetId(tweetId);
        return new ResponseEntity<>(commentService.createComment(commentRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{tweetId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getTweetComments(@PathVariable Long tweetId){
        log.info("Fetching comments for tweet: {}", tweetId);
        return ResponseEntity.ok(commentService.findCommentsByTweetId(tweetId));
    }

    @PostMapping("/{tweetId}/like")
    public ResponseEntity<Void>likeTweet(@PathVariable Long tweetId){
        log.info("Liking tweet: {}", tweetId);
        likeService.likeTweet(tweetId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{tweetId}/like")
    public ResponseEntity<Void>unlikeTweet(@PathVariable Long tweetId){
        log.info("Unliking tweet: {}", tweetId);
        likeService.unlikeTweet(tweetId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{tweetId}/likes")
    public ResponseEntity<List<UserResponseDTO>> getTweetLikes(@PathVariable Long tweetId) {
        log.info("Fetching likes for tweet: {}", tweetId);
        return ResponseEntity.ok(likeService.getLikesByTweetId(tweetId));
    }

    @PostMapping("/{tweetId}/retweet")
    public ResponseEntity<RetweetResponseDTO> retweet(@PathVariable Long tweetId, @RequestBody(required = false) RetweetRequestDTO retweetRequestDTO) {
        log.info("Retweeting tweet: {}", tweetId);
        if (retweetRequestDTO == null) {
            retweetRequestDTO = new RetweetRequestDTO(tweetId, null);
        } else {
            retweetRequestDTO.setOriginalTweetId(tweetId);
        }
        return new ResponseEntity<>(retweetService.createRetweet(retweetRequestDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{tweetId}/retweet")
    public ResponseEntity<Void>undoRetweet(@PathVariable Long tweetId) {
        log.info("Removing retweet: {}", tweetId);
        retweetService.deleteRetweet(tweetId);
        return ResponseEntity.noContent().build();
    }



}


































