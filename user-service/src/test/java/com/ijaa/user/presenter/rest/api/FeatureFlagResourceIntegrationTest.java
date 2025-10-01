package com.ijaa.user.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.config.TestConfig;
import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.domain.entity.User;
import com.ijaa.user.repository.FeatureFlagRepository;
import com.ijaa.user.repository.UserRepository;
import com.ijaa.user.domain.request.FeatureFlagRequest;
import com.ijaa.user.domain.request.FeatureFlagUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
@Transactional
class FeatureFlagResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeatureFlagRepository featureFlagRepository;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper;

    private FeatureFlag parentFlag;
    private FeatureFlag childFlag;
    private User adminUser;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Create test admin user
        adminUser = new User();
        adminUser.setUserId("ADMIN_123456");
        adminUser.setUsername("admin@test.com");
        adminUser.setPassword("password");
        adminUser.setActive(true);
        adminUser = userRepository.save(adminUser);

        // Create parent feature flag only if it doesn't exist
        if (!featureFlagRepository.existsByName("chat")) {
            parentFlag = new FeatureFlag();
            parentFlag.setName("chat");
            parentFlag.setDisplayName("Chat Feature");
            parentFlag.setDescription("Real-time chat functionality");
            parentFlag.setEnabled(true);
            parentFlag.setCreatedAt(LocalDateTime.now());
            parentFlag.setUpdatedAt(LocalDateTime.now());
            parentFlag = featureFlagRepository.save(parentFlag);
        } else {
            parentFlag = featureFlagRepository.findByName("chat").orElse(null);
        }

        // Create child feature flag only if it doesn't exist
        if (!featureFlagRepository.existsByName("chat.file-sharing")) {
            childFlag = new FeatureFlag();
            childFlag.setName("chat.file-sharing");
            childFlag.setDisplayName("File Sharing in Chat");
            childFlag.setDescription("Allow file sharing in chat");
            childFlag.setEnabled(true);
            childFlag.setParent(parentFlag);
            childFlag.setCreatedAt(LocalDateTime.now());
            childFlag.setUpdatedAt(LocalDateTime.now());
            childFlag = featureFlagRepository.save(childFlag);
        } else {
            childFlag = featureFlagRepository.findByName("chat.file-sharing").orElse(null);
        }

        // Enable admin features for testing only if it doesn't exist
        if (!featureFlagRepository.existsByName("admin.features")) {
            FeatureFlag adminFeaturesFlag = new FeatureFlag();
            adminFeaturesFlag.setName("admin.features");
            adminFeaturesFlag.setDisplayName("Admin Features");
            adminFeaturesFlag.setDescription("Admin feature management");
            adminFeaturesFlag.setEnabled(true);
            adminFeaturesFlag.setCreatedAt(LocalDateTime.now());
            adminFeaturesFlag.setUpdatedAt(LocalDateTime.now());
            featureFlagRepository.save(adminFeaturesFlag);
        }
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void testGetAllFeatureFlags_Success() throws Exception {
        mockMvc.perform(get("/api/v1/user/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feature flags retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[?(@.name == 'chat')]").exists())
                .andExpect(jsonPath("$.data[?(@.name == 'admin.features')]").exists());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void testGetFeatureFlagByName_Success() throws Exception {
        mockMvc.perform(get("/api/v1/user/admin/feature-flags/chat")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feature flag retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.name").value("chat"))
                .andExpect(jsonPath("$.data.displayName").value("Chat Feature"))
                .andExpect(jsonPath("$.data.enabled").value(true));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void testGetFeatureFlagByName_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/user/admin/feature-flags/nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Feature flag not found: nonexistent"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void testCreateFeatureFlag_Success() throws Exception {
        FeatureFlagRequest request = new FeatureFlagRequest();
        request.setName("new-feature");
        request.setDisplayName("New Feature");
        request.setDescription("A new feature for testing");
        request.setEnabled(true);

        mockMvc.perform(post("/api/v1/user/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Feature flag created successfully"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.data.name").value("new-feature"))
                .andExpect(jsonPath("$.data.displayName").value("New Feature"))
                .andExpect(jsonPath("$.data.enabled").value(true));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void testCreateFeatureFlag_AlreadyExists() throws Exception {
        FeatureFlagRequest request = new FeatureFlagRequest();
        request.setName("chat"); // Already exists
        request.setDisplayName("Chat Feature");
        request.setDescription("Real-time chat functionality");
        request.setEnabled(true);

        mockMvc.perform(post("/api/v1/user/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void testUpdateFeatureFlag_Success() throws Exception {
        FeatureFlagUpdateRequest request = new FeatureFlagUpdateRequest();
        request.setEnabled(false);

        mockMvc.perform(put("/api/v1/user/admin/feature-flags/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feature flag updated successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.name").value("chat"))
                .andExpect(jsonPath("$.data.enabled").value(false));
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void testUpdateFeatureFlag_NotFound() throws Exception {
        FeatureFlagUpdateRequest request = new FeatureFlagUpdateRequest();
        request.setEnabled(true);

        mockMvc.perform(put("/api/v1/user/admin/feature-flags/nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Feature flag not found: nonexistent"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void testDeleteFeatureFlag_Success() throws Exception {
        // Create a flag to delete
        FeatureFlag flagToDelete = new FeatureFlag();
        flagToDelete.setName("flag-to-delete");
        flagToDelete.setDisplayName("Flag to Delete");
        flagToDelete.setDescription("This flag will be deleted");
        flagToDelete.setEnabled(true);
        flagToDelete.setCreatedAt(LocalDateTime.now());
        flagToDelete.setUpdatedAt(LocalDateTime.now());
        featureFlagRepository.save(flagToDelete);

        mockMvc.perform(delete("/api/v1/user/admin/feature-flags/flag-to-delete")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feature flag deleted successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void testDeleteFeatureFlag_NotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/user/admin/feature-flags/nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Feature flag not found: nonexistent"))
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.data").isEmpty());
    }



    @Test
    void testCheckFeatureFlagStatus_Enabled() throws Exception {
        // Create a separate enabled flag for this test to avoid conflicts
        FeatureFlag enabledFlag = new FeatureFlag();
        enabledFlag.setName("enabled-test-flag");
        enabledFlag.setDisplayName("Enabled Test Flag");
        enabledFlag.setDescription("This flag is enabled for testing");
        enabledFlag.setEnabled(true);
        enabledFlag.setCreatedAt(LocalDateTime.now());
        enabledFlag.setUpdatedAt(LocalDateTime.now());
        featureFlagRepository.save(enabledFlag);

        mockMvc.perform(get("/api/v1/user/admin/feature-flags/enabled-test-flag/enabled")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feature flag status retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.name").value("enabled-test-flag"))
                .andExpect(jsonPath("$.data.enabled").value(true));
    }

    @Test
    void testCheckFeatureFlagStatus_Disabled() throws Exception {
        // Create a disabled flag
        FeatureFlag disabledFlag = new FeatureFlag();
        disabledFlag.setName("disabled-check");
        disabledFlag.setDisplayName("Disabled Check");
        disabledFlag.setDescription("This feature is disabled");
        disabledFlag.setEnabled(false);
        disabledFlag.setCreatedAt(LocalDateTime.now());
        disabledFlag.setUpdatedAt(LocalDateTime.now());
        featureFlagRepository.save(disabledFlag);

        mockMvc.perform(get("/api/v1/user/admin/feature-flags/disabled-check/enabled")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Feature flag status retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.name").value("disabled-check"))
                .andExpect(jsonPath("$.data.enabled").value(false));
    }



    @Test
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void testGetAllFeatureFlags_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/user/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // The system returns 404 for unauthorized access
    }

    @Test
    void testGetAllFeatureFlags_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/user/admin/feature-flags")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // The system returns 404 for unauthenticated access
    }
}
