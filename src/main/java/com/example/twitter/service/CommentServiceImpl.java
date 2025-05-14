package com.example.twitter.service;

import com.example.twitter.dto.CommentRequestDTO;
import com.example.twitter.dto.CommentResponseDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.entity.Comment;
import com.example.twitter.entity.Tweet;
import com.example.twitter.entity.User;
import com.example.twitter.exception.ResourceNotFoundException;
import com.example.twitter.exception.UnauthorizedException;
import com.example.twitter.repository.CommentRepository;
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
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Override
    public CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO) {
        UserResponseDTO currentUser = authService.getCurrentUser();
        Tweet tweet = tweetRepository.findById(commentRequestDTO.getTweetId()).orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));

        Comment comment = new Comment();
        comment.setContent(commentRequestDTO.getContent());
        comment.setTweet(tweet);
        comment.setUser(userRepository.getReferenceById(currentUser.getId()));

        Comment savedComment = commentRepository.save(comment);
        return convertCommentToCommentResponse(savedComment);
    }

    @Override
    public CommentResponseDTO updateComment(Long id, CommentRequestDTO commentRequestDTO) {
        UserResponseDTO currentUser = authService.getCurrentUser();
        Comment comment = commentRepository.findByIdAndUserId(id, currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        comment.setContent(commentRequestDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return convertCommentToCommentResponse(updatedComment);
    }

    @Override
    public void deleteComment(Long id) {
        UserResponseDTO currentUser = authService.getCurrentUser();
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if(!comment.getUser().getId().equals(currentUser.getId()) && !comment.getTweet().getUser().getId().equals(currentUser.getId())){
            throw new UnauthorizedException("You don't have permission to delete this comment");
        }
        commentRepository.delete(comment);
    }

    @Override
    public List<CommentResponseDTO> findCommentsByTweetId(Long tweetId) {
        return commentRepository.findByTweetId(tweetId).stream().map(this::convertCommentToCommentResponse).collect(Collectors.toList());
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

    private UserResponseDTO convertUserToUserResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }

}
