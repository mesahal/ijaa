package com.ijaa.file_service.config;

import com.ijaa.file_service.client.FeatureFlagClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Utility class for feature flag integration in the file service.
 * Connects to the user service via Feign client to check feature flags.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagUtils {

    private final FeatureFlagClient featureFlagClient;

    // Feature flag constants
    public static final String FILE_UPLOAD = "file-upload";
    public static final String FILE_UPLOAD_PROFILE_PHOTO = "file-upload.profile-photo";
    public static final String FILE_UPLOAD_COVER_PHOTO = "file-upload.cover-photo";
    public static final String FILE_DOWNLOAD = "file-download";
    public static final String FILE_DELETE = "file-delete";

    /**
     * Check if a feature flag is enabled
     * @param featureName the name of the feature flag
     * @return true if the feature is enabled, false otherwise
     */
    public boolean isFeatureEnabled(String featureName) {
        try {
            FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
                featureFlagClient.checkFeatureFlag(featureName);
            
            if (response != null && response.getData() != null) {
                boolean enabled = response.getData().isEnabled();
                log.debug("Feature flag '{}' is {}", featureName, enabled ? "enabled" : "disabled");
                return enabled;
            } else {
                log.warn("Invalid response for feature flag '{}': response or data is null", featureName);
                return true; // Default to enabled for file service features on error
            }
        } catch (Exception e) {
            log.warn("Error checking feature flag '{}': {}. Defaulting to enabled.", featureName, e.getMessage());
            return true; // Default to enabled for file service features on error
        }
    }

    /**
     * Check if file upload is enabled
     * @return true if file upload is enabled
     */
    public boolean isFileUploadEnabled() {
        return isFeatureEnabled(FILE_UPLOAD);
    }

    /**
     * Check if profile photo upload is enabled
     * @return true if profile photo upload is enabled
     */
    public boolean isProfilePhotoUploadEnabled() {
        return isFeatureEnabled(FILE_UPLOAD_PROFILE_PHOTO);
    }

    /**
     * Check if cover photo upload is enabled
     * @return true if cover photo upload is enabled
     */
    public boolean isCoverPhotoUploadEnabled() {
        return isFeatureEnabled(FILE_UPLOAD_COVER_PHOTO);
    }

    /**
     * Check if file download is enabled
     * @return true if file download is enabled
     */
    public boolean isFileDownloadEnabled() {
        return isFeatureEnabled(FILE_DOWNLOAD);
    }

    /**
     * Check if file delete is enabled
     * @return true if file delete is enabled
     */
    public boolean isFileDeleteEnabled() {
        return isFeatureEnabled(FILE_DELETE);
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
