package com.example.twitter.service;

import com.example.twitter.dto.*;
import com.example.twitter.entity.Comment;
import com.example.twitter.entity.Retweet;
import com.example.twitter.entity.Tweet;
import com.example.twitter.entity.User;
import com.example.twitter.exception.ResourceAlreadyExistsException;
import com.example.twitter.exception.ResourceNotFoundException;
import com.example.twitter.exception.UnauthorizedException;
import com.example.twitter.repository.RetweetRepository;
import com.example.twitter.repository.TweetRepository;
import com.example.twitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetweetServiceImpl implements RetweetService{

    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public RetweetResponseDTO createRetweet(RetweetRequestDTO retweetRequestDTO) {
        UserResponseDTO currentUser = authService.getCurrentUser();

        Tweet originalTweet = tweetRepository.findById(retweetRequestDTO.getOriginalTweetId()).orElseThrow(() -> new ResourceNotFoundException("Original tweet not found"));

        if(retweetRepository.existsByUserIdAndOriginalTweetId(currentUser.getId(),retweetRequestDTO.getOriginalTweetId())){
            throw new ResourceAlreadyExistsException("You have already retweeted this tweet");
        }

        Retweet retweet = new Retweet();
        retweet.setUser(userRepository.getReferenceById(currentUser.getId()));
        retweet.setOriginalTweet(originalTweet);
        retweet.setAdditionalContent(retweetRequestDTO.getAdditionalContent());

        Retweet savedRetweet = retweetRepository.save(retweet);
        return convertRetweetToRetweetResponse(savedRetweet);

    }

    @Override
    public void deleteRetweet(Long id) {

        UserResponseDTO currentUser = authService.getCurrentUser();

        Retweet retweet = retweetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Retweet not found"));

        if(!retweet.getUser().getId().equals(currentUser.getId())){
            throw new UnauthorizedException("You don't have permission to delete this retweet");
        }

        retweetRepository.delete(retweet);

    }

    private RetweetResponseDTO convertRetweetToRetweetResponse(Retweet retweet) {
        return new RetweetResponseDTO(
                retweet.getId(),
                convertUserToUserResponse(retweet.getUser()),
                convertTweetToTweetResponse(retweet.getOriginalTweet()),
                retweet.getAdditionalContent(),
                retweet.getRetweetedAt()
        );
    }

    private TweetResponseDTO convertTweetToTweetResponse(Tweet tweet) {

        if (tweet == null) {
            return null;
        }

        UserResponseDTO userResponseDTO = tweet.getUser() != null ?
                convertUserToUserResponse(tweet.getUser()) : null;

        List<CommentResponseDTO> comments = new ArrayList<>();
        int commentCount = 0;
        int likeCount = 0;
        int retweetCount = 0;

        if (tweet.getComments() != null) {
            comments = tweet.getComments().stream()
                    .map(this::convertCommentToCommentResponse)
                    .collect(Collectors.toList());
            commentCount = comments.size();
        }

        if (tweet.getLikes() != null) {
            likeCount = tweet.getLikes().size();
        }

        if (tweet.getRetweets() != null) {
            retweetCount = tweet.getRetweets().size();
        }

        return new TweetResponseDTO(
                tweet.getId(),
                tweet.getContent(),
                userResponseDTO,
                comments,
                tweet.getCreatedAt(),
                likeCount,
                commentCount,
                retweetCount,
                false,
                false
        );
    }

    private UserResponseDTO convertUserToUserResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

    private CommentResponseDTO convertCommentToCommentResponse(Comment comment) {
        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                convertUserToUserResponse(comment.getUser()),
                comment.getTweet().getId(),
                comment.getCreatedAt()
        );
    }
}
