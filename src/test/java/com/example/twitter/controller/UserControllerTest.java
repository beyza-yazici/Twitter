package com.example.twitter.controller;

import com.example.twitter.dto.UserRequestDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.dto.TweetResponseDTO;
import com.example.twitter.service.UserService;
import com.example.twitter.service.TweetService;
import com.example.twitter.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @Mock
    private TweetService tweetService;

    @Mock
    private AuthService authService;

    private UserController userController;

    @BeforeEach
    void setup() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        userController = new UserController(userService, tweetService, authService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void whenGetUserById_thenReturn200() throws Exception {

        Long userId = 1L;
        UserResponseDTO response = new UserResponseDTO();
        response.setId(userId);
        response.setUsername("testuser");
        response.setEmail("test@test.com");

        when(userService.findUserById(userId)).thenReturn(response);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andDo(print());
    }

    @Test
    void whenUpdateUser_thenReturn200() throws Exception {

        Long userId = 1L;
        UserRequestDTO request = new UserRequestDTO();
        request.setUsername("updateduser");
        request.setEmail("updated@test.com");

        UserResponseDTO currentUser = new UserResponseDTO();
        currentUser.setId(userId);

        UserResponseDTO response = new UserResponseDTO();
        response.setId(userId);
        response.setUsername(request.getUsername());
        response.setEmail(request.getEmail());

        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(userService.updateUser(eq(userId), any(UserRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(request.getUsername()))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andDo(print());
    }

    @Test
    void whenDeleteUser_thenReturn204() throws Exception {

        UserResponseDTO currentUser = new UserResponseDTO();
        currentUser.setId(1L);

        when(authService.getCurrentUser()).thenReturn(currentUser);
        doNothing().when(userService).deleteUser(currentUser.getId());

        mockMvc.perform(delete("/users/profile"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void whenGetUserTweets_thenReturn200() throws Exception {

        Long userId = 1L;
        List<TweetResponseDTO> tweets = Arrays.asList(
                new TweetResponseDTO(1L, "First tweet", null, new ArrayList<>(), LocalDateTime.now(), 0, 0, 0, false, false),
                new TweetResponseDTO(2L, "Second tweet", null, new ArrayList<>(), LocalDateTime.now(), 0, 0, 0, false, false)
        );

        when(tweetService.findTweetsByUserId(userId)).thenReturn(tweets);

        mockMvc.perform(get("/users/{userId}/tweets", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].content").value("First tweet"))
                .andDo(print());
    }
}
