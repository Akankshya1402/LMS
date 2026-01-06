package com.lms.auth.service;

import com.lms.auth.dto.LoginRequest;
import com.lms.auth.dto.RegisterRequest;
import com.lms.auth.exception.InvalidCredentialsException;
import com.lms.auth.exception.UserAlreadyExistsException;
import com.lms.auth.model.Role;
import com.lms.auth.model.User;
import com.lms.auth.repository.UserRepository;
import com.lms.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // =========================
    // REGISTER
    // =========================
    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(Role.CUSTOMER))
                .enabled(true)
                .build();

        userRepository.save(user);
    }

    // =========================
    // LOGIN
    // =========================
    public String login(LoginRequest request) {

        // ADMIN LOGIN
        if ("admin".equals(request.getUsername())) {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getUsername(),
                                    request.getPassword()
                            )
                    );

            if (!authentication.isAuthenticated()) {
                throw new InvalidCredentialsException("Invalid admin credentials");
            }

            return jwtUtil.generateToken("admin", Set.of("ADMIN"));
        }

        // CUSTOMER LOGIN
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new InvalidCredentialsException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return jwtUtil.generateToken(
                user.getUsername(),
                user.getRoles()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet())
        );
    }
    public void forgotPassword(String username, String newPassword) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new InvalidCredentialsException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
