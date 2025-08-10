package com.ijaa.user.common.utils;

import com.ijaa.user.service.FeatureFlagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FeatureFlagUtils
 * Tests utility methods for feature flag integration
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Feature Flag Utils Tests")
class FeatureFlagUtilsTest {

    @Mock
    private FeatureFlagService featureFlagService;

    @InjectMocks
    private FeatureFlagUtils featureFlagUtils;

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        reset(featureFlagService);
    }

    @Test
    @DisplayName("Should check if feature is enabled successfully")
    void shouldCheckIfFeatureIsEnabled() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE")).thenReturn(true);

        // Act
        boolean result = featureFlagUtils.isFeatureEnabled("TEST_FEATURE");

        // Assert
        assertTrue(result);
        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should return false when feature flag service throws exception")
    void shouldReturnFalseWhenFeatureFlagServiceThrowsException() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE"))
            .thenThrow(new RuntimeException("Database error"));

        // Act
        boolean result = featureFlagUtils.isFeatureEnabled("TEST_FEATURE");

        // Assert
        assertFalse(result);
        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should check new UI feature flag")
    void shouldCheckNewUiFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.NEW_UI)).thenReturn(true);

        // Act
        boolean result = featureFlagUtils.isNewUiEnabled();

        // Assert
        assertTrue(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.NEW_UI);
    }

    @Test
    @DisplayName("Should check chat feature flag")
    void shouldCheckChatFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.CHAT_FEATURE)).thenReturn(false);

        // Act
        boolean result = featureFlagUtils.isChatFeatureEnabled();

        // Assert
        assertFalse(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.CHAT_FEATURE);
    }

    @Test
    @DisplayName("Should check event registration feature flag")
    void shouldCheckEventRegistrationFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.EVENT_REGISTRATION)).thenReturn(true);

        // Act
        boolean result = featureFlagUtils.isEventRegistrationEnabled();

        // Assert
        assertTrue(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.EVENT_REGISTRATION);
    }

    @Test
    @DisplayName("Should check payment integration feature flag")
    void shouldCheckPaymentIntegrationFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.PAYMENT_INTEGRATION)).thenReturn(false);

        // Act
        boolean result = featureFlagUtils.isPaymentIntegrationEnabled();

        // Assert
        assertFalse(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.PAYMENT_INTEGRATION);
    }

    @Test
    @DisplayName("Should check social login feature flag")
    void shouldCheckSocialLoginFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.SOCIAL_LOGIN)).thenReturn(true);

        // Act
        boolean result = featureFlagUtils.isSocialLoginEnabled();

        // Assert
        assertTrue(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.SOCIAL_LOGIN);
    }

    @Test
    @DisplayName("Should check dark mode feature flag")
    void shouldCheckDarkModeFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.DARK_MODE)).thenReturn(true);

        // Act
        boolean result = featureFlagUtils.isDarkModeEnabled();

        // Assert
        assertTrue(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.DARK_MODE);
    }

    @Test
    @DisplayName("Should check notifications feature flag")
    void shouldCheckNotificationsFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.NOTIFICATIONS)).thenReturn(false);

        // Act
        boolean result = featureFlagUtils.isNotificationsEnabled();

        // Assert
        assertFalse(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.NOTIFICATIONS);
    }

    @Test
    @DisplayName("Should check advanced search feature flag")
    void shouldCheckAdvancedSearchFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.ADVANCED_SEARCH)).thenReturn(true);

        // Act
        boolean result = featureFlagUtils.isAdvancedSearchEnabled();

        // Assert
        assertTrue(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.ADVANCED_SEARCH);
    }

    @Test
    @DisplayName("Should check alumni directory feature flag")
    void shouldCheckAlumniDirectoryFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.ALUMNI_DIRECTORY)).thenReturn(true);

        // Act
        boolean result = featureFlagUtils.isAlumniDirectoryEnabled();

        // Assert
        assertTrue(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.ALUMNI_DIRECTORY);
    }

    @Test
    @DisplayName("Should check mentorship program feature flag")
    void shouldCheckMentorshipProgramFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.MENTORSHIP_PROGRAM)).thenReturn(false);

        // Act
        boolean result = featureFlagUtils.isMentorshipProgramEnabled();

        // Assert
        assertFalse(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.MENTORSHIP_PROGRAM);
    }

    @Test
    @DisplayName("Should check event analytics feature flag")
    void shouldCheckEventAnalyticsFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.EVENT_ANALYTICS)).thenReturn(true);

        // Act
        boolean result = featureFlagUtils.isEventAnalyticsEnabled();

        // Assert
        assertTrue(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.EVENT_ANALYTICS);
    }

    @Test
    @DisplayName("Should check event templates feature flag")
    void shouldCheckEventTemplatesFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.EVENT_TEMPLATES)).thenReturn(true);

        // Act
        boolean result = featureFlagUtils.isEventTemplatesEnabled();

        // Assert
        assertTrue(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.EVENT_TEMPLATES);
    }

    @Test
    @DisplayName("Should check recurring events feature flag")
    void shouldCheckRecurringEventsFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.RECURRING_EVENTS)).thenReturn(false);

        // Act
        boolean result = featureFlagUtils.isRecurringEventsEnabled();

        // Assert
        assertFalse(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.RECURRING_EVENTS);
    }

    @Test
    @DisplayName("Should check event media feature flag")
    void shouldCheckEventMediaFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.EVENT_MEDIA)).thenReturn(true);

        // Act
        boolean result = featureFlagUtils.isEventMediaEnabled();

        // Assert
        assertTrue(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.EVENT_MEDIA);
    }

    @Test
    @DisplayName("Should check event comments feature flag")
    void shouldCheckEventCommentsFeatureFlag() {
        // Arrange
        when(featureFlagService.isFeatureEnabled(FeatureFlagUtils.EVENT_COMMENTS)).thenReturn(true);

        // Act
        boolean result = featureFlagUtils.isEventCommentsEnabled();

        // Assert
        assertTrue(result);
        verify(featureFlagService).isFeatureEnabled(FeatureFlagUtils.EVENT_COMMENTS);
    }

    @Test
    @DisplayName("Should execute function when feature is enabled")
    void shouldExecuteFunctionWhenFeatureIsEnabled() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE")).thenReturn(true);

        // Act
        String result = featureFlagUtils.executeIfEnabled("TEST_FEATURE", () -> "Success");

        // Assert
        assertEquals("Success", result);
        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should return null when feature is disabled")
    void shouldReturnNullWhenFeatureIsDisabled() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE")).thenReturn(false);

        // Act
        String result = featureFlagUtils.executeIfEnabled("TEST_FEATURE", () -> "Success");

        // Assert
        assertNull(result);
        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should return null when function throws exception")
    void shouldReturnNullWhenFunctionThrowsException() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE")).thenReturn(true);

        // Act
        String result = featureFlagUtils.executeIfEnabled("TEST_FEATURE", () -> {
            throw new RuntimeException("Function error");
        });

        // Assert
        assertNull(result);
        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should execute runnable when feature is enabled")
    void shouldExecuteRunnableWhenFeatureIsEnabled() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE")).thenReturn(true);
        boolean[] executed = {false};

        // Act
        featureFlagUtils.executeIfEnabled("TEST_FEATURE", () -> executed[0] = true);

        // Assert
        assertTrue(executed[0]);
        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should not execute runnable when feature is disabled")
    void shouldNotExecuteRunnableWhenFeatureIsDisabled() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE")).thenReturn(false);
        boolean[] executed = {false};

        // Act
        featureFlagUtils.executeIfEnabled("TEST_FEATURE", () -> executed[0] = true);

        // Assert
        assertFalse(executed[0]);
        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should handle runnable exception gracefully")
    void shouldHandleRunnableExceptionGracefully() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE")).thenReturn(true);

        // Act & Assert (should not throw exception)
        assertDoesNotThrow(() -> {
            featureFlagUtils.executeIfEnabled("TEST_FEATURE", () -> {
                throw new RuntimeException("Runnable error");
            });
        });

        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should return default value when feature is disabled")
    void shouldReturnDefaultValueWhenFeatureIsDisabled() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE")).thenReturn(false);

        // Act
        String result = featureFlagUtils.getDefaultIfDisabled("TEST_FEATURE", "Default Value");

        // Assert
        assertEquals("Default Value", result);
        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should return null when feature is enabled")
    void shouldReturnNullWhenFeatureIsEnabled() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE")).thenReturn(true);

        // Act
        String result = featureFlagUtils.getDefaultIfDisabled("TEST_FEATURE", "Default Value");

        // Assert
        assertNull(result);
        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should log feature usage when feature is enabled")
    void shouldLogFeatureUsageWhenFeatureIsEnabled() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE")).thenReturn(true);

        // Act
        featureFlagUtils.logFeatureUsage("TEST_FEATURE", "testuser");

        // Assert
        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should not log feature usage when feature is disabled")
    void shouldNotLogFeatureUsageWhenFeatureIsDisabled() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE")).thenReturn(false);

        // Act
        featureFlagUtils.logFeatureUsage("TEST_FEATURE", "testuser");

        // Assert
        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should handle null user ID in log feature usage")
    void shouldHandleNullUserIdInLogFeatureUsage() {
        // Arrange
        when(featureFlagService.isFeatureEnabled("TEST_FEATURE")).thenReturn(true);

        // Act & Assert (should not throw exception)
        assertDoesNotThrow(() -> {
            featureFlagUtils.logFeatureUsage("TEST_FEATURE", null);
        });

        verify(featureFlagService).isFeatureEnabled("TEST_FEATURE");
    }

    @Test
    @DisplayName("Should handle null feature name gracefully")
    void shouldHandleNullFeatureNameGracefully() {
        // Act
        boolean result = featureFlagUtils.isFeatureEnabled(null);

        // Assert
        assertFalse(result);
        verify(featureFlagService).isFeatureEnabled(null);
    }

    @Test
    @DisplayName("Should handle empty feature name gracefully")
    void shouldHandleEmptyFeatureNameGracefully() {
        // Act
        boolean result = featureFlagUtils.isFeatureEnabled("");

        // Assert
        assertFalse(result);
        verify(featureFlagService).isFeatureEnabled("");
    }
}

