package com.example.twitter.service;

import com.example.twitter.dto.RetweetRequestDTO;
import com.example.twitter.dto.RetweetResponseDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.entity.Retweet;
import com.example.twitter.entity.Tweet;
import com.example.twitter.entity.User;
import com.example.twitter.exception.ResourceAlreadyExistsException;
import com.example.twitter.repository.RetweetRepository;
import com.example.twitter.repository.TweetRepository;
import com.example.twitter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetweetServiceTests {

    @Mock
    private RetweetRepository retweetRepository;

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private RetweetServiceImpl retweetService;

    @Test
    void whenCreateRetweet_thenReturnRetweetResponseDTO() {

        RetweetRequestDTO request = new RetweetRequestDTO();
        request.setOriginalTweetId(1L);
        request.setAdditionalContent("Test retweet content");

        UserResponseDTO currentUser = new UserResponseDTO();
        currentUser.setId(1L);
        currentUser.setUsername("testuser");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Tweet originalTweet = new Tweet();
        originalTweet.setId(1L);
        originalTweet.setContent("Original tweet content");

        Retweet retweet = new Retweet();
        retweet.setId(1L);
        retweet.setUser(user);
        retweet.setOriginalTweet(originalTweet);
        retweet.setAdditionalContent(request.getAdditionalContent());
        retweet.setRetweetedAt(LocalDateTime.now());

        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(tweetRepository.findById(request.getOriginalTweetId())).thenReturn(Optional.of(originalTweet));
        when(userRepository.findById(currentUser.getId())).thenReturn(Optional.of(user));
        when(retweetRepository.save(any(Retweet.class))).thenReturn(retweet);

        RetweetResponseDTO response = retweetService.createRetweet(request);

        assertNotNull(response);
        assertEquals(request.getAdditionalContent(), response.getAdditionalContent());
        assertEquals(currentUser.getUsername(), response.getUser().getUsername());
        assertEquals(originalTweet.getId(), response.getOriginalTweet().getId());

        verify(retweetRepository).save(any(Retweet.class));
    }

    @Test
    void whenCreateRetweetWithExistingRetweet_thenThrowException() {

        RetweetRequestDTO request = new RetweetRequestDTO();
        request.setOriginalTweetId(1L);

        UserResponseDTO currentUser = new UserResponseDTO();
        currentUser.setId(1L);

        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(retweetRepository.existsByUserIdAndOriginalTweetId(currentUser.getId(), request.getOriginalTweetId()))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            retweetService.createRetweet(request);
        });
    }

    @Test
    void whenDeleteRetweet_thenSuccess() {
        // Arrange
        Long retweetId = 1L;
        UserResponseDTO currentUser = new UserResponseDTO();
        currentUser.setId(1L);

        Retweet retweet = new Retweet();
        retweet.setId(retweetId);
        User user = new User();
        user.setId(currentUser.getId());
        retweet.setUser(user);

        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(retweetRepository.findById(retweetId)).thenReturn(Optional.of(retweet));

        retweetService.deleteRetweet(retweetId);

        verify(retweetRepository).delete(retweet);
    }

}
