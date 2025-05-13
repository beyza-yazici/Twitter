package com.example.twitter.service;

import com.example.twitter.dto.TweetRequestDTO;
import com.example.twitter.dto.TweetResponseDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.entity.Tweet;
import com.example.twitter.entity.User;
import com.example.twitter.repository.TweetRepository;
import com.example.twitter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TweetServiceTests {
    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TweetServiceImpl tweetService;

    @Test
    void whenCreateTweet_thenReturnTweetResponseDTO() {

        TweetRequestDTO request = new TweetRequestDTO();
        request.setContent("Test tweet");

        UserResponseDTO currentUser = new UserResponseDTO();
        currentUser.setId(1L);
        currentUser.setUsername("testuser");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Tweet tweet = new Tweet();
        tweet.setId(1L);
        tweet.setContent(request.getContent());
        tweet.setUser(user);
        tweet.setCreatedAt(LocalDateTime.now());

        tweet.setComments(new ArrayList<>());
        tweet.setLikes(new ArrayList<>());
        tweet.setRetweets(new ArrayList<>());

        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.findById(currentUser.getId())).thenReturn(Optional.of(user));
        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweet);

        TweetResponseDTO response = tweetService.createTweet(request);

        assertNotNull(response);
        assertEquals(request.getContent(), response.getContent());
        assertEquals(currentUser.getUsername(), response.getUser().getUsername());

        verify(tweetRepository).save(any(Tweet.class));
    }

    @Test
    void whenGetTweetById_thenReturnTweetResponseDTO() {

        Long tweetId = 1L;
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Tweet tweet = new Tweet();
        tweet.setId(tweetId);
        tweet.setContent("Test tweet");
        tweet.setUser(user);
        tweet.setCreatedAt(LocalDateTime.now());

        tweet.setComments(new ArrayList<>());
        tweet.setLikes(new ArrayList<>());
        tweet.setRetweets(new ArrayList<>());

        when(tweetRepository.findById(tweetId)).thenReturn(Optional.of(tweet));

        TweetResponseDTO response = tweetService.findTweetById(tweetId);

        assertNotNull(response);
        assertEquals(tweet.getContent(), response.getContent());
        assertEquals(user.getUsername(), response.getUser().getUsername());

        verify(tweetRepository).findById(tweetId);
    }


}
