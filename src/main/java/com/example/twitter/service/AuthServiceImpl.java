package com.example.twitter.service;

import com.example.twitter.dto.AuthResponseDTO;
import com.example.twitter.dto.LoginRequestDTO;
import com.example.twitter.dto.UserResponseDTO;
import com.example.twitter.entity.User;
import com.example.twitter.exception.ResourceNotFoundException;
import com.example.twitter.exception.UnauthorizedException;
import com.example.twitter.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO, HttpServletRequest request) {
        User user = userRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        authToken.setDetails(new WebAuthenticationDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        log.info("User logged in successfully: {}, Session ID: {}", user.getUsername(), session.getId());

        return new AuthResponseDTO(
                session.getId(),
                "Session",
                convertUserToUserResponse(user)
        );
    }

    @Override
    public UserResponseDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            log.error("User not authenticated properly. Auth: {}, Name: {}",
                    auth, auth != null ? auth.getName() : "null");
            throw new UnauthorizedException("User not authenticated");
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return convertUserToUserResponse(user);
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
