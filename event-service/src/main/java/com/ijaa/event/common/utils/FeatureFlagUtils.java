package com.ijaa.event.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Utility class for feature flag integration in the event service.
 * This is a simplified version that defaults to enabled for development.
 * In a production implementation, this would connect to the user service via Feign client.
 */
@Component
@Slf4j
public class FeatureFlagUtils {

    // Event Features
    public static final String EVENT_REGISTRATION = "EVENT_REGISTRATION";
    public static final String EVENT_MANAGEMENT = "events";
    public static final String EVENT_CREATION = "events.creation";
    public static final String EVENT_UPDATE = "events.update";
    public static final String EVENT_DELETE = "events.delete";
    public static final String EVENT_PARTICIPATION = "events.participation";
    public static final String EVENT_INVITATIONS = "events.invitations";
    public static final String EVENT_COMMENTS = "events.comments";
    public static final String EVENT_MEDIA = "events.media";



    // Search Features
    public static final String ADVANCED_SEARCH = "search";
    public static final String SEARCH_ADVANCED_FILTERS = "search.advanced-filters";

    @Value("${feature.flags.enabled:true}")
    private boolean featureFlagsEnabled;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    /**
     * Check if a feature flag is enabled
     * @param featureName the name of the feature flag
     * @return true if the feature is enabled, false otherwise
     */
    public boolean isFeatureEnabled(String featureName) {
        // Enable features by default for development and testing
        // In production, this would call the user service via Feign client
        boolean isDevelopment = "dev".equals(activeProfile) || "test".equals(activeProfile) || "default".equals(activeProfile);
        
        if (isDevelopment || featureFlagsEnabled) {
            log.debug("Feature flag: {} is enabled for profile: {}", featureName, activeProfile);
            return true;
        }
        
        log.debug("Feature flag: {} is disabled for production profile: {}", featureName, activeProfile);
        return false;
    }

    // Event Features
    public boolean isEventRegistrationEnabled() {
        return isFeatureEnabled(EVENT_REGISTRATION);
    }

    public boolean isEventManagementEnabled() {
        return isFeatureEnabled(EVENT_MANAGEMENT);
    }

    public boolean isEventCreationEnabled() {
        return isFeatureEnabled(EVENT_CREATION);
    }

    public boolean isEventUpdateEnabled() {
        return isFeatureEnabled(EVENT_UPDATE);
    }

    public boolean isEventDeleteEnabled() {
        return isFeatureEnabled(EVENT_DELETE);
    }

    public boolean isEventParticipationEnabled() {
        return isFeatureEnabled(EVENT_PARTICIPATION);
    }

    public boolean isEventInvitationsEnabled() {
        return isFeatureEnabled(EVENT_INVITATIONS);
    }

    public boolean isEventCommentsEnabled() {
        return isFeatureEnabled(EVENT_COMMENTS);
    }

    public boolean isEventMediaEnabled() {
        return isFeatureEnabled(EVENT_MEDIA);
    }





    // Search Features
    public boolean isAdvancedSearchEnabled() {
        return isFeatureEnabled(ADVANCED_SEARCH);
    }

    public boolean isSearchAdvancedFiltersEnabled() {
        return isFeatureEnabled(SEARCH_ADVANCED_FILTERS);
    }

    /**
     * Log feature flag usage for analytics
     * @param featureName the name of the feature flag
     * @param userId the user ID (optional)
     */
    public void logFeatureUsage(String featureName, String userId) {
        if (isFeatureEnabled(featureName)) {
            log.info("Feature flag {} used by user {}", featureName, userId != null ? userId : "anonymous");
        }
    }
}
