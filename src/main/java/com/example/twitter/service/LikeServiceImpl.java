package com.example.twitter.service;

import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.entity.Like;
import com.example.twitter.entity.Tweet;
import com.example.twitter.entity.User;
import com.example.twitter.exception.ResourceAlreadyExistsException;
import com.example.twitter.exception.ResourceNotFoundException;
import com.example.twitter.repository.LikeRepository;
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
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public void likeTweet(Long tweetId) {
        UserResponseDTO currentUser = authService.getCurrentUser();

        Tweet tweet = tweetRepository.findById(tweetId).orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        if(likeRepository.existsByUserIdAndTweetId(currentUser.getId(), tweetId)){
            throw new ResourceAlreadyExistsException("You have already liked the tweet");
        }

        Like like = new Like();
        like.setUser(userRepository.getReferenceById(currentUser.getId()));
        like.setTweet(tweet);

        likeRepository.save(like);

    }

    @Override
    public void unlikeTweet(Long tweetId) {
        UserResponseDTO currentUser = authService.getCurrentUser();

        Like like = likeRepository.findByUserIdAndTweetId(currentUser.getId(), tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("Like not found"));

        likeRepository.delete(like);

    }

    @Override
    public List<Long> getUserLikedTweetIds(Long userId) {
        return likeRepository.findByUserId(userId).stream()
                .map(like -> like.getTweet().getId())
                .collect(Collectors.toList());
    }

    @Override
    public int getLikeCountForTweet(Long tweetId) {
        return likeRepository.findByTweetId(tweetId).size();
    }

    @Override
    public boolean hasUserLikedTweet(Long tweetId) {
        UserResponseDTO currentUser = authService.getCurrentUser();
        return likeRepository.existsByUserIdAndTweetId(currentUser.getId(), tweetId);
    }

    @Override
    public List<UserResponseDTO> getLikesByTweetId(Long tweetId) {
        return likeRepository.findByTweetId(tweetId).stream()
                .map(like -> convertUserToUserResponse(like.getUser()))
                .collect(Collectors.toList());
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
