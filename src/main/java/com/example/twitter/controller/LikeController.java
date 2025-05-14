package com.example.twitter.controller;

import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;

    /*@PostMapping("/tweet/{tweetId}")
    public ResponseEntity<Void> likeTweet(@PathVariable Long tweetId) {
        log.info("Liking tweet: {}", tweetId);
        likeService.likeTweet(tweetId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tweet/{tweetId}")
    public ResponseEntity<Void> unlikeTweet(@PathVariable Long tweetId) {
        log.info("Unliking tweet: {}", tweetId);
        likeService.unlikeTweet(tweetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tweet/{tweetId}")
    public ResponseEntity<List<UserResponseDTO>> getTweetLikes(@PathVariable Long tweetId) {
        log.info("Fetching likes for tweet: {}", tweetId);
        return ResponseEntity.ok(likeService.getLikesByTweetId(tweetId));
    }*/

    @GetMapping("/tweet/{tweetId}/count")
    public ResponseEntity<Integer> getTweetLikeCount(@PathVariable Long tweetId) {
        log.info("Fetching like count for tweet: {}", tweetId);
        return ResponseEntity.ok(likeService.getLikeCountForTweet(tweetId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Long>> getUserLikedTweets(@PathVariable Long userId) {
        log.info("Fetching liked tweets for user: {}", userId);
        return ResponseEntity.ok(likeService.getUserLikedTweetIds(userId));
    }

    @GetMapping("/tweet/{tweetId}/check")
    public ResponseEntity<Boolean> checkIfUserLikedTweet(@PathVariable Long tweetId) {
        log.info("Checking if current user liked tweet: {}", tweetId);
        return ResponseEntity.ok(likeService.hasUserLikedTweet(tweetId));
    }
}































