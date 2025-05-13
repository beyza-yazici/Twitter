package com.example.twitter.service;

import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.entity.Like;
import com.example.twitter.entity.Tweet;
import com.example.twitter.entity.User;
import com.example.twitter.exception.ResourceAlreadyExistsException;
import com.example.twitter.repository.LikeRepository;
import com.example.twitter.repository.TweetRepository;
import com.example.twitter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServiceTests {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private LikeServiceImpl likeService;

    @Test
    void whenLikeTweet_thenSaveLike() {

        Long tweetId = 1L;
        UserResponseDTO currentUser = new UserResponseDTO();
        currentUser.setId(1L);

        Tweet tweet = new Tweet();
        tweet.setId(tweetId);

        User user = new User();
        user.setId(currentUser.getId());

        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(tweetRepository.findById(tweetId)).thenReturn(Optional.of(tweet));
        when(likeRepository.existsByUserIdAndTweetId(currentUser.getId(), tweetId))
                .thenReturn(false);
        when(userRepository.getReferenceById(currentUser.getId())).thenReturn(user);

        likeService.likeTweet(tweetId);

        verify(likeRepository).save(any(Like.class));
    }

    @Test
    void whenLikeAlreadyExists_thenThrowException() {

        Long tweetId = 1L;
        UserResponseDTO currentUser = new UserResponseDTO();
        currentUser.setId(1L);

        Tweet tweet = new Tweet();
        tweet.setId(1L);

        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(tweetRepository.findById(tweetId)).thenReturn(Optional.of(tweet));
        when(likeRepository.existsByUserIdAndTweetId(currentUser.getId(), tweetId))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            likeService.likeTweet(tweetId);
        });
    }
}
