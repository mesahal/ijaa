package com.ijaa.user.service;

import com.ijaa.user.common.exceptions.AuthenticationFailedException;
import com.ijaa.user.common.exceptions.UserAlreadyExistsException;
import com.ijaa.user.common.utils.UniqueIdGenerator;
import com.ijaa.user.domain.entity.User;
import com.ijaa.user.domain.request.SignInRequest;
import com.ijaa.user.domain.request.SignUpRequest;
import com.ijaa.user.domain.response.AuthResponse;
import com.ijaa.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;



    @Mock
    private UniqueIdGenerator idGenerator;

    @InjectMocks
    private AuthService authService;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_USER_ID = "USER_ABC123XYZ";
    private static final String TEST_JWT_TOKEN = "test.jwt.token";

    @Test
    @DisplayName("Should register user successfully with valid request")
    void registerUser_Success() {
        // Arrange
        SignUpRequest request = TestDataBuilder.createSignUpRequest();
        when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(false);
        when(userRepository.existsByUserId(TEST_USER_ID)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateUserToken(anyString())).thenReturn(TEST_JWT_TOKEN);
        when(idGenerator.generateUUID()).thenReturn(TEST_USER_ID);

        // Act
        AuthResponse response = authService.registerUser(request);

        // Assert
        assertNotNull(response);
        assertEquals(TEST_JWT_TOKEN, response.getToken());
        assertEquals(TEST_USER_ID, response.getUserId());

        verify(userRepository).existsByUsername(TEST_USERNAME);
        verify(userRepository).existsByUserId(TEST_USER_ID);
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(jwtService).generateUserToken(TEST_USERNAME);
        verify(idGenerator).generateUUID();
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when username already exists")
    void registerUser_UsernameAlreadyExists() {
        // Arrange
        SignUpRequest request = TestDataBuilder.createSignUpRequest();
        when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(true);

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> authService.registerUser(request)
        );

        assertEquals("Username already taken", exception.getMessage());
        verify(userRepository).existsByUsername(TEST_USERNAME);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should retry user ID generation when duplicate ID is generated")
    void registerUser_RetryUserIdGeneration() {
        // Arrange
        SignUpRequest request = TestDataBuilder.createSignUpRequest();
        when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(false);
        when(userRepository.existsByUserId(TEST_USER_ID)).thenReturn(true, false); // First attempt fails, second succeeds
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateUserToken(anyString())).thenReturn(TEST_JWT_TOKEN);
        when(idGenerator.generateUUID()).thenReturn(TEST_USER_ID);

        // Act
        AuthResponse response = authService.registerUser(request);

        // Assert
        assertNotNull(response);
        assertEquals(TEST_JWT_TOKEN, response.getToken());
        assertEquals(TEST_USER_ID, response.getUserId());

        verify(userRepository, times(2)).existsByUserId(TEST_USER_ID);
        verify(idGenerator, times(2)).generateUUID();
    }

    @Test
    @DisplayName("Should throw RuntimeException when unable to generate unique user ID after max attempts")
    void registerUser_MaxAttemptsExceeded() {
        // Arrange
        SignUpRequest request = TestDataBuilder.createSignUpRequest();
        when(userRepository.existsByUsername(TEST_USERNAME)).thenReturn(false);
        when(userRepository.existsByUserId(TEST_USER_ID)).thenReturn(true); // Always return true to trigger max attempts
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(idGenerator.generateUUID()).thenReturn(TEST_USER_ID);
        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.registerUser(request)
        );
        assertEquals("Failed to generate unique user ID after 10 attempts", exception.getMessage());
        verify(userRepository, times(9)).existsByUserId(TEST_USER_ID);
        verify(idGenerator, times(10)).generateUUID();
    }

    @Test
    @DisplayName("Should verify user successfully with valid credentials")
    void verify_Success() {
        // Arrange
        SignInRequest request = TestDataBuilder.createSignInRequest();
        User user = TestDataBuilder.createTestUser();
        
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateUserToken(anyString())).thenReturn(TEST_JWT_TOKEN);

        // Act
        AuthResponse response = authService.verify(request);

        // Assert
        assertNotNull(response);
        assertEquals(TEST_JWT_TOKEN, response.getToken());
        assertEquals(TEST_USER_ID, response.getUserId());

        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(jwtService).generateUserToken(TEST_USERNAME);
    }

    @Test
    @DisplayName("Should throw AuthenticationFailedException when user not found")
    void verify_UserNotFound() {
        // Arrange
        SignInRequest request = TestDataBuilder.createSignInRequest();
        
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        AuthenticationFailedException exception = assertThrows(
                AuthenticationFailedException.class,
                () -> authService.verify(request)
        );

        assertEquals("Invalid credentials", exception.getMessage());
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(jwtService, never()).generateUserToken(anyString());
    }

    @Test
    @DisplayName("Should throw AuthenticationFailedException when password verification fails")
    void verify_AuthenticationFailed() {
        // Arrange
        SignInRequest request = TestDataBuilder.createSignInRequest();
        User user = TestDataBuilder.createTestUser();
        
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        // Act & Assert
        AuthenticationFailedException exception = assertThrows(
                AuthenticationFailedException.class,
                () -> authService.verify(request)
        );

        assertEquals("Invalid credentials", exception.getMessage());
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Should handle null request gracefully")
    void registerUser_NullRequest() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> authService.registerUser(null));
    }

    @Test
    @DisplayName("Should handle null request gracefully in verify method")
    void verify_NullRequest() {
        // Act & Assert
        assertThrows(AuthenticationFailedException.class, () -> authService.verify(null));
    }
} 