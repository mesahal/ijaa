package com.ijaa.event_service.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Utility class for feature flag integration in the event service.
 * Simplified version that defaults to enabled for all features.
 */
@Component
@Slf4j
public class FeatureFlagUtils {

    // Feature flag constants
    public static final String NEW_UI = "NEW_UI";
    public static final String CHAT_FEATURE = "CHAT_FEATURE";
    public static final String EVENT_REGISTRATION = "EVENT_REGISTRATION";
    public static final String PAYMENT_INTEGRATION = "PAYMENT_INTEGRATION";
    public static final String SOCIAL_LOGIN = "SOCIAL_LOGIN";
    public static final String DARK_MODE = "DARK_MODE";
    public static final String NOTIFICATIONS = "NOTIFICATIONS";
    public static final String ADVANCED_SEARCH = "ADVANCED_SEARCH";
    public static final String ALUMNI_DIRECTORY = "ALUMNI_DIRECTORY";
    public static final String MENTORSHIP_PROGRAM = "MENTORSHIP_PROGRAM";
    public static final String EVENT_ANALYTICS = "EVENT_ANALYTICS";
    public static final String EVENT_TEMPLATES = "EVENT_TEMPLATES";
    public static final String RECURRING_EVENTS = "RECURRING_EVENTS";
    public static final String EVENT_MEDIA = "EVENT_MEDIA";
    public static final String EVENT_COMMENTS = "EVENT_COMMENTS";

    /**
     * Check if a feature flag is enabled
     * @param featureName the name of the feature flag
     * @return true if the feature is enabled, false otherwise
     */
    public boolean isFeatureEnabled(String featureName) {
        // Default to enabled for all features in event service
        return true;
    }

    /**
     * Check if event registration is enabled
     * @return true if event registration is enabled
     */
    public boolean isEventRegistrationEnabled() {
        return isFeatureEnabled(EVENT_REGISTRATION);
    }

    /**
     * Check if advanced search is enabled
     * @return true if advanced search is enabled
     */
    public boolean isAdvancedSearchEnabled() {
        return isFeatureEnabled(ADVANCED_SEARCH);
    }

    /**
     * Log feature flag usage for analytics
     * @param featureName the name of the feature flag
     * @param userId the user ID (optional)
     */
    public void logFeatureUsage(String featureName, String userId) {
        log.info("Feature flag {} used by user {}", featureName, userId != null ? userId : "anonymous");
    }
}
