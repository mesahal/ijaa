package com.ijaa.user.config;

import com.ijaa.user.domain.entity.CurrentUserContext;
import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.repository.FeatureFlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;

@TestConfiguration
public class TestConfig {

    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    @PostConstruct
    public void setupMockRequestContext() {
        // Create a mock HTTP request with user context
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        // Create a mock user context
        CurrentUserContext userContext = new CurrentUserContext();
        userContext.setUserId("USER_123456");
        userContext.setUsername("testuser");
        userContext.setUserType("USER");
        userContext.setRole("USER");
        
        // Encode the user context as Base64 (simulating gateway behavior)
        String jsonContext = "{\"userId\":\"USER_123456\",\"username\":\"testuser\",\"userType\":\"USER\",\"role\":\"USER\"}";
        String base64Context = java.util.Base64.getUrlEncoder().encodeToString(jsonContext.getBytes());
        
        // Set the header that the gateway would set
        request.addHeader("X-USER_ID", base64Context);
        
        // Set the request in the context holder
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        
        // Setup feature flags for testing
        setupFeatureFlags();
    }
    
    private void setupFeatureFlags() {
        // Enable required feature flags for testing
        enableFeatureFlag("user.registration", "User Registration", "Enable user registration");
        enableFeatureFlag("user.login", "User Login", "Enable user login");
        enableFeatureFlag("user.password-change", "User Password Change", "Enable user password change");
        enableFeatureFlag("chat", "Chat Feature", "Enable chat functionality");
    }
    
    private void enableFeatureFlag(String name, String displayName, String description) {
        if (!featureFlagRepository.existsByName(name)) {
            FeatureFlag featureFlag = new FeatureFlag();
            featureFlag.setName(name);
            featureFlag.setDisplayName(displayName);
            featureFlag.setDescription(description);
            featureFlag.setEnabled(true);
            featureFlag.setCreatedAt(LocalDateTime.now());
            featureFlag.setUpdatedAt(LocalDateTime.now());
            featureFlagRepository.save(featureFlag);
        } else {
            // Update existing flag to be enabled
            featureFlagRepository.findByName(name).ifPresent(flag -> {
                flag.setEnabled(true);
                flag.setUpdatedAt(LocalDateTime.now());
                featureFlagRepository.save(flag);
            });
        }
    }

    @Bean
    @Primary
    public CurrentUserContext mockCurrentUserContext() {
        CurrentUserContext userContext = new CurrentUserContext();
        userContext.setUserId("USER_123456");
        userContext.setUsername("testuser");
        userContext.setUserType("USER");
        userContext.setRole("USER");
        return userContext;
    }
}
