package com.example.twitter.service;

import com.example.twitter.dto.TweetRequestDTO;
import com.example.twitter.dto.TweetResponseDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.entity.Tweet;
import com.example.twitter.entity.User;
import com.example.twitter.exception.ResourceNotFoundException;
import com.example.twitter.repository.TweetRepository;
import com.example.twitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public TweetResponseDTO createTweet(TweetRequestDTO tweetRequestDTO) {
        UserResponseDTO currentUser = authService.getCurrentUser();
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tweet tweet = new Tweet();
        tweet.setContent(tweetRequestDTO.getContent());
        tweet.setUser(user);

        Tweet savedTweet = tweetRepository.save(tweet);
        return convertTweetToTweetResponse(savedTweet);
    }

    @Override
    public List<TweetResponseDTO> findTweetsByUserId(Long userId) {
        return tweetRepository.findByUserId(userId).stream().map(this::convertTweetToTweetResponse).collect(Collectors.toList());
    }

    @Override
    public TweetResponseDTO findTweetById(Long id) {
        Tweet tweet = tweetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));
        return convertTweetToTweetResponse(tweet);
    }

    @Override
    public TweetResponseDTO updateTweet(Long id, TweetRequestDTO tweetRequestDTO) {
        UserResponseDTO currentUser = authService.getCurrentUser();
        Tweet tweet = tweetRepository.findByIdAndUserId(id, currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        tweet.setContent(tweetRequestDTO.getContent());
        Tweet updatedTweet = tweetRepository.save(tweet);
        return convertTweetToTweetResponse(updatedTweet);
    }

    @Override
    public void deleteTweet(Long id) {
        UserResponseDTO currentUser = authService.getCurrentUser();
        Tweet tweet = tweetRepository.findByIdAndUserId(id, currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        tweetRepository.delete(tweet);

    }

    private TweetResponseDTO convertTweetToTweetResponse(Tweet tweet) {
        return new TweetResponseDTO(
                tweet.getId(),
                tweet.getContent(),
                convertUserToUserResponse(tweet.getUser()),
                tweet.getCreatedAt(),
                tweet.getComments().size(),
                tweet.getLikes().size(),
                tweet.getRetweets().size(),
                false,
                false
        );
    }

    private UserResponseDTO convertUserToUserResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                LocalDateTime.now()
        );
    }

}
