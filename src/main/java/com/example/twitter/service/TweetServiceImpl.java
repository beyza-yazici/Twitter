package com.example.twitter.service;

import com.example.twitter.dto.CommentResponseDTO;
import com.example.twitter.dto.TweetRequestDTO;
import com.example.twitter.dto.TweetResponseDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.entity.Comment;
import com.example.twitter.entity.Tweet;
import com.example.twitter.entity.User;
import com.example.twitter.exception.BadRequestException;
import com.example.twitter.exception.ResourceNotFoundException;
import com.example.twitter.exception.UnauthorizedException;
import com.example.twitter.repository.TweetRepository;
import com.example.twitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
                throw new UnauthorizedException("Please login first");
            }

            UserResponseDTO currentUser = authService.getCurrentUser();
            User user = userRepository.findById(currentUser.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            Tweet tweet = new Tweet();
            tweet.setContent(tweetRequestDTO.getContent());
            tweet.setUser(user);
            tweet.setCreatedAt(LocalDateTime.now());

            tweet.setComments(new ArrayList<>());

            Tweet savedTweet = tweetRepository.save(tweet);
            return convertTweetToTweetResponse(savedTweet);
        } catch (UnauthorizedException e) {
            throw e;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Error creating tweet: " + e.getMessage());
        }
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

        if (tweet == null) {
            return null;
        }

        UserResponseDTO userResponseDTO = tweet.getUser() != null ?
                convertUserToUserResponse(tweet.getUser()) : null;

        int commentCount = tweet.getComments() != null ? tweet.getComments().size() : 0;
        int likeCount = tweet.getLikes() != null ? tweet.getLikes().size() : 0;
        int retweetCount = tweet.getRetweets() != null ? tweet.getRetweets().size() : 0;

        List<CommentResponseDTO> commentResponseDTOs = new ArrayList<>();
        if (tweet.getComments() != null) {
            commentResponseDTOs = tweet.getComments().stream()
                    .map(this::convertCommentToCommentResponse)
                    .collect(Collectors.toList());
        }

        return new TweetResponseDTO(
                tweet.getId(),
                tweet.getContent(),
                userResponseDTO,
                commentResponseDTOs,
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
