package com.ijaa.user.service;

import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.repository.FeatureFlagRepository;
import com.ijaa.user.service.impl.FeatureFlagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for FeatureFlagService
 * Tests all CRUD operations and edge cases
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Feature Flag Service Tests")
class FeatureFlagServiceTest {

    @Mock
    private FeatureFlagRepository featureFlagRepository;

    @InjectMocks
    private FeatureFlagServiceImpl featureFlagService;

    private FeatureFlag testFeatureFlag;
    private static final String TEST_FEATURE_NAME = "TEST_FEATURE";
    private static final String TEST_DESCRIPTION = "Test feature for unit testing";

    @BeforeEach
    void setUp() {
        testFeatureFlag = TestDataBuilder.createTestFeatureFlag();
    }

    @Test
    @DisplayName("Should get all feature flags successfully")
    void shouldGetAllFeatureFlags() {
        // Arrange
        List<FeatureFlag> expectedFlags = Arrays.asList(
            TestDataBuilder.createTestFeatureFlag(),
            TestDataBuilder.createTestFeatureFlag()
        );
        when(featureFlagRepository.findAll()).thenReturn(expectedFlags);

        // Act
        List<FeatureFlag> result = featureFlagService.getAllFeatureFlags();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(featureFlagRepository).findAll();
    }

