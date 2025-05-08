package com.example.twitter.service;

import com.example.twitter.dto.TweetRequestDTO;
import com.example.twitter.dto.TweetResponseDTO;

import java.util.List;

public interface TweetService {

    TweetResponseDTO createTweet(TweetRequestDTO tweetRequestDTO);
    List<TweetResponseDTO> findTweetsByUserId(Long userId);
    TweetResponseDTO findTweetById(Long id);
    TweetResponseDTO updateTweet(Long id, TweetRequestDTO tweetRequestDTO);
    void deleteTweet(Long id);
}
