package com.example.twitter.controller;

import com.example.twitter.dto.RetweetRequestDTO;
import com.example.twitter.dto.RetweetResponseDTO;
import com.example.twitter.service.RetweetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/retweets")
@RequiredArgsConstructor
@Slf4j
public class RetweetController {

    private final RetweetService retweetService;

    @PostMapping("/tweet/{tweetId}")
    public ResponseEntity<RetweetResponseDTO> createRetweet(@PathVariable Long tweetId, @RequestBody(required = false) RetweetRequestDTO retweetRequestDTO) {
        log.info("Retweeting tweet: {}", tweetId);

        if (retweetRequestDTO == null) {
            retweetRequestDTO = new RetweetRequestDTO(tweetId, null);
        } else {
            retweetRequestDTO.setOriginalTweetId(tweetId);
        }

        return new ResponseEntity<>(retweetService.createRetweet(retweetRequestDTO),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{retweetId}")
    public ResponseEntity<Void> deleteRetweet(@PathVariable Long retweetId) {
        log.info("Deleting retweet: {}", retweetId);
        retweetService.deleteRetweet(retweetId);
        return ResponseEntity.noContent().build();
    }
}
