package com.example.twitter.repository;

import com.example.twitter.entity.Comment;
import com.example.twitter.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTweetId(Long tweetId);
    List<Comment> findByUserId(Long userId);
    Optional<Comment> findByIdAndUserId(Long id, Long userId);
    Optional<Comment> findByIdAndTweetId(Long id, Long tweetId);
}
