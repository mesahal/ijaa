package com.ijaa.user.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.request.SignInRequest;
import com.ijaa.user.domain.request.SignUpRequest;
import com.ijaa.user.service.AuthService;
import com.ijaa.user.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthResource.class)
class AuthResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JWTService jwtService;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setPassword("password123");

        signInRequest = new SignInRequest();
        signInRequest.setUsername("testuser");
        signInRequest.setPassword("password123");
    }

    @Test
    void shouldSignUpUserWhenValidRequestProvided() throws Exception {
        // Given
        when(authService.registerUser(any(SignUpRequest.class)))
                .thenReturn(new com.ijaa.user.domain.response.AuthResponse("test-jwt-token", "USER_123456"));

        // When & Then
        mockMvc.perform(post("/api/v1/user/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("test-jwt-token"))
                .andExpect(jsonPath("$.data.userId").value("USER_123456"));
    }

    @Test
    void shouldReturnBadRequestWhenSignUpRequestIsInvalid() throws Exception {
        // Given
        SignUpRequest invalidRequest = new SignUpRequest();
        invalidRequest.setUsername(""); // Invalid username
        invalidRequest.setPassword(""); // Invalid password

        // When & Then
        mockMvc.perform(post("/api/v1/user/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSignInUserWhenValidCredentialsProvided() throws Exception {
        // Given
        when(authService.verify(any(SignInRequest.class)))
                .thenReturn(new com.ijaa.user.domain.response.AuthResponse("test-jwt-token", "USER_123456"));

        // When & Then
        mockMvc.perform(post("/api/v1/user/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("test-jwt-token"))
                .andExpect(jsonPath("$.data.userId").value("USER_123456"));
    }

    @Test
    void shouldReturnBadRequestWhenSignInRequestIsInvalid() throws Exception {
        // Given
        SignInRequest invalidRequest = new SignInRequest();
        invalidRequest.setUsername(""); // Invalid username
        invalidRequest.setPassword(""); // Invalid password

        // When & Then
        mockMvc.perform(post("/api/v1/user/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnauthorizedWhenInvalidCredentialsProvided() throws Exception {
        // Given
        when(authService.verify(any(SignInRequest.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // When & Then
        mockMvc.perform(post("/api/v1/user/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnConflictWhenUsernameAlreadyExists() throws Exception {
        // Given
        when(authService.registerUser(any(SignUpRequest.class)))
                .thenThrow(new RuntimeException("Username already exists"));

        // When & Then
        mockMvc.perform(post("/api/v1/user/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnBadRequestWhenRequestBodyIsEmpty() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/user/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/user/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenContentTypeIsNotJson() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/user/auth/signup")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("invalid content"))
                .andExpect(status().isUnsupportedMediaType());

        mockMvc.perform(post("/api/v1/user/auth/signin")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("invalid content"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturnBadRequestWhenJsonIsMalformed() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/user/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/api/v1/user/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }
}
