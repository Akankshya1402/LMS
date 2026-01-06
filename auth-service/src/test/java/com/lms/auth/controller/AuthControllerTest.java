package com.lms.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lms.auth.dto.ForgotPasswordRequest;
import com.lms.auth.dto.LoginRequest;
import com.lms.auth.dto.RegisterRequest;
import com.lms.auth.exception.GlobalExceptionHandler;
import com.lms.auth.service.AuthService;
import com.lms.auth.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔥 disable security
@Import(GlobalExceptionHandler.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("user1");
        request.setPassword("password123");
        request.setEmail("user1@test.com"); // 🔥 REQUIRED

        doNothing().when(authService).register(any());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("User registered successfully"));
    }


    @Test
    void shouldLoginSuccessfully() throws Exception {

        LoginRequest request = new LoginRequest();
        request.setUsername("user1");
        request.setPassword("password123");

        when(authService.login(any())).thenReturn("jwt-token");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void shouldResetPasswordSuccessfully() throws Exception {

        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setUsername("user1");
        request.setNewPassword("newPassword123");

        doNothing().when(authService).forgotPassword(any(), any());

        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Password updated successfully"));
    }
}
