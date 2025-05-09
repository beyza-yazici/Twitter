package com.example.twitter.service;

import com.example.twitter.dto.RetweetRequestDTO;
import com.example.twitter.dto.RetweetResponseDTO;
import com.example.twitter.dto.TweetResponseDTO;
import com.example.twitter.dto.UserResponseDTO;
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
