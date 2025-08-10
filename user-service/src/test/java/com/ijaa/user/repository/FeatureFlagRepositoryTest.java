package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.FeatureFlag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Repository tests for FeatureFlagRepository
 * Tests database operations and queries
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Feature Flag Repository Tests")
class FeatureFlagRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    private FeatureFlag testFeatureFlag;
    private static final String TEST_FEATURE_NAME = "TEST_FEATURE";
    private static final String TEST_DESCRIPTION = "Test feature for repository testing";

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        featureFlagRepository.deleteAll();
        
        // Create test feature flag
        testFeatureFlag = TestDataBuilder.createTestFeatureFlag();
        testFeatureFlag.setFeatureName(TEST_FEATURE_NAME);
        testFeatureFlag.setDescription(TEST_DESCRIPTION);
    }

    @Test
    @DisplayName("Should save feature flag successfully")
    void shouldSaveFeatureFlag() {
        // Act
        FeatureFlag savedFlag = featureFlagRepository.save(testFeatureFlag);

        // Assert
        assertNotNull(savedFlag);
        assertNotNull(savedFlag.getId());
        assertEquals(TEST_FEATURE_NAME, savedFlag.getFeatureName());
        assertEquals(TEST_DESCRIPTION, savedFlag.getDescription());
        assertFalse(savedFlag.getEnabled()); // Should default to disabled
        assertNotNull(savedFlag.getCreatedAt());
        assertNotNull(savedFlag.getUpdatedAt());
    }

    @Test
    @DisplayName("Should find feature flag by name successfully")
    void shouldFindFeatureFlagByName() {
        // Arrange
        featureFlagRepository.save(testFeatureFlag);

        // Act
        Optional<FeatureFlag> found = featureFlagRepository.findByFeatureName(TEST_FEATURE_NAME);

        // Assert
        assertTrue(found.isPresent());
        assertEquals(TEST_FEATURE_NAME, found.get().getFeatureName());
        assertEquals(TEST_DESCRIPTION, found.get().getDescription());
    }

    @Test
    @DisplayName("Should return empty when feature flag not found by name")
    void shouldReturnEmptyWhenFeatureFlagNotFoundByName() {
        // Act
        Optional<FeatureFlag> found = featureFlagRepository.findByFeatureName("NON_EXISTENT_FEATURE");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should check if feature flag exists by name")
    void shouldCheckIfFeatureFlagExistsByName() {
        // Arrange
        featureFlagRepository.save(testFeatureFlag);

        // Act
        boolean exists = featureFlagRepository.existsByFeatureName(TEST_FEATURE_NAME);
        boolean notExists = featureFlagRepository.existsByFeatureName("NON_EXISTENT_FEATURE");

        // Assert
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    @DisplayName("Should find all enabled feature flags")
    void shouldFindAllEnabledFeatureFlags() {
        // Arrange
        FeatureFlag enabledFlag1 = TestDataBuilder.createTestFeatureFlag();
        enabledFlag1.setFeatureName("ENABLED_FEATURE_1");
        enabledFlag1.setEnabled(true);

        FeatureFlag enabledFlag2 = TestDataBuilder.createTestFeatureFlag();
        enabledFlag2.setFeatureName("ENABLED_FEATURE_2");
        enabledFlag2.setEnabled(true);

        FeatureFlag disabledFlag = TestDataBuilder.createTestFeatureFlag();
        disabledFlag.setFeatureName("DISABLED_FEATURE");
        disabledFlag.setEnabled(false);

        featureFlagRepository.saveAll(Arrays.asList(enabledFlag1, enabledFlag2, disabledFlag));

        // Act
        List<FeatureFlag> enabledFlags = featureFlagRepository.findByEnabledTrue();

        // Assert
        assertEquals(2, enabledFlags.size());
        assertTrue(enabledFlags.stream().allMatch(FeatureFlag::getEnabled));
        assertTrue(enabledFlags.stream().anyMatch(flag -> flag.getFeatureName().equals("ENABLED_FEATURE_1")));
        assertTrue(enabledFlags.stream().anyMatch(flag -> flag.getFeatureName().equals("ENABLED_FEATURE_2")));
    }

    @Test
    @DisplayName("Should find all disabled feature flags")
    void shouldFindAllDisabledFeatureFlags() {
        // Arrange
        FeatureFlag enabledFlag = TestDataBuilder.createTestFeatureFlag();
        enabledFlag.setFeatureName("ENABLED_FEATURE");
        enabledFlag.setEnabled(true);

        FeatureFlag disabledFlag1 = TestDataBuilder.createTestFeatureFlag();
        disabledFlag1.setFeatureName("DISABLED_FEATURE_1");
        disabledFlag1.setEnabled(false);

        FeatureFlag disabledFlag2 = TestDataBuilder.createTestFeatureFlag();
        disabledFlag2.setFeatureName("DISABLED_FEATURE_2");
        disabledFlag2.setEnabled(false);

        featureFlagRepository.saveAll(Arrays.asList(enabledFlag, disabledFlag1, disabledFlag2));

        // Act
        List<FeatureFlag> disabledFlags = featureFlagRepository.findByEnabledFalse();

        // Assert
        assertEquals(2, disabledFlags.size());
        assertTrue(disabledFlags.stream().noneMatch(FeatureFlag::getEnabled));
        assertTrue(disabledFlags.stream().anyMatch(flag -> flag.getFeatureName().equals("DISABLED_FEATURE_1")));
        assertTrue(disabledFlags.stream().anyMatch(flag -> flag.getFeatureName().equals("DISABLED_FEATURE_2")));
    }

    @Test
    @DisplayName("Should update feature flag successfully")
    void shouldUpdateFeatureFlag() {
        // Arrange
        FeatureFlag savedFlag = featureFlagRepository.save(testFeatureFlag);
        savedFlag.setEnabled(true);
        savedFlag.setDescription("Updated description");

        // Act
        FeatureFlag updatedFlag = featureFlagRepository.save(savedFlag);

        // Assert
        assertNotNull(updatedFlag);
        assertEquals(savedFlag.getId(), updatedFlag.getId());
        assertTrue(updatedFlag.getEnabled());
        assertEquals("Updated description", updatedFlag.getDescription());
        assertNotNull(updatedFlag.getUpdatedAt());
    }

    @Test
    @DisplayName("Should delete feature flag successfully")
    void shouldDeleteFeatureFlag() {
        // Arrange
        FeatureFlag savedFlag = featureFlagRepository.save(testFeatureFlag);

        // Act
        featureFlagRepository.delete(savedFlag);

        // Assert
        Optional<FeatureFlag> found = featureFlagRepository.findByFeatureName(TEST_FEATURE_NAME);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should handle unique constraint on feature name")
    void shouldHandleUniqueConstraintOnFeatureName() {
        // Arrange
        featureFlagRepository.save(testFeatureFlag);

        // Create another feature flag with the same name
        FeatureFlag duplicateFlag = TestDataBuilder.createTestFeatureFlag();
        duplicateFlag.setFeatureName(TEST_FEATURE_NAME);
        duplicateFlag.setDescription("Different description");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            featureFlagRepository.save(duplicateFlag);
        });
    }

    @Test
    @DisplayName("Should handle null feature name")
    void shouldHandleNullFeatureName() {
        // Arrange
        testFeatureFlag.setFeatureName(null);

        // Act & Assert
        assertThrows(Exception.class, () -> {
            featureFlagRepository.save(testFeatureFlag);
        });
    }

    @Test
    @DisplayName("Should handle empty feature name")
    void shouldHandleEmptyFeatureName() {
        // Arrange
        testFeatureFlag.setFeatureName("");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            featureFlagRepository.save(testFeatureFlag);
        });
    }

    @Test
    @DisplayName("Should handle long feature name")
    void shouldHandleLongFeatureName() {
        // Arrange
        testFeatureFlag.setFeatureName("A".repeat(101)); // Exceeds 100 character limit

        // Act & Assert
        assertThrows(Exception.class, () -> {
            featureFlagRepository.save(testFeatureFlag);
        });
    }

    @Test
    @DisplayName("Should handle long description")
    void shouldHandleLongDescription() {
        // Arrange
        testFeatureFlag.setDescription("A".repeat(501)); // Exceeds 500 character limit

        // Act & Assert
        assertThrows(Exception.class, () -> {
            featureFlagRepository.save(testFeatureFlag);
        });
    }

    @Test
    @DisplayName("Should set timestamps automatically")
    void shouldSetTimestampsAutomatically() {
        // Act
        FeatureFlag savedFlag = featureFlagRepository.save(testFeatureFlag);

        // Assert
        assertNotNull(savedFlag.getCreatedAt());
        assertNotNull(savedFlag.getUpdatedAt());
        assertEquals(savedFlag.getCreatedAt(), savedFlag.getUpdatedAt()); // Should be same on creation
    }

    @Test
    @DisplayName("Should update timestamp on modification")
    void shouldUpdateTimestampOnModification() throws InterruptedException {
        // Arrange
        FeatureFlag savedFlag = featureFlagRepository.save(testFeatureFlag);
        LocalDateTime originalUpdatedAt = savedFlag.getUpdatedAt();

        // Wait a bit to ensure timestamp difference
        Thread.sleep(10);

        // Act
        savedFlag.setDescription("Updated description");
        FeatureFlag updatedFlag = featureFlagRepository.save(savedFlag);

        // Assert
        assertNotNull(updatedFlag.getUpdatedAt());
        assertTrue(updatedFlag.getUpdatedAt().isAfter(originalUpdatedAt));
        assertEquals(savedFlag.getCreatedAt(), updatedFlag.getCreatedAt()); // Should remain unchanged
    }

    @Test
    @DisplayName("Should find all feature flags")
    void shouldFindAllFeatureFlags() {
        // Arrange
        FeatureFlag flag1 = TestDataBuilder.createTestFeatureFlag();
        flag1.setFeatureName("FEATURE_1");

        FeatureFlag flag2 = TestDataBuilder.createTestFeatureFlag();
        flag2.setFeatureName("FEATURE_2");

        featureFlagRepository.saveAll(Arrays.asList(flag1, flag2));

        // Act
        List<FeatureFlag> allFlags = featureFlagRepository.findAll();

        // Assert
        assertEquals(2, allFlags.size());
        assertTrue(allFlags.stream().anyMatch(flag -> flag.getFeatureName().equals("FEATURE_1")));
        assertTrue(allFlags.stream().anyMatch(flag -> flag.getFeatureName().equals("FEATURE_2")));
    }

    @Test
    @DisplayName("Should find feature flag by ID")
    void shouldFindFeatureFlagById() {
        // Arrange
        FeatureFlag savedFlag = featureFlagRepository.save(testFeatureFlag);

        // Act
        Optional<FeatureFlag> found = featureFlagRepository.findById(savedFlag.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(TEST_FEATURE_NAME, found.get().getFeatureName());
    }

    @Test
    @DisplayName("Should return empty when feature flag not found by ID")
    void shouldReturnEmptyWhenFeatureFlagNotFoundById() {
        // Act
        Optional<FeatureFlag> found = featureFlagRepository.findById(999L);

        // Assert
        assertFalse(found.isPresent());
    }
}

