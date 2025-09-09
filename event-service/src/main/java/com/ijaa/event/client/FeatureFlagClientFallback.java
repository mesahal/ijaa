package com.ijaa.event.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Fallback implementation for FeatureFlagClient when user service is unavailable
 */
@Component
@Slf4j
public class FeatureFlagClientFallback implements FeatureFlagClient {

    @Override
    public ApiResponse<FeatureFlagStatus> checkFeatureFlag(String featureName) {
        log.warn("User service unavailable. Feature flag '{}' check failed. Defaulting to disabled.", featureName);
        
        // Return disabled status as fallback for safety
        FeatureFlagStatus status = new FeatureFlagStatus(featureName, false);
        return new ApiResponse<>("Feature flag check failed - service unavailable", "SERVICE_UNAVAILABLE", status);
    }
}
