package com.lms.auth.service;

import com.lms.auth.dto.LoginRequest;
import com.lms.auth.dto.RegisterRequest;
import com.lms.auth.exception.InvalidCredentialsException;
import com.lms.auth.model.Role;
import com.lms.auth.model.User;
import com.lms.auth.repository.UserRepository;
import com.lms.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .id("1")
                .username("user1")
                .password("encodedPassword")
                .roles(Set.of(Role.CUSTOMER))
                .enabled(true)
                .build();
    }

    // =========================
    // REGISTER TEST
    // =========================
    @Test
    void shouldRegisterUserSuccessfully() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("user1");
        request.setPassword("password123");

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertDoesNotThrow(() -> authService.register(request));

        verify(userRepository).save(any(User.class));
    }

    // =========================
    // LOGIN TESTS
    // =========================
    @Test
    void shouldLoginSuccessfullyAndReturnToken() {

        LoginRequest request = new LoginRequest();
        request.setUsername("user1");
        request.setPassword("password123");

        when(userRepository.findByUsername("user1"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any()))
                .thenReturn(true);
        when(jwtUtil.generateToken(any(), any()))
                .thenReturn("jwt-token");

        String token = authService.login(request);

        assertEquals("jwt-token", token);
    }

    @Test
    void shouldThrowExceptionForWrongPassword() {

        LoginRequest request = new LoginRequest();
        request.setUsername("user1");
        request.setPassword("wrong");

        when(userRepository.findByUsername("user1"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        InvalidCredentialsException ex =
                assertThrows(InvalidCredentialsException.class,
                        () -> authService.login(request));

        assertEquals("Invalid credentials", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        LoginRequest request = new LoginRequest();
        request.setUsername("unknown");
        request.setPassword("password");

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        InvalidCredentialsException ex =
                assertThrows(InvalidCredentialsException.class,
                        () -> authService.login(request));

        assertEquals("User not found", ex.getMessage());
    }
}

