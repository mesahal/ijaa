package com.ijaa.user.service;

import com.ijaa.user.common.exceptions.AuthenticationFailedException;
import com.ijaa.user.common.exceptions.GoogleOAuthException;
import com.ijaa.user.common.exceptions.PasswordChangeException;
import com.ijaa.user.common.exceptions.UserAlreadyExistsException;
import com.ijaa.user.common.exceptions.UserNotFoundException;
import com.ijaa.user.common.utils.UniqueIdGenerator;
import com.ijaa.user.domain.entity.User;
import com.ijaa.user.domain.enums.AuthProvider;
import com.ijaa.user.domain.request.SignInRequest;
import com.ijaa.user.domain.request.SignUpRequest;
import com.ijaa.user.domain.request.GoogleSignInRequest;
import com.ijaa.user.domain.request.UserPasswordChangeRequest;
import com.ijaa.user.domain.response.AuthResponse;
import com.ijaa.user.domain.dto.GoogleUserInfo;
import com.ijaa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UniqueIdGenerator idGenerator;
    private final GoogleOAuthService googleOAuthService;

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
        user.setAuthProvider(AuthProvider.LOCAL);

        userRepository.save(user);

        String token = jwtService.generateUserToken(request.getUsername(), userId);

        return new AuthResponse(token, userId);
    }

    public AuthResponse verify(SignInRequest request) {
        try {
            // Find user by username
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new AuthenticationFailedException("User not found"));

            // Verify password manually
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new AuthenticationFailedException("Invalid credentials");
            }

            String token = jwtService.generateUserToken(request.getUsername(), user.getUserId());

            return new AuthResponse(token, user.getUserId());
        } catch (Exception e) {
            throw new AuthenticationFailedException("Invalid credentials");
        }
    }

    /**
     * Google Sign-In authentication
     * @param request Google sign-in request containing OAuth tokens
     * @return AuthResponse with JWT token and user ID
     */
    public AuthResponse googleSignIn(GoogleSignInRequest request) {
        try {
            // Verify Google OAuth token and extract user information
            GoogleUserInfo googleUserInfo = googleOAuthService.verifyGoogleToken(request);
            
            // Check if user already exists
            User user = userRepository.findByEmail(googleUserInfo.getEmail())
                    .orElseGet(() -> createGoogleUser(googleUserInfo));
            
            // Generate JWT token
            String token = jwtService.generateUserToken(user.getUsername(), user.getUserId());
            
            return new AuthResponse(token, user.getUserId());
            
        } catch (Exception e) {
            throw new GoogleOAuthException("Google Sign-In failed: " + e.getMessage(), e);
        }
    }

    /**
     * Create a new user from Google OAuth information
     * @param googleUserInfo Google user information
     * @return Created user
     */
    private User createGoogleUser(GoogleUserInfo googleUserInfo) {
        // Check if email is already used by a local user
        if (userRepository.existsByEmail(googleUserInfo.getEmail())) {
            throw new UserAlreadyExistsException("Email already registered with local account");
        }

        User user = new User();
        
        // Generate unique user ID
        String userId = generateUniqueUserId();
        user.setUserId(userId);
        
        // Set Google OAuth information
        user.setGoogleId(googleUserInfo.getSub());
        user.setEmail(googleUserInfo.getEmail());
        user.setFirstName(googleUserInfo.getGivenName());
        user.setLastName(googleUserInfo.getFamilyName());
        user.setProfilePictureUrl(googleUserInfo.getPicture());
        user.setLocale(googleUserInfo.getLocale());
        user.setEmailVerified(googleUserInfo.getEmailVerified() != null ? 
            googleUserInfo.getEmailVerified().toString() : "false");
        user.setAuthProvider(AuthProvider.GOOGLE);
        
        // Set username as email for Google users
        user.setUsername(googleUserInfo.getEmail());
        
        // Generate a random password for Google users (they won't use it)
        user.setPassword(passwordEncoder.encode(generateRandomPassword()));
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        
        return savedUser;
    }

    /**
     * Generates a unique user ID and ensures it doesn't already exist
     */
    private String generateUniqueUserId() {
        String userId;
        do {
            userId = idGenerator.generateUserIdWithPrefix();
        } while (userRepository.existsByUserId(userId));
        return userId;
    }

    /**
     * Generate a random password for Google OAuth users
     */
    private String generateRandomPassword() {
        return java.util.UUID.randomUUID().toString();
    }

    public void changePassword(UserPasswordChangeRequest request) {
        // Get current authenticated user
        String currentUsername = getCurrentUsername();
        if (currentUsername == null) {
            throw new AuthenticationFailedException("Authentication required to change password");
        }

        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Check if user is a Google OAuth user
        if (AuthProvider.GOOGLE.equals(user.getAuthProvider())) {
            throw new AuthenticationFailedException("Password change not allowed for Google OAuth users");
        }

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
}
