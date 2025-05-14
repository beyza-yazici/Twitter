package com.example.twitter.controller;

import com.example.twitter.dto.AuthResponseDTO;
import com.example.twitter.dto.LoginRequestDTO;
import com.example.twitter.dto.UserRequestDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.service.AuthService;
import com.example.twitter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO userRequestDTO){
        log.info("Registration request received for username: {}", userRequestDTO.getUsername());
        return new ResponseEntity<>(userService.registerUser(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletRequest request
    ){
        log.info("Login request received for username: {}", loginRequestDTO.getUsername());
        return ResponseEntity.ok(authService.login(loginRequestDTO, request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){
        log.info("Logout request received");
        SecurityContextHolder.clearContext();;
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserResponseDTO> getCurrentUser(){
        log.info("Fetching current user details");
        return ResponseEntity.ok(authService.getCurrentUser());
    }
}
