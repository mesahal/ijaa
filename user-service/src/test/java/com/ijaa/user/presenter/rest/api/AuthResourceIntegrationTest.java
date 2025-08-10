package com.ijaa.user.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.common.exceptions.AuthenticationFailedException;
import com.ijaa.user.common.exceptions.UserAlreadyExistsException;
import com.ijaa.user.domain.request.SignInRequest;
import com.ijaa.user.domain.request.SignUpRequest;
import com.ijaa.user.domain.response.AuthResponse;
import com.ijaa.user.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.context.annotation.Import;
import com.ijaa.user.config.TestConfig;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthResource.class)
@Import(TestConfig.class)
@DisplayName("AuthResource Integration Tests")
class AuthResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private static final String BASE_URL = "/api/v1/user";

    @BeforeEach
    void setUp() {
        reset(authService);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should sign up user successfully with valid request")
    void signUp_Success() throws Exception {
        // Arrange
        SignUpRequest request = TestDataBuilder.createSignUpRequest();
        AuthResponse authResponse = TestDataBuilder.createAuthResponse();
        
        when(authService.registerUser(any(SignUpRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registration successful"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.data.token").value(authResponse.getToken()))
                .andExpect(jsonPath("$.data.userId").value(authResponse.getUserId()));

        verify(authService).registerUser(any(SignUpRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should sign in user successfully with valid credentials")
    void signIn_Success() throws Exception {
        // Arrange
        SignInRequest request = TestDataBuilder.createSignInRequest();
        AuthResponse authResponse = TestDataBuilder.createAuthResponse();
        
        when(authService.verify(any(SignInRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.token").value(authResponse.getToken()))
                .andExpect(jsonPath("$.data.userId").value(authResponse.getUserId()));

        verify(authService).verify(any(SignInRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when username already exists during signup")
    void signUp_UsernameAlreadyExists() throws Exception {
        // Arrange
        SignUpRequest request = TestDataBuilder.createSignUpRequest();
        when(authService.registerUser(any(SignUpRequest.class)))
                .thenThrow(new UserAlreadyExistsException("Username already taken"));

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(authService).registerUser(any(SignUpRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when authentication fails during signin")
    void signIn_AuthenticationFailed() throws Exception {
        // Arrange
        SignInRequest request = TestDataBuilder.createSignInRequest();
        when(authService.verify(any(SignInRequest.class)))
                .thenThrow(new AuthenticationFailedException("Invalid credentials"));

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(authService).verify(any(SignInRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when signup request has invalid data")
    void signUp_InvalidRequest() throws Exception {
        // Arrange
        SignUpRequest request = TestDataBuilder.createInvalidSignUpRequest();

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).registerUser(any(SignUpRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when signin request has invalid data")
    void signIn_InvalidRequest() throws Exception {
        // Arrange
        SignInRequest request = TestDataBuilder.createInvalidSignInRequest();

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).verify(any(SignInRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when signup request is missing required fields")
    void signUp_MissingFields() throws Exception {
        // Arrange
        String invalidJson = "{\"username\":\"testuser\"}"; // Missing password

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(authService, never()).registerUser(any(SignUpRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when signin request is missing required fields")
    void signIn_MissingFields() throws Exception {
        // Arrange
        String invalidJson = "{\"username\":\"testuser\"}"; // Missing password

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(authService, never()).verify(any(SignInRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when signup request has empty username")
    void signUp_EmptyUsername() throws Exception {
        // Arrange
        SignUpRequest request = new SignUpRequest();
        request.setUsername("");
        request.setPassword("password123");

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).registerUser(any(SignUpRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when signup request has too short password")
    void signUp_ShortPassword() throws Exception {
        // Arrange
        SignUpRequest request = new SignUpRequest();
        request.setUsername("testuser");
        request.setPassword("123"); // Too short

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).registerUser(any(SignUpRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when signin request has empty credentials")
    void signIn_EmptyCredentials() throws Exception {
        // Arrange
        SignInRequest request = new SignInRequest();
        request.setUsername("");
        request.setPassword("");

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).verify(any(SignInRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when request body is malformed JSON")
    void signUp_MalformedJson() throws Exception {
        // Arrange
        String malformedJson = "{\"username\":\"testuser\",\"password\":}"; // Invalid JSON

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());

        verify(authService, never()).registerUser(any(SignUpRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when request body is empty")
    void signUp_EmptyBody() throws Exception {
        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

        verify(authService, never()).registerUser(any(SignUpRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 415 when content type is not JSON")
    void signUp_WrongContentType() throws Exception {
        // Arrange
        SignUpRequest request = TestDataBuilder.createSignUpRequest();

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnsupportedMediaType());

        verify(authService, never()).registerUser(any(SignUpRequest.class));
    }
} 