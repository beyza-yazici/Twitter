package com.example.twitter.repository;

import com.example.twitter.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetweetRepository extends JpaRepository<Retweet, Long> {

    List<Retweet> findByUserId(Long userId);
    List<Retweet> findByOriginalTweetId(Long tweetId);
    boolean existsByUserIdAndOriginalTweetId(Long userId, Long tweetId);
    void deleteByUserIdAndOriginalTweetId(Long userId, Long tweetId);
}