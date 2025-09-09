package com.ijaa.user.service;

import com.ijaa.user.common.exceptions.AuthenticationFailedException;
import com.ijaa.user.common.exceptions.PasswordChangeException;
import com.ijaa.user.common.exceptions.UserAlreadyExistsException;
import com.ijaa.user.common.exceptions.UserNotFoundException;
import com.ijaa.user.common.utils.UniqueIdGenerator;
import com.ijaa.user.domain.entity.RefreshToken;
import com.ijaa.user.domain.entity.User;
import com.ijaa.user.domain.request.SignInRequest;
import com.ijaa.user.domain.request.SignUpRequest;
import com.ijaa.user.domain.request.UserPasswordChangeRequest;
import com.ijaa.user.domain.response.AuthResponse;
import com.ijaa.user.repository.RefreshTokenRepository;
import com.ijaa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UniqueIdGenerator idGenerator;

    public AuthResponse registerUser(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already taken");
        }

        User user = new User();

        // Generate unique user ID
        String userId = generateUniqueUserId();
        user.setUserId(userId);

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(request.getUsername(), userId);
        String refreshToken = jwtService.generateRandomRefreshToken();
        
        // Save refresh token to database
        saveRefreshToken(user, refreshToken);

        return new AuthResponse(accessToken, userId);
    }

    public AuthResponse verify(SignInRequest request) {
        try {
            System.err.println("=== SIGN-IN DEBUG START ===");
            System.err.println("Attempting sign-in for username: " + request.getUsername());
            
            // Find user by username
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> {
                        System.err.println("User not found: " + request.getUsername());
                        return new AuthenticationFailedException("User not found");
                    });

            System.err.println("User found: " + user.getUsername() + ", ID: " + user.getUserId());
            System.err.println("Stored password hash: " + user.getPassword());
            System.err.println("Provided password: " + request.getPassword());

            // Test password encoding
            String testHash = passwordEncoder.encode(request.getPassword());
            System.err.println("Test hash for provided password: " + testHash);
            boolean testMatch = passwordEncoder.matches(request.getPassword(), testHash);
            System.err.println("Test password encoding works: " + testMatch);

            // Verify password manually
            boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
            System.err.println("Password matches: " + passwordMatches);
            
            if (!passwordMatches) {
                System.err.println("Password verification failed");
                throw new AuthenticationFailedException("Invalid credentials");
            }

            System.err.println("Password verification successful, generating token...");
            String accessToken = jwtService.generateAccessToken(request.getUsername(), user.getUserId());
            System.err.println("Generated access token successfully");
            System.err.println("=== SIGN-IN DEBUG END ===");
            
            // Temporarily skip refresh token logic for debugging
            // String refreshToken = jwtService.generateRandomRefreshToken();
            // refreshTokenRepository.revokeAllUserTokens(user);
            // saveRefreshToken(user, refreshToken);

            return new AuthResponse(accessToken, user.getUserId());
        } catch (AuthenticationFailedException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            throw e; // Re-throw authentication exceptions
        } catch (Exception e) {
            // Log the actual exception for debugging
            System.err.println("Sign-in error: " + e.getMessage());
            e.printStackTrace();
            throw new AuthenticationFailedException("Invalid credentials");
        }
    }

    /**
     * Generates a unique user ID and ensures it doesn't already exist
     */
    private String generateUniqueUserId() {
        String userId;
        int maxAttempts = 10;
        int attempts = 0;

        do {
            // Choose your preferred ID format:
            userId = idGenerator.generateUUID(); // USER_ABC12XYZ
            // Or use: userId = idGenerator.generateShortId(10); // abc123xyz0
            // Or use: userId = idGenerator.generateSnowflakeId(); // 1234567890123456

            attempts++;

            if (attempts >= maxAttempts) {
                throw new RuntimeException("Failed to generate unique user ID after " + maxAttempts + " attempts");
            }

        } while (userRepository.existsByUserId(userId));

        return userId;
    }

    /**
     * Change user password
     */
    public void changePassword(UserPasswordChangeRequest request) {
        // Get current authenticated user
        String currentUsername = getCurrentUsername();
        if (currentUsername == null) {
            throw new AuthenticationFailedException("Authentication required to change password");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new PasswordChangeException("Current password is incorrect");
        }

        // Validate new password confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordChangeException("New password and confirm password do not match");
        }

        // Check if new password is same as current password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new PasswordChangeException("New password must be different from current password");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Gets the current authenticated username from security context
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !"anonymousUser".equals(authentication.getName())) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * Refresh access token using refresh token
     */
    public AuthResponse refreshToken(String refreshToken) {
        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findValidToken(refreshToken, LocalDateTime.now());
        
        if (tokenOptional.isEmpty()) {
            throw new AuthenticationFailedException("Invalid or expired refresh token");
        }
        
        RefreshToken token = tokenOptional.get();
        User user = token.getUser();
        
        // Generate new access token
        String newAccessToken = jwtService.generateAccessToken(user.getUsername(), user.getUserId());
        
        return new AuthResponse(newAccessToken, user.getUserId());
    }

    /**
     * Logout user by revoking refresh token
     */
    public void logout(String refreshToken) {
        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByToken(refreshToken);
        
        if (tokenOptional.isPresent()) {
            RefreshToken token = tokenOptional.get();
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        }
    }

    /**
     * Save refresh token to database
     */
    private void saveRefreshToken(User user, String refreshToken) {
        RefreshToken token = new RefreshToken();
        token.setToken(refreshToken);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusDays(7)); // 7 days from now
        token.setRevoked(false);
        
        refreshTokenRepository.save(token);
    }

    /**
     * Get refresh token for user (for setting in cookie)
     */
    public String getRefreshTokenForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByUserAndRevokedFalse(user)
                .stream()
                .filter(token -> !token.isExpired())
                .findFirst();
        
        return tokenOptional.map(RefreshToken::getToken).orElse(null);
    }
}
