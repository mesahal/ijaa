package com.ijaa.user.common.utils;

import com.ijaa.user.service.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Utility class for feature flag integration throughout the application.
 * Provides convenient methods to check feature flags and integrate them into various services.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagUtils {

    private final FeatureFlagService featureFlagService;

    // Feature flag constants
    public static final String NEW_UI = "NEW_UI";
    public static final String CHAT_FEATURE = "CHAT_FEATURE";
    public static final String PAYMENT_INTEGRATION = "PAYMENT_INTEGRATION";
    public static final String SOCIAL_LOGIN = "SOCIAL_LOGIN";
    public static final String DARK_MODE = "DARK_MODE";
    public static final String NOTIFICATIONS = "NOTIFICATIONS";
    public static final String ADVANCED_SEARCH = "ADVANCED_SEARCH";
    public static final String ALUMNI_DIRECTORY = "ALUMNI_DIRECTORY";
    public static final String MENTORSHIP_PROGRAM = "MENTORSHIP_PROGRAM";

    /**
     * Check if a feature flag is enabled
     * @param featureName the name of the feature flag
     * @return true if the feature is enabled, false otherwise
     */
    public boolean isFeatureEnabled(String featureName) {
        try {
            return featureFlagService.isFeatureEnabled(featureName);
        } catch (Exception e) {
            log.warn("Error checking feature flag {}: {}", featureName, e.getMessage());
            return false; // Default to disabled on error
        }
    }

    /**
     * Check if new UI is enabled
     * @return true if new UI is enabled
     */
    public boolean isNewUiEnabled() {
        return isFeatureEnabled(NEW_UI);
    }

    /**
     * Check if chat feature is enabled
     * @return true if chat feature is enabled
     */
    public boolean isChatFeatureEnabled() {
        return isFeatureEnabled(CHAT_FEATURE);
    }



    /**
     * Check if payment integration is enabled
     * @return true if payment integration is enabled
     */
    public boolean isPaymentIntegrationEnabled() {
        return isFeatureEnabled(PAYMENT_INTEGRATION);
    }

    /**
     * Check if social login is enabled
     * @return true if social login is enabled
     */
    public boolean isSocialLoginEnabled() {
        return isFeatureEnabled(SOCIAL_LOGIN);
    }

    /**
     * Check if dark mode is enabled
     * @return true if dark mode is enabled
     */
    public boolean isDarkModeEnabled() {
        return isFeatureEnabled(DARK_MODE);
    }

    /**
     * Check if notifications are enabled
     * @return true if notifications are enabled
     */
    public boolean isNotificationsEnabled() {
        return isFeatureEnabled(NOTIFICATIONS);
    }

    /**
     * Check if advanced search is enabled
     * @return true if advanced search is enabled
     */
    public boolean isAdvancedSearchEnabled() {
        return isFeatureEnabled(ADVANCED_SEARCH);
    }

    /**
     * Check if alumni directory is enabled
     * @return true if alumni directory is enabled
     */
    public boolean isAlumniDirectoryEnabled() {
        return isFeatureEnabled(ALUMNI_DIRECTORY);
    }

    /**
     * Check if mentorship program is enabled
     * @return true if mentorship program is enabled
     */
    public boolean isMentorshipProgramEnabled() {
        return isFeatureEnabled(MENTORSHIP_PROGRAM);
    }



    /**
     * Execute a function only if a feature flag is enabled
     * @param featureName the name of the feature flag
     * @param function the function to execute if the feature is enabled
     * @param <T> the return type of the function
     * @return the result of the function if the feature is enabled, null otherwise
     */
    public <T> T executeIfEnabled(String featureName, java.util.function.Supplier<T> function) {
        if (isFeatureEnabled(featureName)) {
            try {
                return function.get();
            } catch (Exception e) {
                log.error("Error executing function for feature {}: {}", featureName, e.getMessage());
                return null;
            }
        }
        return null;
    }

    /**
     * Execute a runnable only if a feature flag is enabled
     * @param featureName the name of the feature flag
     * @param runnable the runnable to execute if the feature is enabled
     */
    public void executeIfEnabled(String featureName, Runnable runnable) {
        if (isFeatureEnabled(featureName)) {
            try {
                runnable.run();
            } catch (Exception e) {
                log.error("Error executing runnable for feature {}: {}", featureName, e.getMessage());
            }
        }
    }

    /**
     * Get a default value if a feature flag is disabled
     * @param featureName the name of the feature flag
     * @param defaultValue the default value to return if the feature is disabled
     * @param <T> the type of the value
     * @return the default value if the feature is disabled, null otherwise
     */
    public <T> T getDefaultIfDisabled(String featureName, T defaultValue) {
        return isFeatureEnabled(featureName) ? null : defaultValue;
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

