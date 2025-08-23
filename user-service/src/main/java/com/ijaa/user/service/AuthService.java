package com.ijaa.user.service;

import com.ijaa.user.common.exceptions.AuthenticationFailedException;
import com.ijaa.user.common.exceptions.UserAlreadyExistsException;
import com.ijaa.user.common.utils.UniqueIdGenerator;
import com.ijaa.user.domain.entity.User;
import com.ijaa.user.domain.request.SignInRequest;
import com.ijaa.user.domain.request.SignUpRequest;
import com.ijaa.user.domain.response.AuthResponse;
import com.ijaa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
}
