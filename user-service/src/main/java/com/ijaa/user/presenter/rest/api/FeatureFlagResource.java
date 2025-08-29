package com.ijaa.user.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.common.annotation.RequiresFeature;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.domain.dto.FeatureFlagDto;
import com.ijaa.user.domain.converter.FeatureFlagConverter;
import com.ijaa.user.presenter.rest.api.dto.FeatureFlagRequest;
import com.ijaa.user.presenter.rest.api.dto.FeatureFlagUpdateRequest;
import com.ijaa.user.presenter.rest.api.dto.FeatureFlagStatus;
import com.ijaa.user.service.FeatureFlagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/feature-flags")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Feature Flag Management", description = "APIs for managing feature flags")
public class FeatureFlagResource {

    private final FeatureFlagService featureFlagService;
    private final ObjectMapper objectMapper;
    private final FeatureFlagConverter featureFlagConverter;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.features")
    @Operation(
        summary = "Get All Feature Flags",
        description = "Retrieve all feature flags in hierarchical structure (ADMIN only)",
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
                                        "name": "chat",
                                        "displayName": "Chat Feature",
                                        "enabled": true,
                                        "description": "Real-time chat functionality",
                                        "children": [
                                            {
                                                "id": 2,
                                                "name": "chat.file-sharing",
                                                "displayName": "File Sharing in Chat",
                                                "enabled": true,
                                                "description": "Allow file sharing in chat"
                                            }
                                        ]
                                    }
                                ]
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<List<FeatureFlagDto>>> getAllFeatureFlags() {
        log.info("Getting all feature flags");
        List<FeatureFlagDto> featureFlags = featureFlagService.getAllFlags();
        return ResponseEntity.ok(new ApiResponse<>("Feature flags retrieved successfully", "200", featureFlags));
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.features")
    @Operation(
        summary = "Get Feature Flag by Name",
        description = "Retrieve a specific feature flag by name (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Feature flag retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Feature flag not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Not Found Response",
                        value = """
                            {
                                "message": "Feature flag not found: {name}",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<FeatureFlagDto>> getFeatureFlag(@PathVariable String name) {
        log.info("Getting feature flag: {}", name);
        FeatureFlag featureFlag = featureFlagService.getFeatureFlag(name);
        
        if (featureFlag == null) {
            return ResponseEntity.status(404)
                .body(new ApiResponse<>("Feature flag not found: " + name, "404", null));
        }
        
        FeatureFlagDto dto = featureFlagConverter.toDto(featureFlag);
        return ResponseEntity.ok(new ApiResponse<>("Feature flag retrieved successfully", "200", dto));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.features")
    @Operation(
        summary = "Create Feature Flag",
        description = "Create a new feature flag (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Feature flag details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FeatureFlagRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Create Feature Flag",
                        value = """
                            {
                                "name": "chat.file-sharing",
                                "displayName": "File Sharing in Chat",
                                "description": "Allow file sharing in chat conversations",
                                "parentId": 1,
                                "enabled": false
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
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request or feature flag already exists"
        )
    })
    public ResponseEntity<ApiResponse<FeatureFlagDto>> createFeatureFlag(@RequestBody FeatureFlagRequest request) {
        log.info("Creating feature flag: {}", request.getName());
        
        try {
            FeatureFlagDto dto = new FeatureFlagDto();
            dto.setName(request.getName());
            dto.setDisplayName(request.getDisplayName());
            dto.setDescription(request.getDescription());
            dto.setParentId(request.getParentId());
            dto.setEnabled(request.getEnabled());
            
            FeatureFlag featureFlag = featureFlagService.createFlag(dto);
            FeatureFlagDto responseDto = featureFlagConverter.toDto(featureFlag);
            return ResponseEntity.status(201)
                .body(new ApiResponse<>("Feature flag created successfully", "201", responseDto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>(e.getMessage(), "400", null));
        }
    }

    @PutMapping("/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.features")
    @Operation(
        summary = "Update Feature Flag",
        description = "Update the enabled state of a feature flag (ADMIN only)",
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
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Feature flag not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Not Found Response",
                        value = """
                            {
                                "message": "Feature flag not found: {name}",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<FeatureFlagDto>> updateFeatureFlag(
            @PathVariable String name,
            @RequestBody FeatureFlagUpdateRequest request) {
        log.info("Updating feature flag: {} to enabled: {}", name, request.isEnabled());
        
        try {
            FeatureFlag featureFlag = featureFlagService.updateFlag(name, request.isEnabled());
            FeatureFlagDto dto = featureFlagConverter.toDto(featureFlag);
            return ResponseEntity.ok(new ApiResponse<>("Feature flag updated successfully", "200", dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404)
                .body(new ApiResponse<>("Feature flag not found: " + name, "404", null));
        }
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.features")
    @Operation(
        summary = "Delete Feature Flag",
        description = "Delete a feature flag (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Feature flag deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Feature flag not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Not Found Response",
                        value = """
                            {
                                "message": "Feature flag not found: {name}",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteFeatureFlag(@PathVariable String name) {
        log.info("Deleting feature flag: {}", name);
        
        try {
            featureFlagService.deleteFeatureFlag(name);
            return ResponseEntity.ok(new ApiResponse<>("Feature flag deleted successfully", "200", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404)
                .body(new ApiResponse<>("Feature flag not found: " + name, "404", null));
        }
    }



    @GetMapping("/{name}/enabled")
    @Operation(
        summary = "Check Feature Flag Status",
        description = "Check if a specific feature flag is enabled (Public endpoint - No authentication required)"
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
                                    "name": "chat",
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
                                    "name": "chat",
                                    "enabled": false
                                }
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<FeatureFlagStatus>> checkFeatureFlag(@PathVariable String name) {
        log.info("Checking feature flag status: {}", name);
        boolean enabled = featureFlagService.isEnabled(name);
        FeatureFlagStatus status = new FeatureFlagStatus(name, enabled);
        return ResponseEntity.ok(new ApiResponse<>("Feature flag status retrieved successfully", "200", status));
    }






}

