package com.example.twitter.controller;

import com.example.twitter.dto.TweetRequestDTO;
import com.example.twitter.dto.TweetResponseDTO;
import com.example.twitter.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TweetControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TweetService tweetService;

    @Mock
    private CommentService commentService;

    @Mock
    private LikeService likeService;

    @Mock
    private RetweetService retweetService;

    private TweetController tweetController;

    @BeforeEach
    void setup() {

        tweetController = new TweetController(tweetService, commentService, likeService, retweetService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(tweetController)
                .build();
    }

    @Test
    void whenCreateTweet_thenReturn201() throws Exception {

        TweetRequestDTO request = new TweetRequestDTO();
        request.setContent("Test tweet");

        TweetResponseDTO response = new TweetResponseDTO();
        response.setId(1L);
        response.setContent(request.getContent());

        when(tweetService.createTweet(any(TweetRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/tweet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(request.getContent()))
                .andDo(print());
    }

    @Test
    void whenGetTweetById_thenReturn200() throws Exception {

        Long tweetId = 1L;
        TweetResponseDTO response = new TweetResponseDTO();
        response.setId(tweetId);
        response.setContent("Test tweet");

        when(tweetService.findTweetById(tweetId)).thenReturn(response);

        mockMvc.perform(get("/tweet/{id}", tweetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tweetId))
                .andExpect(jsonPath("$.content").value("Test tweet"))
                .andDo(print());
    }
}