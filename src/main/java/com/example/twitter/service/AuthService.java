package com.example.twitter.service;

import com.example.twitter.dto.AuthResponseDTO;
import com.example.twitter.dto.LoginRequestDTO;
import com.example.twitter.dto.UserResponseDTO;

public interface AuthService {
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
    UserResponseDTO getCurrentUser();
}