    @Test
    @DisplayName("Should get feature flag by name successfully")
    void shouldGetFeatureFlagByName() {
        // Arrange
        when(featureFlagRepository.findByFeatureName(TEST_FEATURE_NAME))
            .thenReturn(Optional.of(testFeatureFlag));

        // Act
        FeatureFlag result = featureFlagService.getFeatureFlag(TEST_FEATURE_NAME);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_FEATURE_NAME, result.getFeatureName());
        verify(featureFlagRepository).findByFeatureName(TEST_FEATURE_NAME);
    }

    @Test
    @DisplayName("Should return null when feature flag not found")
    void shouldReturnNullWhenFeatureFlagNotFound() {
        // Arrange
        when(featureFlagRepository.findByFeatureName(TEST_FEATURE_NAME))
            .thenReturn(Optional.empty());

        // Act
        FeatureFlag result = featureFlagService.getFeatureFlag(TEST_FEATURE_NAME);

        // Assert
        assertNull(result);
        verify(featureFlagRepository).findByFeatureName(TEST_FEATURE_NAME);
    }

    @Test
    @DisplayName("Should create feature flag successfully")
    void shouldCreateFeatureFlag() {
        // Arrange
        when(featureFlagRepository.existsByFeatureName(TEST_FEATURE_NAME)).thenReturn(false);
        when(featureFlagRepository.save(any(FeatureFlag.class))).thenReturn(testFeatureFlag);

        // Act
        FeatureFlag result = featureFlagService.createFeatureFlag(TEST_FEATURE_NAME, TEST_DESCRIPTION);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_FEATURE_NAME, result.getFeatureName());
        assertEquals(TEST_DESCRIPTION, result.getDescription());
        assertFalse(result.getEnabled()); // Should default to disabled
        verify(featureFlagRepository).existsByFeatureName(TEST_FEATURE_NAME);
        verify(featureFlagRepository).save(any(FeatureFlag.class));
    }

    @Test
    @DisplayName("Should throw exception when creating duplicate feature flag")
    void shouldThrowExceptionWhenCreatingDuplicateFeatureFlag() {
        // Arrange
        when(featureFlagRepository.existsByFeatureName(TEST_FEATURE_NAME)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            featureFlagService.createFeatureFlag(TEST_FEATURE_NAME, TEST_DESCRIPTION);
        });

        assertEquals("Feature flag with name " + TEST_FEATURE_NAME + " already exists", exception.getMessage());
        verify(featureFlagRepository).existsByFeatureName(TEST_FEATURE_NAME);
        verify(featureFlagRepository, never()).save(any(FeatureFlag.class));
    }

    @Test
    @DisplayName("Should update feature flag successfully")
    void shouldUpdateFeatureFlag() {
        // Arrange
        FeatureFlag existingFlag = TestDataBuilder.createTestFeatureFlag();
        existingFlag.setEnabled(false);
        
        when(featureFlagRepository.findByFeatureName(TEST_FEATURE_NAME))
            .thenReturn(Optional.of(existingFlag));
        when(featureFlagRepository.save(any(FeatureFlag.class))).thenReturn(existingFlag);

        // Act
        FeatureFlag result = featureFlagService.updateFeatureFlag(TEST_FEATURE_NAME, true);

        // Assert
        assertNotNull(result);
        assertTrue(result.getEnabled());
        verify(featureFlagRepository).findByFeatureName(TEST_FEATURE_NAME);
        verify(featureFlagRepository).save(existingFlag);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent feature flag")
    void shouldThrowExceptionWhenUpdatingNonExistentFeatureFlag() {
        // Arrange
        when(featureFlagRepository.findByFeatureName(TEST_FEATURE_NAME))
            .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            featureFlagService.updateFeatureFlag(TEST_FEATURE_NAME, true);
        });

        assertEquals("Feature flag not found: " + TEST_FEATURE_NAME, exception.getMessage());
        verify(featureFlagRepository).findByFeatureName(TEST_FEATURE_NAME);
        verify(featureFlagRepository, never()).save(any(FeatureFlag.class));
    }

    @Test
    @DisplayName("Should delete feature flag successfully")
    void shouldDeleteFeatureFlag() {
        // Arrange
        when(featureFlagRepository.findByFeatureName(TEST_FEATURE_NAME))
            .thenReturn(Optional.of(testFeatureFlag));
        doNothing().when(featureFlagRepository).delete(testFeatureFlag);

        // Act
        featureFlagService.deleteFeatureFlag(TEST_FEATURE_NAME);

        // Assert
        verify(featureFlagRepository).findByFeatureName(TEST_FEATURE_NAME);
        verify(featureFlagRepository).delete(testFeatureFlag);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent feature flag")
    void shouldThrowExceptionWhenDeletingNonExistentFeatureFlag() {
        // Arrange
        when(featureFlagRepository.findByFeatureName(TEST_FEATURE_NAME))
            .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            featureFlagService.deleteFeatureFlag(TEST_FEATURE_NAME);
        });

        assertEquals("Feature flag not found: " + TEST_FEATURE_NAME, exception.getMessage());
        verify(featureFlagRepository).findByFeatureName(TEST_FEATURE_NAME);
        verify(featureFlagRepository, never()).delete(any(FeatureFlag.class));
    }

    @Test
    @DisplayName("Should return true when feature flag is enabled")
    void shouldReturnTrueWhenFeatureFlagIsEnabled() {
        // Arrange
        testFeatureFlag.setEnabled(true);
        when(featureFlagRepository.findByFeatureName(TEST_FEATURE_NAME))
            .thenReturn(Optional.of(testFeatureFlag));

        // Act
        boolean result = featureFlagService.isFeatureEnabled(TEST_FEATURE_NAME);

        // Assert
        assertTrue(result);
        verify(featureFlagRepository).findByFeatureName(TEST_FEATURE_NAME);
    }

    @Test
    @DisplayName("Should return false when feature flag is disabled")
    void shouldReturnFalseWhenFeatureFlagIsDisabled() {
        // Arrange
        testFeatureFlag.setEnabled(false);
        when(featureFlagRepository.findByFeatureName(TEST_FEATURE_NAME))
            .thenReturn(Optional.of(testFeatureFlag));

        // Act
        boolean result = featureFlagService.isFeatureEnabled(TEST_FEATURE_NAME);

        // Assert
        assertFalse(result);
        verify(featureFlagRepository).findByFeatureName(TEST_FEATURE_NAME);
    }

    @Test
    @DisplayName("Should return false when feature flag does not exist")
    void shouldReturnFalseWhenFeatureFlagDoesNotExist() {
        // Arrange
        when(featureFlagRepository.findByFeatureName(TEST_FEATURE_NAME))
            .thenReturn(Optional.empty());

        // Act
        boolean result = featureFlagService.isFeatureEnabled(TEST_FEATURE_NAME);

        // Assert
        assertFalse(result);
        verify(featureFlagRepository).findByFeatureName(TEST_FEATURE_NAME);
    }

    @Test
    @DisplayName("Should handle null feature name gracefully")
    void shouldHandleNullFeatureName() {
        // Act
        boolean result = featureFlagService.isFeatureEnabled(null);

        // Assert
        assertFalse(result);
        verify(featureFlagRepository).findByFeatureName(null);
    }

    @Test
    @DisplayName("Should handle empty feature name gracefully")
    void shouldHandleEmptyFeatureName() {
        // Act
        boolean result = featureFlagService.isFeatureEnabled("");

        // Assert
        assertFalse(result);
        verify(featureFlagRepository).findByFeatureName("");
    }

    @Test
    @DisplayName("Should create feature flag with proper timestamps")
    void shouldCreateFeatureFlagWithProperTimestamps() {
        // Arrange
        when(featureFlagRepository.existsByFeatureName(TEST_FEATURE_NAME)).thenReturn(false);
        when(featureFlagRepository.save(any(FeatureFlag.class))).thenAnswer(invocation -> {
            FeatureFlag flag = invocation.getArgument(0);
            flag.setCreatedAt(LocalDateTime.now());
            flag.setUpdatedAt(LocalDateTime.now());
            return flag;
        });

        // Act
        FeatureFlag result = featureFlagService.createFeatureFlag(TEST_FEATURE_NAME, TEST_DESCRIPTION);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
        assertEquals(result.getCreatedAt(), result.getUpdatedAt()); // Should be same on creation
    }

    @Test
    @DisplayName("Should update feature flag with proper timestamp")
    void shouldUpdateFeatureFlagWithProperTimestamp() {
        // Arrange
        LocalDateTime originalTime = LocalDateTime.now().minusDays(1);
        FeatureFlag existingFlag = TestDataBuilder.createTestFeatureFlag();
        existingFlag.setCreatedAt(originalTime);
        existingFlag.setUpdatedAt(originalTime);
        
        when(featureFlagRepository.findByFeatureName(TEST_FEATURE_NAME))
            .thenReturn(Optional.of(existingFlag));
        when(featureFlagRepository.save(any(FeatureFlag.class))).thenAnswer(invocation -> {
            FeatureFlag flag = invocation.getArgument(0);
            flag.setUpdatedAt(LocalDateTime.now());
            return flag;
        });

        // Act
        FeatureFlag result = featureFlagService.updateFeatureFlag(TEST_FEATURE_NAME, true);

        // Assert
        assertNotNull(result);
        assertEquals(originalTime, result.getCreatedAt()); // Should remain unchanged
        assertNotEquals(originalTime, result.getUpdatedAt()); // Should be updated
        verify(featureFlagRepository).save(existingFlag);
    }
}

