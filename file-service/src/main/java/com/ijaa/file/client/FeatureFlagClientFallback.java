package com.ijaa.file.client;

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
        log.warn("User service unavailable, defaulting feature flag '{}' to enabled", featureName);
        // Default to enabled for file service features when user service is unavailable
        FeatureFlagStatus status = new FeatureFlagStatus(featureName, true);
        return new ApiResponse<>("Feature flag defaulted to enabled (fallback)", "200", status);
    }
}
