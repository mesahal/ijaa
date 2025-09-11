package com.ijaa.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.common.exceptions.AuthenticationFailedException;
import com.ijaa.user.common.exceptions.PasswordChangeException;
import com.ijaa.user.common.exceptions.UserAlreadyExistsException;
import com.ijaa.user.common.exceptions.UserNotFoundException;
import com.ijaa.user.common.utils.UniqueIdGenerator;
import com.ijaa.user.common.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import com.ijaa.user.domain.entity.RefreshToken;
import com.ijaa.user.domain.entity.User;
import com.ijaa.user.domain.request.SignInRequest;
import com.ijaa.user.domain.request.SignUpRequest;
import com.ijaa.user.domain.request.UserPasswordChangeRequest;
import com.ijaa.user.domain.response.AuthResponse;
import com.ijaa.user.repository.RefreshTokenRepository;
import com.ijaa.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthService extends BaseService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UniqueIdGenerator idGenerator;
    private final CookieUtils cookieUtils;

    public AuthService(UserRepository userRepository,
                      RefreshTokenRepository refreshTokenRepository,
                      BCryptPasswordEncoder passwordEncoder,
                      JWTService jwtService,
                      UniqueIdGenerator idGenerator,
                      CookieUtils cookieUtils,
                      ObjectMapper objectMapper) {
        super(objectMapper);
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.idGenerator = idGenerator;
        this.cookieUtils = cookieUtils;
    }

    public AuthResponse registerUser(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken");
        }

        User user = new User();

        String userId = idGenerator.generateUUID();
        user.setUserId(userId);
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(request.getUsername(), userId);
        String refreshToken = jwtService.generateRandomRefreshToken();
        saveRefreshToken(user, refreshToken);

        return new AuthResponse(accessToken, userId, refreshToken);
    }

    public AuthResponse verify(SignInRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationFailedException("User not found"));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        
        if (!passwordMatches) {
            throw new AuthenticationFailedException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(request.getUsername(), user.getUserId());
        
        // Try to handle refresh tokens, but don't let refresh token issues block authentication
        try {
            // Revoke all existing refresh tokens for this user
            refreshTokenRepository.revokeAllUserTokens(user);
            
            // Generate new refresh token
            String refreshToken = jwtService.generateRandomRefreshToken();
            saveRefreshToken(user, refreshToken);
            
            return new AuthResponse(accessToken, user.getUserId(), refreshToken);
        } catch (Exception e) {
            log.warn("Failed to handle refresh token for user {}: {}", user.getUsername(), e.getMessage());
            // Return access token without refresh token if refresh token handling fails
            return new AuthResponse(accessToken, user.getUserId(), null);
        }
    }


    public void changePassword(UserPasswordChangeRequest request) {
        String currentUsername = getCurrentUsername();
        if (currentUsername == null) {
            throw new AuthenticationFailedException("Authentication required to change password");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new PasswordChangeException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordChangeException("New password and confirm password do not match");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new PasswordChangeException("New password must be different from current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    public AuthResponse refreshToken(String refreshToken) {
        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findValidToken(refreshToken, LocalDateTime.now());
        
        if (tokenOptional.isEmpty()) {
            throw new AuthenticationFailedException("Invalid or expired refresh token");
        }
        
        RefreshToken token = tokenOptional.get();
        User user = token.getUser();
        
        String newAccessToken = jwtService.generateAccessToken(user.getUsername(), user.getUserId());
        
        return new AuthResponse(newAccessToken, user.getUserId());
    }

    public void logout(String refreshToken) {
        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByToken(refreshToken);
        
        if (tokenOptional.isPresent()) {
            RefreshToken token = tokenOptional.get();
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        }
    }

    private void saveRefreshToken(User user, String refreshToken) {
        RefreshToken token = new RefreshToken();
        token.setToken(refreshToken);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusDays(7));
        token.setRevoked(false);
        
        refreshTokenRepository.save(token);
    }

    public String getRefreshTokenForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByUserAndRevokedFalse(user)
                .stream()
                .filter(token -> !token.isExpired())
                .findFirst();
        
        return tokenOptional.map(RefreshToken::getToken).orElse(null);
    }

    public AuthResponse signInWithCookieManagement(SignInRequest request, HttpServletResponse response) {
        AuthResponse authResponse = verify(request);
        
        String refreshToken = getRefreshTokenForUser(request.getUsername());
        if (refreshToken != null) {
            cookieUtils.setRefreshTokenCookie(response, refreshToken);
        }
        
        return authResponse;
    }

    public String extractRefreshToken(Map<String, String> requestBody, HttpServletRequest request) {
        String refreshToken = null;
        
        if (requestBody != null && requestBody.containsKey("refreshToken")) {
            refreshToken = requestBody.get("refreshToken");
        }
        
        if (refreshToken == null) {
            refreshToken = cookieUtils.getRefreshTokenFromCookie(request);
        }
        
        return refreshToken;
    }

    public boolean isUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }

    public void logoutWithCookieManagement(Map<String, String> requestBody, HttpServletRequest request, HttpServletResponse response) {
        if (!isUserAuthenticated()) {
            throw new AuthenticationFailedException("Authentication required");
        }
        
        String refreshToken = extractRefreshToken(requestBody, request);
        
        if (refreshToken != null && !refreshToken.trim().isEmpty()) {
            try {
                logout(refreshToken);
            } catch (Exception e) {
                // Log the error but don't fail the logout
                // User might have already logged out or token might be invalid
            }
        }
        
        cookieUtils.clearRefreshTokenCookie(response);
    }
}
