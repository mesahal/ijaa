package com.ijaa.user.service;

import com.ijaa.user.domain.entity.User;
import com.ijaa.user.domain.request.SignInRequest;
import com.ijaa.user.domain.request.SignUpRequest;
import com.ijaa.user.domain.request.UserPasswordChangeRequest;
import com.ijaa.user.domain.response.AuthResponse;
import com.ijaa.user.repository.UserRepository;
import com.ijaa.user.repository.RefreshTokenRepository;
import com.ijaa.user.common.utils.UniqueIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UniqueIdGenerator idGenerator;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private UserPasswordChangeRequest passwordChangeRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUserId("USER_123456");
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setActive(true);

        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setPassword("password123");

        signInRequest = new SignInRequest();
        signInRequest.setUsername("testuser");
        signInRequest.setPassword("password123");

        passwordChangeRequest = new UserPasswordChangeRequest();
        passwordChangeRequest.setCurrentPassword("oldPassword123");
        passwordChangeRequest.setNewPassword("newPassword123");
        passwordChangeRequest.setConfirmPassword("newPassword123");
    }

    @Test
    void testRegisterUser() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(idGenerator.generateUUID()).thenReturn("USER_123456");
        when(userRepository.existsByUserId("USER_123456")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateAccessToken("testuser", "USER_123456")).thenReturn("test-jwt-token");
        when(jwtService.generateRandomRefreshToken()).thenReturn("test-refresh-token");
        when(refreshTokenRepository.save(any())).thenReturn(null);

        // When
        AuthResponse result = authService.registerUser(signUpRequest);

        // Then
        assertNotNull(result);
        assertEquals("test-jwt-token", result.getAccessToken());
        assertEquals("USER_123456", result.getUserId());
        verify(userRepository).existsByUsername("testuser");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateAccessToken("testuser", "USER_123456");
        verify(jwtService).generateRandomRefreshToken();
    }

    @Test
    void testRegisterUserUsernameAlreadyExists() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.registerUser(signUpRequest));
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testVerify() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtService.generateAccessToken("testuser", "USER_123456")).thenReturn("test-jwt-token");

        // When
        AuthResponse result = authService.verify(signInRequest);

        // Then
        assertNotNull(result);
        assertEquals("test-jwt-token", result.getAccessToken());
        assertEquals("USER_123456", result.getUserId());
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder, atLeastOnce()).matches("password123", "encodedPassword");
        verify(jwtService).generateAccessToken("testuser", "USER_123456");
    }

    @Test
    void testVerifyUserNotFound() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.verify(signInRequest));
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testVerifyInvalidPassword() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> authService.verify(signInRequest));
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder, atLeastOnce()).matches("password123", "encodedPassword");
        verify(jwtService, never()).generateAccessToken(anyString(), anyString());
    }

    // Password Change Tests
    @Test
    void shouldSuccessfullyChangePasswordWhenValidRequestProvided() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("oldPassword123", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.matches("newPassword123", "encodedPassword")).thenReturn(false);
        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Mock SecurityContextHolder to return current user
        try (MockedStatic<SecurityContextHolder> mockedSecurityContext = 
                mockStatic(SecurityContextHolder.class)) {
            
            Authentication mockAuth = mock(Authentication.class);
            when(mockAuth.isAuthenticated()).thenReturn(true);
            when(mockAuth.getName()).thenReturn("testuser");
            
            SecurityContext mockContext = mock(SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuth);
            
            mockedSecurityContext.when(SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When
            authService.changePassword(passwordChangeRequest);

            // Then
            verify(userRepository).findByUsername("testuser");
            verify(passwordEncoder).matches("oldPassword123", "encodedPassword");
            verify(passwordEncoder).encode("newPassword123");
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    void shouldThrowPasswordChangeExceptionWhenCurrentPasswordIsIncorrect() {
        // Given
        UserPasswordChangeRequest request = new UserPasswordChangeRequest();
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newPassword123");
        request.setConfirmPassword("newPassword123");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Mock SecurityContextHolder to return current user
        try (MockedStatic<SecurityContextHolder> mockedSecurityContext = 
                mockStatic(SecurityContextHolder.class)) {
            
            Authentication mockAuth = mock(Authentication.class);
            when(mockAuth.isAuthenticated()).thenReturn(true);
            when(mockAuth.getName()).thenReturn("testuser");
            
            SecurityContext mockContext = mock(SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuth);
            
            mockedSecurityContext.when(SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                authService.changePassword(request);
            });
            
            verify(userRepository).findByUsername("testuser");
            verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void shouldThrowPasswordChangeExceptionWhenNewPasswordAndConfirmPasswordDoNotMatch() {
        // Given
        UserPasswordChangeRequest request = new UserPasswordChangeRequest();
        request.setCurrentPassword("oldPassword123");
        request.setNewPassword("newPassword123");
        request.setConfirmPassword("differentPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("oldPassword123", "encodedPassword")).thenReturn(true);

        // Mock SecurityContextHolder to return current user
        try (MockedStatic<SecurityContextHolder> mockedSecurityContext = 
                mockStatic(SecurityContextHolder.class)) {
            
            Authentication mockAuth = mock(Authentication.class);
            when(mockAuth.isAuthenticated()).thenReturn(true);
            when(mockAuth.getName()).thenReturn("testuser");
            
            SecurityContext mockContext = mock(SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuth);
            
            mockedSecurityContext.when(SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                authService.changePassword(request);
            });
            
            verify(userRepository).findByUsername("testuser");
            verify(passwordEncoder).matches("oldPassword123", "encodedPassword");
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void shouldThrowPasswordChangeExceptionWhenNewPasswordIsSameAsCurrentPassword() {
        // Given
        UserPasswordChangeRequest request = new UserPasswordChangeRequest();
        request.setCurrentPassword("oldPassword123");
        request.setNewPassword("oldPassword123");
        request.setConfirmPassword("oldPassword123");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("oldPassword123", "encodedPassword")).thenReturn(true);

        // Mock SecurityContextHolder to return current user
        try (MockedStatic<SecurityContextHolder> mockedSecurityContext = 
                mockStatic(SecurityContextHolder.class)) {
            
            Authentication mockAuth = mock(Authentication.class);
            when(mockAuth.isAuthenticated()).thenReturn(true);
            when(mockAuth.getName()).thenReturn("testuser");
            
            SecurityContext mockContext = mock(SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuth);
            
            mockedSecurityContext.when(SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                authService.changePassword(request);
            });
            
            verify(userRepository).findByUsername("testuser");
            verify(passwordEncoder, times(2)).matches("oldPassword123", "encodedPassword");
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void shouldThrowAuthenticationFailedExceptionWhenUserNotAuthenticated() {
        // Given
        // Mock SecurityContextHolder to return null authentication
        try (MockedStatic<SecurityContextHolder> mockedSecurityContext = 
                mockStatic(SecurityContextHolder.class)) {
            
            SecurityContext mockContext = mock(SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(null);
            
            mockedSecurityContext.when(SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                authService.changePassword(passwordChangeRequest);
            });
            
            verify(userRepository, never()).findByUsername(anyString());
            verify(passwordEncoder, never()).matches(anyString(), anyString());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Mock SecurityContextHolder to return current user
        try (MockedStatic<SecurityContextHolder> mockedSecurityContext = 
                mockStatic(SecurityContextHolder.class)) {
            
            Authentication mockAuth = mock(Authentication.class);
            when(mockAuth.isAuthenticated()).thenReturn(true);
            when(mockAuth.getName()).thenReturn("testuser");
            
            SecurityContext mockContext = mock(SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuth);
            
            mockedSecurityContext.when(SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When & Then
            assertThrows(RuntimeException.class, () -> {
                authService.changePassword(passwordChangeRequest);
            });
            
            verify(userRepository).findByUsername("testuser");
            verify(passwordEncoder, never()).matches(anyString(), anyString());
            verify(userRepository, never()).save(any(User.class));
        }
    }
}
