package com.example.twitter.service;

import com.example.twitter.dto.UserResponseDTO;

import java.util.List;

public interface LikeService {

    void likeTweet(Long tweetId);
    void unlikeTweet(Long tweetId);
    List<Long> getUserLikedTweetIds(Long userId);
    int getLikeCountForTweet(Long tweetId);
    boolean hasUserLikedTweet(Long tweetId);
    List<UserResponseDTO> getLikesByTweetId(Long tweetId);
}
