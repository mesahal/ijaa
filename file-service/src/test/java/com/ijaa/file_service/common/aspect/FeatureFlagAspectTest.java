package com.ijaa.file_service.common.aspect;

import com.ijaa.file_service.common.annotation.RequiresFeature;
import com.ijaa.file_service.common.exceptions.FeatureDisabledException;
import com.ijaa.file_service.config.FeatureFlagUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureFlagAspectTest {

    @Mock
    private FeatureFlagUtils featureFlagUtils;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Signature signature;

    private FeatureFlagAspect featureFlagAspect;

    @BeforeEach
    void setUp() {
        featureFlagAspect = new FeatureFlagAspect(featureFlagUtils);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("testMethod");
    }

    @Test
    void checkFeatureFlag_WhenFeatureIsEnabled_AllowsExecution() throws Throwable {
        // Given
        String featureName = "file-upload.profile-photo";
        RequiresFeature requiresFeature = mock(RequiresFeature.class);
        when(requiresFeature.value()).thenReturn(featureName);
        when(requiresFeature.message()).thenReturn("");
        
        when(featureFlagUtils.isFeatureEnabled(featureName)).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> {
            featureFlagAspect.checkFeatureFlag(joinPoint, requiresFeature);
        });
        
        verify(featureFlagUtils).isFeatureEnabled(featureName);
    }

    @Test
    void checkFeatureFlag_WhenFeatureIsDisabled_ThrowsFeatureDisabledException() {
        // Given
        String featureName = "file-upload.profile-photo";
        RequiresFeature requiresFeature = mock(RequiresFeature.class);
        when(requiresFeature.value()).thenReturn(featureName);
        when(requiresFeature.message()).thenReturn("");
        
        when(featureFlagUtils.isFeatureEnabled(featureName)).thenReturn(false);

        // When & Then
        FeatureDisabledException exception = assertThrows(FeatureDisabledException.class, () -> {
            featureFlagAspect.checkFeatureFlag(joinPoint, requiresFeature);
        });
        
        assertEquals("Feature 'file-upload.profile-photo' is disabled", exception.getMessage());
        assertEquals(featureName, exception.getFeatureName());
        verify(featureFlagUtils).isFeatureEnabled(featureName);
    }

    @Test
    void checkFeatureFlag_WithCustomMessage_ThrowsExceptionWithCustomMessage() {
        // Given
        String featureName = "file-upload.profile-photo";
        String customMessage = "Custom error message";
        RequiresFeature requiresFeature = mock(RequiresFeature.class);
        when(requiresFeature.value()).thenReturn(featureName);
        when(requiresFeature.message()).thenReturn(customMessage);
        
        when(featureFlagUtils.isFeatureEnabled(featureName)).thenReturn(false);

        // When & Then
        FeatureDisabledException exception = assertThrows(FeatureDisabledException.class, () -> {
            featureFlagAspect.checkFeatureFlag(joinPoint, requiresFeature);
        });
        
        assertEquals(customMessage, exception.getMessage());
        assertEquals(featureName, exception.getFeatureName());
        verify(featureFlagUtils).isFeatureEnabled(featureName);
    }

    @Test
    void checkFeatureFlag_WithEmptyCustomMessage_UsesDefaultMessage() {
        // Given
        String featureName = "file-upload.profile-photo";
        RequiresFeature requiresFeature = mock(RequiresFeature.class);
        when(requiresFeature.value()).thenReturn(featureName);
        when(requiresFeature.message()).thenReturn("");
        
        when(featureFlagUtils.isFeatureEnabled(featureName)).thenReturn(false);

        // When & Then
        FeatureDisabledException exception = assertThrows(FeatureDisabledException.class, () -> {
            featureFlagAspect.checkFeatureFlag(joinPoint, requiresFeature);
        });
        
        assertEquals("Feature 'file-upload.profile-photo' is disabled", exception.getMessage());
        assertEquals(featureName, exception.getFeatureName());
        verify(featureFlagUtils).isFeatureEnabled(featureName);
    }
}
