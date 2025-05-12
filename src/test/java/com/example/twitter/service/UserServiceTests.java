package com.example.twitter.service;

import com.example.twitter.dto.UserRequestDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.entity.User;
import com.example.twitter.exception.ResourceAlreadyExistsException;
import com.example.twitter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void whenRegisterUser_thenReturnUserResponseDTO() {

        UserRequestDTO request = new UserRequestDTO();
        request.setUsername("testuser");
        request.setEmail("test@test.com");
        request.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword("encodedPassword");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals(request.getUsername(), response.getUsername());
        assertEquals(request.getEmail(), response.getEmail());

        verify(userRepository).existsByUsername(request.getUsername());
        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void whenRegisterUserWithExistingUsername_thenThrowException() {

        UserRequestDTO request = new UserRequestDTO();
        request.setUsername("existinguser");
        request.setEmail("test@test.com");

        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.registerUser(request);
        });
    }
}
