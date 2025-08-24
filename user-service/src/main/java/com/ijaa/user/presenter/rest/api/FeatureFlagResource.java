package com.ijaa.user.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.service.FeatureFlagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/feature-flags")
@Slf4j
@Tag(name = "Feature Flag Management", description = "APIs for managing feature flags")
public class FeatureFlagResource {

    private final FeatureFlagService featureFlagService;
    private final ObjectMapper objectMapper;

    public FeatureFlagResource(FeatureFlagService featureFlagService, ObjectMapper objectMapper) {
        this.featureFlagService = featureFlagService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get All Feature Flags",
        description = "Retrieve all feature flags in the system",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Feature flags retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Feature flags retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "featureName": "NEW_UI",
                                        "enabled": true,
                                        "description": "Enable new user interface with modern design",
                                        "createdAt": "2024-12-01T10:00:00",
                                        "updatedAt": "2024-12-01T10:00:00"
                                    },
                                    {
                                        "id": 2,
                                        "featureName": "CHAT_FEATURE",
                                        "enabled": false,
                                        "description": "Enable real-time chat functionality between alumni",
                                        "createdAt": "2024-12-01T10:00:00",
                                        "updatedAt": "2024-12-01T10:00:00"
                                    }
                                ]
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Missing or invalid token",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Unauthorized",
                        value = """
                            {
                                "message": "Missing Authorization Header",
                                "code": "401",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient privileges",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Forbidden",
                        value = """
                            {
                                "message": "Access denied",
                                "code": "403",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<List<FeatureFlag>>> getAllFeatureFlags() {
        log.info("Getting all feature flags");
        List<FeatureFlag> featureFlags = featureFlagService.getAllFeatureFlags();
        return ResponseEntity.ok(new ApiResponse<>("Feature flags retrieved successfully", "200", featureFlags));
    }

    @GetMapping("/{featureName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get Feature Flag by Name",
        description = "Retrieve a specific feature flag by its name",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Feature flag retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Feature flag retrieved successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "featureName": "NEW_UI",
                                    "enabled": true,
                                    "description": "Enable new user interface with modern design",
                                    "createdAt": "2024-12-01T10:00:00",
                                    "updatedAt": "2024-12-01T10:00:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Feature flag not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Feature flag not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<FeatureFlag>> getFeatureFlag(@PathVariable String featureName) {
        log.info("Getting feature flag: {}", featureName);
        FeatureFlag featureFlag = featureFlagService.getFeatureFlag(featureName);
        
        if (featureFlag == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(new ApiResponse<>("Feature flag retrieved successfully", "200", featureFlag));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create Feature Flag",
        description = "Create a new feature flag",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Feature flag creation details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FeatureFlagRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Create Feature Flag",
                        value = """
                            {
                                "featureName": "NEW_FEATURE",
                                "description": "Enable new feature for testing"
                            }
                            """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Feature flag created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Feature flag created successfully",
                                "code": "201",
                                "data": {
                                    "id": 1,
                                    "featureName": "NEW_FEATURE",
                                    "enabled": false,
                                    "description": "Enable new feature for testing",
                                    "createdAt": "2024-12-01T10:00:00",
                                    "updatedAt": "2024-12-01T10:00:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid input",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Bad Request",
                        value = """
                            {
                                "message": "Feature flag with name NEW_FEATURE already exists",
                                "code": "400",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<FeatureFlag>> createFeatureFlag(@RequestBody FeatureFlagRequest request) {
        log.info("Creating feature flag: {}", request.getFeatureName());
        
        try {
            FeatureFlag featureFlag = featureFlagService.createFeatureFlag(
                request.getFeatureName(), 
                request.getDescription()
            );
            return ResponseEntity.status(201)
                .body(new ApiResponse<>("Feature flag created successfully", "201", featureFlag));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(e.getMessage(), "400", null));
        }
    }

    @PutMapping("/{featureName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update Feature Flag",
        description = "Update a feature flag's enabled status",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Feature flag update details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FeatureFlagUpdateRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Update Feature Flag",
                        value = """
                            {
                                "enabled": true
                            }
                            """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Feature flag updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Feature flag updated successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "featureName": "NEW_UI",
                                    "enabled": true,
                                    "description": "Enable new user interface with modern design",
                                    "createdAt": "2024-12-01T10:00:00",
                                    "updatedAt": "2024-12-01T11:00:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Feature flag not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Feature flag not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<FeatureFlag>> updateFeatureFlag(
            @PathVariable String featureName,
            @RequestBody FeatureFlagUpdateRequest request) {
        log.info("Updating feature flag: {} to enabled: {}", featureName, request.isEnabled());
        
        try {
            FeatureFlag featureFlag = featureFlagService.updateFeatureFlag(featureName, request.isEnabled());
            return ResponseEntity.ok(new ApiResponse<>("Feature flag updated successfully", "200", featureFlag));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{featureName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete Feature Flag",
        description = "Delete a feature flag",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Feature flag deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Feature flag deleted successfully",
                                "code": "200",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Feature flag not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Feature flag not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteFeatureFlag(@PathVariable String featureName) {
        log.info("Deleting feature flag: {}", featureName);
        
        try {
            featureFlagService.deleteFeatureFlag(featureName);
            return ResponseEntity.ok(new ApiResponse<>("Feature flag deleted successfully", "200", null));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get Enabled Feature Flags",
        description = "Retrieve all enabled feature flags",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Enabled feature flags retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Enabled feature flags retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "featureName": "NEW_UI",
                                        "enabled": true,
                                        "description": "Enable new user interface with modern design",
                                        "createdAt": "2024-12-01T10:00:00",
                                        "updatedAt": "2024-12-01T10:00:00"
                                    }
                                ]
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<List<FeatureFlag>>> getEnabledFeatureFlags() {
        log.info("Getting enabled feature flags");
        List<FeatureFlag> featureFlags = featureFlagService.getAllFeatureFlags()
            .stream()
            .filter(FeatureFlag::getEnabled)
            .toList();
        return ResponseEntity.ok(new ApiResponse<>("Enabled feature flags retrieved successfully", "200", featureFlags));
    }

    @GetMapping("/disabled")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get Disabled Feature Flags",
        description = "Retrieve all disabled feature flags",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Disabled feature flags retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Disabled feature flags retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "id": 2,
                                        "featureName": "CHAT_FEATURE",
                                        "enabled": false,
                                        "description": "Enable real-time chat functionality between alumni",
                                        "createdAt": "2024-12-01T10:00:00",
                                        "updatedAt": "2024-12-01T10:00:00"
                                    }
                                ]
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<List<FeatureFlag>>> getDisabledFeatureFlags() {
        log.info("Getting disabled feature flags");
        List<FeatureFlag> featureFlags = featureFlagService.getAllFeatureFlags()
            .stream()
            .filter(flag -> !flag.getEnabled())
            .toList();
        return ResponseEntity.ok(new ApiResponse<>("Disabled feature flags retrieved successfully", "200", featureFlags));
    }

    @GetMapping("/check/{featureName}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Check Feature Flag Status",
        description = "Check if a specific feature flag is enabled",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Feature flag status retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Enabled Feature",
                        value = """
                            {
                                "message": "Feature flag status retrieved successfully",
                                "code": "200",
                                "data": {
                                    "featureName": "NEW_UI",
                                    "enabled": true
                                }
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Disabled Feature",
                        value = """
                            {
                                "message": "Feature flag status retrieved successfully",
                                "code": "200",
                                "data": {
                                    "featureName": "CHAT_FEATURE",
                                    "enabled": false
                                }
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<FeatureFlagStatus>> checkFeatureFlag(@PathVariable String featureName) {
        log.info("Checking feature flag status: {}", featureName);
        boolean enabled = featureFlagService.isFeatureEnabled(featureName);
        FeatureFlagStatus status = new FeatureFlagStatus(featureName, enabled);
        return ResponseEntity.ok(new ApiResponse<>("Feature flag status retrieved successfully", "200", status));
    }

    // Request/Response DTOs
    public static class FeatureFlagRequest {
        private String featureName;
        private String description;

        // Getters and Setters
        public String getFeatureName() { return featureName; }
        public void setFeatureName(String featureName) { this.featureName = featureName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class FeatureFlagUpdateRequest {
        private boolean enabled;

        // Getters and Setters
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public static class FeatureFlagStatus {
        private String featureName;
        private boolean enabled;

        public FeatureFlagStatus(String featureName, boolean enabled) {
            this.featureName = featureName;
            this.enabled = enabled;
        }

        // Getters and Setters
        public String getFeatureName() { return featureName; }
        public void setFeatureName(String featureName) { this.featureName = featureName; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
}

