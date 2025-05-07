package com.example.twitter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "retweets", schema = "twitter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Retweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "original_tweet_id")
    private Tweet originalTweet;

    private LocalDateTime retweetedAt;

    private String additionalContent;

    @PrePersist
    protected void onCreate() {
        retweetedAt = LocalDateTime.now();
    }
}
