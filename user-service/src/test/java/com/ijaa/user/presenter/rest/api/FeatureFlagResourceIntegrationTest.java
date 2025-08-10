package com.ijaa.user.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.service.FeatureFlagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for FeatureFlagResource
 * Tests all REST endpoints with proper authentication and authorization
 */
@WebMvcTest(FeatureFlagResource.class)
@DisplayName("Feature Flag Resource Integration Tests")
class FeatureFlagResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeatureFlagService featureFlagService;

    @Autowired
    private ObjectMapper objectMapper;

    private FeatureFlag testFeatureFlag;
    private static final String TEST_FEATURE_NAME = "TEST_FEATURE";
    private static final String TEST_DESCRIPTION = "Test feature for integration testing";

    @BeforeEach
    void setUp() {
        testFeatureFlag = TestDataBuilder.createTestFeatureFlag();
    }

    @Test
    @DisplayName("Should get all feature flags successfully")
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllFeatureFlags() throws Exception {
        // Arrange
        List<FeatureFlag> expectedFlags = Arrays.asList(
            TestDataBuilder.createTestFeatureFlag(),
            TestDataBuilder.createTestFeatureFlag()
        );
        when(featureFlagService.getAllFeatureFlags()).thenReturn(expectedFlags);

        // Act & Assert
        mockMvc.perform(get("/api/v1/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feature flags retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));

        verify(featureFlagService).getAllFeatureFlags();
    }

    @Test
    @DisplayName("Should get feature flag by name successfully")
    @WithMockUser(roles = "ADMIN")
    void shouldGetFeatureFlagByName() throws Exception {
        // Arrange
        when(featureFlagService.getFeatureFlag(TEST_FEATURE_NAME)).thenReturn(testFeatureFlag);

        // Act & Assert
        mockMvc.perform(get("/api/v1/admin/feature-flags/{featureName}", TEST_FEATURE_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feature flag retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.featureName").value(TEST_FEATURE_NAME))
                .andExpect(jsonPath("$.data.description").value(TEST_DESCRIPTION));

        verify(featureFlagService).getFeatureFlag(TEST_FEATURE_NAME);
    }

    @Test
    @DisplayName("Should return 404 when feature flag not found")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenFeatureFlagNotFound() throws Exception {
        // Arrange
        when(featureFlagService.getFeatureFlag(TEST_FEATURE_NAME)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/v1/admin/feature-flags/{featureName}", TEST_FEATURE_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(featureFlagService).getFeatureFlag(TEST_FEATURE_NAME);
    }

    @Test
    @DisplayName("Should create feature flag successfully")
    @WithMockUser(roles = "ADMIN")
    void shouldCreateFeatureFlag() throws Exception {
        // Arrange
        FeatureFlagResource.FeatureFlagRequest request = new FeatureFlagResource.FeatureFlagRequest();
        request.setFeatureName(TEST_FEATURE_NAME);
        request.setDescription(TEST_DESCRIPTION);

        when(featureFlagService.createFeatureFlag(TEST_FEATURE_NAME, TEST_DESCRIPTION))
            .thenReturn(testFeatureFlag);

        // Act & Assert
        mockMvc.perform(post("/api/v1/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Feature flag created successfully"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.data.featureName").value(TEST_FEATURE_NAME));

        verify(featureFlagService).createFeatureFlag(TEST_FEATURE_NAME, TEST_DESCRIPTION);
    }

    @Test
    @DisplayName("Should return 400 when creating duplicate feature flag")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn400WhenCreatingDuplicateFeatureFlag() throws Exception {
        // Arrange
        FeatureFlagResource.FeatureFlagRequest request = new FeatureFlagResource.FeatureFlagRequest();
        request.setFeatureName(TEST_FEATURE_NAME);
        request.setDescription(TEST_DESCRIPTION);

        when(featureFlagService.createFeatureFlag(TEST_FEATURE_NAME, TEST_DESCRIPTION))
            .thenThrow(new RuntimeException("Feature flag with name " + TEST_FEATURE_NAME + " already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Feature flag with name " + TEST_FEATURE_NAME + " already exists"))
                .andExpect(jsonPath("$.code").value("400"));

        verify(featureFlagService).createFeatureFlag(TEST_FEATURE_NAME, TEST_DESCRIPTION);
    }

    @Test
    @DisplayName("Should update feature flag successfully")
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateFeatureFlag() throws Exception {
        // Arrange
        FeatureFlagResource.FeatureFlagUpdateRequest request = new FeatureFlagResource.FeatureFlagUpdateRequest();
        request.setEnabled(true);

        testFeatureFlag.setEnabled(true);
        when(featureFlagService.updateFeatureFlag(TEST_FEATURE_NAME, true))
            .thenReturn(testFeatureFlag);

        // Act & Assert
        mockMvc.perform(put("/api/v1/admin/feature-flags/{featureName}", TEST_FEATURE_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feature flag updated successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.enabled").value(true));

        verify(featureFlagService).updateFeatureFlag(TEST_FEATURE_NAME, true);
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent feature flag")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenUpdatingNonExistentFeatureFlag() throws Exception {
        // Arrange
        FeatureFlagResource.FeatureFlagUpdateRequest request = new FeatureFlagResource.FeatureFlagUpdateRequest();
        request.setEnabled(true);

        when(featureFlagService.updateFeatureFlag(TEST_FEATURE_NAME, true))
            .thenThrow(new RuntimeException("Feature flag not found: " + TEST_FEATURE_NAME));

        // Act & Assert
        mockMvc.perform(put("/api/v1/admin/feature-flags/{featureName}", TEST_FEATURE_NAME)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(featureFlagService).updateFeatureFlag(TEST_FEATURE_NAME, true);
    }

    @Test
    @DisplayName("Should delete feature flag successfully")
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteFeatureFlag() throws Exception {
        // Arrange
        doNothing().when(featureFlagService).deleteFeatureFlag(TEST_FEATURE_NAME);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/admin/feature-flags/{featureName}", TEST_FEATURE_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feature flag deleted successfully"))
                .andExpect(jsonPath("$.code").value("200"));

        verify(featureFlagService).deleteFeatureFlag(TEST_FEATURE_NAME);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent feature flag")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenDeletingNonExistentFeatureFlag() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Feature flag not found: " + TEST_FEATURE_NAME))
            .when(featureFlagService).deleteFeatureFlag(TEST_FEATURE_NAME);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/admin/feature-flags/{featureName}", TEST_FEATURE_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(featureFlagService).deleteFeatureFlag(TEST_FEATURE_NAME);
    }

    @Test
    @DisplayName("Should get enabled feature flags successfully")
    @WithMockUser(roles = "ADMIN")
    void shouldGetEnabledFeatureFlags() throws Exception {
        // Arrange
        List<FeatureFlag> enabledFlags = Arrays.asList(
            TestDataBuilder.createTestFeatureFlag()
        );
        when(featureFlagService.getAllFeatureFlags()).thenReturn(enabledFlags);

        // Act & Assert
        mockMvc.perform(get("/api/v1/admin/feature-flags/enabled")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Enabled feature flags retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isArray());

        verify(featureFlagService).getAllFeatureFlags();
    }

    @Test
    @DisplayName("Should get disabled feature flags successfully")
    @WithMockUser(roles = "ADMIN")
    void shouldGetDisabledFeatureFlags() throws Exception {
        // Arrange
        FeatureFlag disabledFlag = TestDataBuilder.createTestFeatureFlag();
        disabledFlag.setEnabled(false);
        List<FeatureFlag> disabledFlags = Arrays.asList(disabledFlag);
        when(featureFlagService.getAllFeatureFlags()).thenReturn(disabledFlags);

        // Act & Assert
        mockMvc.perform(get("/api/v1/admin/feature-flags/disabled")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Disabled feature flags retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isArray());

        verify(featureFlagService).getAllFeatureFlags();
    }

    @Test
    @DisplayName("Should check feature flag status successfully")
    @WithMockUser(roles = "USER")
    void shouldCheckFeatureFlagStatus() throws Exception {
        // Arrange
        when(featureFlagService.isFeatureEnabled(TEST_FEATURE_NAME)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/v1/admin/feature-flags/check/{featureName}", TEST_FEATURE_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feature flag status retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.featureName").value(TEST_FEATURE_NAME))
                .andExpect(jsonPath("$.data.enabled").value(true));

        verify(featureFlagService).isFeatureEnabled(TEST_FEATURE_NAME);
    }

    @Test
    @DisplayName("Should return 401 when accessing without authentication")
    void shouldReturn401WhenAccessingWithoutAuthentication() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 403 when accessing with insufficient privileges")
    @WithMockUser(roles = "USER")
    void shouldReturn403WhenAccessingWithInsufficientPrivileges() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should handle invalid JSON in request body")
    @WithMockUser(roles = "ADMIN")
    void shouldHandleInvalidJsonInRequestBody() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content("invalid json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle missing required fields in request")
    @WithMockUser(roles = "ADMIN")
    void shouldHandleMissingRequiredFieldsInRequest() throws Exception {
        // Arrange
        FeatureFlagResource.FeatureFlagRequest request = new FeatureFlagResource.FeatureFlagRequest();
        // Missing featureName and description

        // Act & Assert
        mockMvc.perform(post("/api/v1/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should validate feature flag name format")
    @WithMockUser(roles = "ADMIN")
    void shouldValidateFeatureFlagNameFormat() throws Exception {
        // Arrange
        FeatureFlagResource.FeatureFlagRequest request = new FeatureFlagResource.FeatureFlagRequest();
        request.setFeatureName("invalid-feature-name-with-special-chars!");
        request.setDescription(TEST_DESCRIPTION);

        // Act & Assert
        mockMvc.perform(post("/api/v1/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
