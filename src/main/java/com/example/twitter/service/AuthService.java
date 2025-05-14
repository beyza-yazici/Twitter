package com.example.twitter.service;

import com.example.twitter.dto.AuthResponseDTO;
import com.example.twitter.dto.LoginRequestDTO;
import com.example.twitter.dto.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    AuthResponseDTO login(LoginRequestDTO loginRequestDTO, HttpServletRequest request);

    UserResponseDTO getCurrentUser();
}
