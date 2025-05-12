package com.example.twitter.service;

import com.example.twitter.dto.CommentRequestDTO;
import com.example.twitter.dto.CommentResponseDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.entity.Comment;
import com.example.twitter.entity.Tweet;
import com.example.twitter.repository.CommentRepository;
import com.example.twitter.repository.TweetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTests {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private AuthService authService;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void whenCreateComment_thenReturnCommentResponseDTO() {

        CommentRequestDTO request = new CommentRequestDTO();
        request.setTweetId(1L);
        request.setContent("Test comment");

        UserResponseDTO currentUser = new UserResponseDTO();
        currentUser.setId(1L);
        currentUser.setUsername("testuser");

        Tweet tweet = new Tweet();
        tweet.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent(request.getContent());
        comment.setTweet(tweet);
        comment.setCreatedAt(LocalDateTime.now());

        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(tweetRepository.findById(request.getTweetId())).thenReturn(Optional.of(tweet));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDTO response = commentService.createComment(request);

        assertNotNull(response);
        assertEquals(request.getContent(), response.getContent());
        assertEquals(request.getTweetId(), response.getTweetId());

        verify(commentRepository).save(any(Comment.class));
    }
}