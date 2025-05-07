package com.example.twitter.repository;

import com.example.twitter.entity.Comment;
import com.example.twitter.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserIdAndTweetId(Long userId, Long tweetId);
    List<Like> findByTweetId(Long tweetId);
    List<Like> findByUserId(Long userId);
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
    void deleteByUserIdAndTweetId(Long userId, Long tweetId);
}
