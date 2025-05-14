package com.example.twitter.service;

import com.example.twitter.dto.UserRequestDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.entity.User;
import com.example.twitter.exception.ResourceAlreadyExistsException;
import com.example.twitter.exception.ResourceNotFoundException;
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
        if(userRepository.existsByUsername(userRequestDTO.getUsername())){
            throw new ResourceAlreadyExistsException("Username is already taken");
        }

        if(userRepository.existsByEmail(userRequestDTO.getEmail())){
            throw new ResourceAlreadyExistsException("Email is already used");
        }

        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        User savedUser = userRepository.save(user);
        return convertUserToUserResponse(savedUser);
    }

    @Override
    public UserResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertUserToUserResponse(user);
    }

    @Override
    public UserResponseDTO findUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertUserToUserResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(!user.getUsername().equals(userRequestDTO.getUsername()) && userRepository.existsByUsername(userRequestDTO.getUsername())){
            throw new ResourceAlreadyExistsException("Username is already taken");
        }

        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        if(userRequestDTO.getPassword() != null){
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return convertUserToUserResponse(updatedUser);
    }

    private UserResponseDTO convertUserToUserResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
