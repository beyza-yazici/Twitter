package com.example.twitter.service;

import com.example.twitter.dto.UserRequestDTO;
import com.example.twitter.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO registerUser(UserRequestDTO userRequest);
    UserResponseDTO findUserById(Long id);
    UserResponseDTO findUserByUsername(String username);
    void deleteUser(Long id);
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequest);
}
