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

    // Core UI Features
    public static final String NEW_UI = "NEW_UI";
    public static final String DARK_MODE = "DARK_MODE";
    public static final String NOTIFICATIONS = "NOTIFICATIONS";

    // Authentication & User Management
    public static final String SOCIAL_LOGIN = "SOCIAL_LOGIN";
    public static final String USER_REGISTRATION = "user.registration";
    public static final String USER_LOGIN = "user.login";
    public static final String USER_PASSWORD_CHANGE = "user.password-change";
    public static final String USER_PROFILE_FEATURES = "user.profile";
    public static final String USER_EXPERIENCES = "user.experiences";
    public static final String USER_INTERESTS = "user.interests";

    // Chat Features
    public static final String CHAT_FEATURE = "chat";
    public static final String CHAT_FILE_SHARING = "chat.file-sharing";
    public static final String CHAT_VOICE_CALLS = "chat.voice-calls";

    // Search Features
    public static final String ADVANCED_SEARCH = "search";
    public static final String SEARCH_ADVANCED_FILTERS = "search.advanced-filters";
    public static final String ALUMNI_SEARCH = "alumni.search";
    public static final String ALUMNI_DIRECTORY = "ALUMNI_DIRECTORY";

    // File Management
    public static final String FILE_UPLOAD = "file-upload";
    public static final String FILE_UPLOAD_PROFILE_PHOTO = "file-upload.profile-photo";
    public static final String FILE_UPLOAD_COVER_PHOTO = "file-upload.cover-photo";
    public static final String FILE_DOWNLOAD = "file-download";
    public static final String FILE_DELETE = "file-delete";

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
    public static final String EVENT_TEMPLATES = "events.templates";
    public static final String RECURRING_EVENTS = "events.recurring";
    public static final String EVENT_ANALYTICS = "events.analytics";
    public static final String EVENT_REMINDERS = "events.reminders";
    public static final String CALENDAR_INTEGRATION = "calendar.integration";

    // Admin Features
    public static final String ADMIN_FEATURES = "admin.features";
    public static final String ADMIN_USER_MANAGEMENT = "admin.user-management";
    public static final String ADMIN_EVENT_MANAGEMENT = "admin.event-management";
    public static final String ADMIN_ANNOUNCEMENTS = "admin.announcements";
    public static final String ADMIN_REPORTS = "admin.reports";
    public static final String ADMIN_AUTH = "admin.auth";

    // Business Features
    public static final String PAYMENT_INTEGRATION = "PAYMENT_INTEGRATION";
    public static final String MENTORSHIP_PROGRAM = "MENTORSHIP_PROGRAM";
    public static final String ANNOUNCEMENT_SYSTEM = "announcements";
    public static final String REPORT_SYSTEM = "reports";

    /**
     * Check if a feature flag is enabled
     * @param featureName the name of the feature flag
     * @return true if the feature is enabled, false otherwise
     */
    public boolean isFeatureEnabled(String featureName) {
        try {
            return featureFlagService.isEnabled(featureName);
        } catch (Exception e) {
            log.warn("Error checking feature flag {}: {}", featureName, e.getMessage());
            return false; // Default to disabled on error
        }
    }

    // Core UI Features
    public boolean isNewUiEnabled() {
        return isFeatureEnabled(NEW_UI);
    }

    public boolean isDarkModeEnabled() {
        return isFeatureEnabled(DARK_MODE);
    }

    public boolean isNotificationsEnabled() {
        return isFeatureEnabled(NOTIFICATIONS);
    }

    // Authentication & User Management
    public boolean isSocialLoginEnabled() {
        return isFeatureEnabled(SOCIAL_LOGIN);
    }

    public boolean isUserRegistrationEnabled() {
        return isFeatureEnabled(USER_REGISTRATION);
    }

    public boolean isUserLoginEnabled() {
        return isFeatureEnabled(USER_LOGIN);
    }

    public boolean isUserPasswordChangeEnabled() {
        return isFeatureEnabled(USER_PASSWORD_CHANGE);
    }

    public boolean isUserProfileFeaturesEnabled() {
        return isFeatureEnabled(USER_PROFILE_FEATURES);
    }

    public boolean isUserExperiencesEnabled() {
        return isFeatureEnabled(USER_EXPERIENCES);
    }

    public boolean isUserInterestsEnabled() {
        return isFeatureEnabled(USER_INTERESTS);
    }

    // Chat Features
    public boolean isChatFeatureEnabled() {
        return isFeatureEnabled(CHAT_FEATURE);
    }

    public boolean isChatFileSharingEnabled() {
        return isFeatureEnabled(CHAT_FILE_SHARING);
    }

    public boolean isChatVoiceCallsEnabled() {
        return isFeatureEnabled(CHAT_VOICE_CALLS);
    }

    // Search Features
    public boolean isAdvancedSearchEnabled() {
        return isFeatureEnabled(ADVANCED_SEARCH);
    }

    public boolean isSearchAdvancedFiltersEnabled() {
        return isFeatureEnabled(SEARCH_ADVANCED_FILTERS);
    }

    public boolean isAlumniSearchEnabled() {
        return isFeatureEnabled(ALUMNI_SEARCH);
    }

    public boolean isAlumniDirectoryEnabled() {
        return isFeatureEnabled(ALUMNI_DIRECTORY);
    }

    // File Management
    public boolean isFileUploadEnabled() {
        return isFeatureEnabled(FILE_UPLOAD);
    }

    public boolean isProfilePhotoUploadEnabled() {
        return isFeatureEnabled(FILE_UPLOAD_PROFILE_PHOTO);
    }

    public boolean isCoverPhotoUploadEnabled() {
        return isFeatureEnabled(FILE_UPLOAD_COVER_PHOTO);
    }

    public boolean isFileDownloadEnabled() {
        return isFeatureEnabled(FILE_DOWNLOAD);
    }

    public boolean isFileDeleteEnabled() {
        return isFeatureEnabled(FILE_DELETE);
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

    public boolean isEventTemplatesEnabled() {
        return isFeatureEnabled(EVENT_TEMPLATES);
    }

    public boolean isRecurringEventsEnabled() {
        return isFeatureEnabled(RECURRING_EVENTS);
    }

    public boolean isEventAnalyticsEnabled() {
        return isFeatureEnabled(EVENT_ANALYTICS);
    }

    public boolean isEventRemindersEnabled() {
        return isFeatureEnabled(EVENT_REMINDERS);
    }

    public boolean isCalendarIntegrationEnabled() {
        return isFeatureEnabled(CALENDAR_INTEGRATION);
    }

    // Admin Features
    public boolean isAdminFeaturesEnabled() {
        return isFeatureEnabled(ADMIN_FEATURES);
    }

    public boolean isAdminUserManagementEnabled() {
        return isFeatureEnabled(ADMIN_USER_MANAGEMENT);
    }

    public boolean isAdminEventManagementEnabled() {
        return isFeatureEnabled(ADMIN_EVENT_MANAGEMENT);
    }

    public boolean isAdminAnnouncementsEnabled() {
        return isFeatureEnabled(ADMIN_ANNOUNCEMENTS);
    }

    public boolean isAdminReportsEnabled() {
        return isFeatureEnabled(ADMIN_REPORTS);
    }

    public boolean isAdminAuthEnabled() {
        return isFeatureEnabled(ADMIN_AUTH);
    }

    // Business Features
    public boolean isPaymentIntegrationEnabled() {
        return isFeatureEnabled(PAYMENT_INTEGRATION);
    }

    public boolean isMentorshipProgramEnabled() {
        return isFeatureEnabled(MENTORSHIP_PROGRAM);
    }

    public boolean isAnnouncementSystemEnabled() {
        return isFeatureEnabled(ANNOUNCEMENT_SYSTEM);
    }

    public boolean isReportSystemEnabled() {
        return isFeatureEnabled(REPORT_SYSTEM);
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

