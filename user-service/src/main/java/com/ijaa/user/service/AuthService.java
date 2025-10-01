package com.ijaa.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.common.exceptions.AuthenticationFailedException;
import com.ijaa.user.common.exceptions.PasswordChangeException;
import com.ijaa.user.common.exceptions.UserAlreadyExistsException;
import com.ijaa.user.common.exceptions.UserNotFoundException;
import com.ijaa.user.common.utils.UniqueIdGenerator;
import com.ijaa.user.common.utils.CookieUtils;
import com.ijaa.user.presenter.rest.external.GatewayTokenBlacklistClient;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthService extends BaseService {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UniqueIdGenerator idGenerator;
    private final CookieUtils cookieUtils;
    // TokenBlacklistService removed - now handled by gateway
    private final GatewayTokenBlacklistClient gatewayTokenBlacklistClient;

    public AuthService(UserRepository userRepository,
                      RefreshTokenRepository refreshTokenRepository,
                      BCryptPasswordEncoder passwordEncoder,
                      JWTService jwtService,
                      UniqueIdGenerator idGenerator,
                      CookieUtils cookieUtils,
                      GatewayTokenBlacklistClient gatewayTokenBlacklistClient,
                      ObjectMapper objectMapper) {
        super(objectMapper);
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.idGenerator = idGenerator;
        this.cookieUtils = cookieUtils;
        // TokenBlacklistService removed - now handled by gateway
        this.gatewayTokenBlacklistClient = gatewayTokenBlacklistClient;
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
        return new AuthResponse(accessToken, userId);
    }

    public AuthResponse verify(SignInRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationFailedException("User not found"));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        
        if (!passwordMatches) {
            throw new AuthenticationFailedException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(request.getUsername(), user.getUserId());
        try {
            refreshTokenRepository.revokeAllUserTokens(user);
            String refreshToken = jwtService.generateRandomRefreshToken();
            saveRefreshToken(user, refreshToken);
            return new AuthResponse(accessToken, user.getUserId());
        } catch (Exception e) {
            log.warn("Failed to handle refresh token for user {}: {}", user.getUsername(), e.getMessage());
            return new AuthResponse(accessToken, user.getUserId());
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
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationFailedException("User not found"));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        
        if (!passwordMatches) {
            throw new AuthenticationFailedException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(request.getUsername(), user.getUserId());
        try {
            refreshTokenRepository.revokeAllUserTokens(user);
            String refreshToken = jwtService.generateRandomRefreshToken();
            saveRefreshToken(user, refreshToken);
            cookieUtils.setRefreshTokenCookie(response, refreshToken, "prod".equals(activeProfile));
            return new AuthResponse(accessToken, user.getUserId());
        } catch (Exception e) {
            log.warn("Failed to handle refresh token for user {}: {}", user.getUsername(), e.getMessage());
            return new AuthResponse(accessToken, user.getUserId());
        }
    }

    public AuthResponse registerUserWithCookieManagement(SignUpRequest request, HttpServletResponse response) {
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
        
        cookieUtils.setRefreshTokenCookie(response, refreshToken, "prod".equals(activeProfile));

        return new AuthResponse(accessToken, userId);
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
        
        // Extract access token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            String userId = jwtService.extractClaim(accessToken, claims -> claims.get("userId", String.class));
            if (userId == null) {
                Integer adminId = jwtService.extractClaim(accessToken, claims -> claims.get("adminId", Integer.class));
                if (adminId != null) {
                    userId = adminId.toString();
                }
            }
            String userType = jwtService.extractRole(accessToken).equals("ADMIN") ? "ADMIN" : "USER";
            
            // Blacklist the access token via gateway
            gatewayTokenBlacklistClient.blacklistToken(accessToken, userId, userType);
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
        
        cookieUtils.clearRefreshTokenCookie(response, "prod".equals(activeProfile));
    }


    // Getter methods for access from controllers
    public JWTService getJwtService() {
        return jwtService;
    }

    public CookieUtils getCookieUtils() {
        return cookieUtils;
    }

    public String getActiveProfile() {
        return activeProfile;
    }

    // TokenBlacklistService getter removed - now handled by gateway
}
