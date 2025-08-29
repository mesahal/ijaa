package com.ijaa.user.common.aspect;

import com.ijaa.user.common.annotation.RequiresFeature;
import com.ijaa.user.common.exceptions.FeatureDisabledException;
import com.ijaa.user.service.FeatureFlagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureFlagAspectTest {

    @Mock
    private FeatureFlagService featureFlagService;

    @InjectMocks
    private FeatureFlagAspect featureFlagAspect;

    private TestService testService;

    @BeforeEach
    void setUp() {
        testService = new TestService();
    }

    @Test
    void testCheckFeatureFlag_WhenFeatureEnabled_AllowsExecution() throws Throwable {
        // Given
        when(featureFlagService.isEnabled("chat")).thenReturn(true);
        
        // Create a mock join point
        var joinPoint = mock(org.aspectj.lang.JoinPoint.class);
        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.getSignature().getName()).thenReturn("testMethod");

        // Create annotation
        RequiresFeature annotation = createAnnotation("chat", "");

        // When & Then
        assertDoesNotThrow(() -> featureFlagAspect.checkFeatureFlag(joinPoint, annotation));
        verify(featureFlagService).isEnabled("chat");
    }

    @Test
    void testCheckFeatureFlag_WhenFeatureDisabled_ThrowsException() {
        // Given
        when(featureFlagService.isEnabled("chat")).thenReturn(false);
        
        // Create a mock join point
        var joinPoint = mock(org.aspectj.lang.JoinPoint.class);
        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.getSignature().getName()).thenReturn("testMethod");

        // Create annotation
        RequiresFeature annotation = createAnnotation("chat", "");

        // When & Then
        FeatureDisabledException exception = assertThrows(
            FeatureDisabledException.class,
            () -> featureFlagAspect.checkFeatureFlag(joinPoint, annotation)
        );
        
        assertEquals("Feature 'chat' is disabled", exception.getMessage());
        assertEquals("chat", exception.getFeatureName());
        verify(featureFlagService).isEnabled("chat");
    }

    @Test
    void testCheckFeatureFlag_WhenFeatureDisabledWithCustomMessage_ThrowsExceptionWithCustomMessage() {
        // Given
        when(featureFlagService.isEnabled("chat")).thenReturn(false);
        
        // Create a mock join point
        var joinPoint = mock(org.aspectj.lang.JoinPoint.class);
        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.getSignature().getName()).thenReturn("testMethod");

        // Create annotation with custom message
        RequiresFeature annotation = createAnnotation("chat", "Custom error message");

        // When & Then
        FeatureDisabledException exception = assertThrows(
            FeatureDisabledException.class,
            () -> featureFlagAspect.checkFeatureFlag(joinPoint, annotation)
        );
        
        assertEquals("Custom error message", exception.getMessage());
        assertEquals("chat", exception.getFeatureName());
    }

    private RequiresFeature createAnnotation(String value, String message) {
        return new RequiresFeature() {
            @Override
            public String value() {
                return value;
            }

            @Override
            public String message() {
                return message;
            }

            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return RequiresFeature.class;
            }
        };
    }

    // Test service class for testing
    public static class TestService {
        @RequiresFeature("chat")
        public void testMethod() {
            // Test method
        }
    }
}


