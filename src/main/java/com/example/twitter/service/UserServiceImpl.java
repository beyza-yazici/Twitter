package com.example.twitter.service;

import com.example.twitter.dto.UserRequestDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        return null;
    }

    @Override
    public UserResponseDTO findUserById(Long id) {
        return null;
    }

    @Override
    public UserResponseDTO findUserByUsername(String username) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        return null;
    }
}
