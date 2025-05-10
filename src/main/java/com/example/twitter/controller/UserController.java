package com.example.twitter.controller;

import com.example.twitter.dto.TweetResponseDTO;
import com.example.twitter.dto.UserRequestDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.service.AuthService;
import com.example.twitter.service.TweetService;
import com.example.twitter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final TweetService tweetService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO userRequestDTO){
        log.info("Registering new user with username: {}", userRequestDTO.getUsername());
        return new ResponseEntity<>(userService.registerUser(userRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        log.info("Fetching current user profile");
        return ResponseEntity.ok(authService.getCurrentUser());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long userId){
        log.info("Finding user with Id: {}", userId);
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO>findUserByUsername(@PathVariable String username){
        log.info("Finding user with username: {}", username);
        return ResponseEntity.ok(userService.findUserByUsername(username));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDTO userRequestDTO){
        log.info("Updating user profile");
        UserResponseDTO currentUser = authService.getCurrentUser();
        return ResponseEntity.ok(userService.updateUser(currentUser.getId(), userRequestDTO));
    }

    @DeleteMapping("/profile")
    public ResponseEntity<Void> deleteUser(){
        log.info("Deleting current user profile");
        UserResponseDTO currentUser = authService.getCurrentUser();
        userService.deleteUser(currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/tweets")
    public ResponseEntity<List<TweetResponseDTO>> getUserTweets(@PathVariable Long userId){
        log.info("Fetching tweets for user with ID: {}", userId);
        return ResponseEntity.ok(tweetService.findTweetsByUserId(userId));
    }

}























