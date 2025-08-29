package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.annotation.RequiresFeature;
import com.ijaa.user.common.utils.AppUtils;
import com.ijaa.user.common.utils.FeatureFlagUtils;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.domain.enums.AdminRole;
import com.ijaa.user.domain.request.AnnouncementRequest;

import com.ijaa.user.domain.request.ReportRequest;
import com.ijaa.user.domain.response.AdminProfileResponse;
import com.ijaa.user.domain.response.AnnouncementResponse;

import com.ijaa.user.domain.response.ReportResponse;
import com.ijaa.user.domain.response.UserResponse;
import com.ijaa.user.service.AdminService;
import com.ijaa.user.service.AnnouncementService;

import com.ijaa.user.service.FeatureFlagService;
import com.ijaa.user.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(AppUtils.ADMIN_BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "APIs for admin management operations")
public class AdminManagementResource {

    private final AdminService adminService;

    private final AnnouncementService announcementService;
    private final ReportService reportService;
    private final FeatureFlagUtils featureFlagUtils;
    // Admin Management (ADMIN only)
    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.features")
    @Operation(
        summary = "Get All Admins",
        description = "Retrieve all admin users (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Admins retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Admins retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "name": "Administrator",
                                        "email": "admin@ijaa.com",
                                        "role": "ADMIN",
                                        "active": true,
                                        "createdAt": "2025-07-31T01:51:12.870989",
                                        "updatedAt": "2025-07-31T01:51:12.871015"
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
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<List<AdminProfileResponse>>> getAllAdmins() {
        List<AdminProfileResponse> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(new ApiResponse<>("Admins retrieved successfully", "200", admins));
    }



    @PostMapping("/admins/{adminId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.features")
    @Operation(
        summary = "Deactivate Admin",
        description = "Deactivate an admin account (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Admin deactivated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Admin deactivated successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "name": "Administrator",
                                    "email": "admin@ijaa.com",
                                    "role": "ADMIN",
                                    "active": false,
                                    "createdAt": "2025-07-31T01:51:12.870989",
                                    "updatedAt": "2025-07-31T01:51:12.871015"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Admin not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Admin Not Found",
                        value = """
                            {
                                "message": "Admin not found with id: 1",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<AdminProfileResponse>> deactivateAdmin(@PathVariable Long adminId) {
        AdminProfileResponse admin = adminService.deactivateAdmin(adminId);
        return ResponseEntity.ok(new ApiResponse<>("Admin deactivated successfully", "200", admin));
    }

    @PostMapping("/admins/{adminId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.features")
    @Operation(
        summary = "Activate Admin",
        description = "Activate a deactivated admin account (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Admin activated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Admin activated successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "name": "Administrator",
                                    "email": "admin@ijaa.com",
                                    "role": "ADMIN",
                                    "active": true,
                                    "createdAt": "2025-07-31T01:51:12.870989",
                                    "updatedAt": "2025-07-31T01:51:12.871015"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Admin not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Admin Not Found",
                        value = """
                            {
                                "message": "Admin not found with id: 1",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<AdminProfileResponse>> activateAdmin(@PathVariable Long adminId) {
        AdminProfileResponse admin = adminService.activateAdmin(adminId);
        return ResponseEntity.ok(new ApiResponse<>("Admin activated successfully", "200", admin));
    }

    // User Management (ADMIN only)
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.user-management")
    @Operation(
        summary = "Get All Users",
        description = "Retrieve all users with enhanced profile information including profession, location, batch, social links, and connections (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Users retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Users retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "userId": "USER_ABC123XYZ",
                                        "username": "john.doe",
                                        "name": "John Doe",
                                        "email": "john.doe@example.com",
                                        "profession": "Software Engineer",
                                        "location": "Dhaka, Bangladesh",
                                        "batch": "2018",
                                        "phone": "+8801234567890",
                                        "linkedIn": "https://linkedin.com/in/johndoe",
                                        "website": "https://johndoe.com",
                                        "facebook": "https://facebook.com/johndoe",
                                        "bio": "Passionate software engineer with 5+ years of experience",
                                        "connections": 25,
                                        "active": true,
                                        "createdAt": "2024-01-15T10:30:00",
                                        "updatedAt": "2024-01-20T14:45:00"
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
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = adminService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>("Users retrieved successfully", "200", users));
    }

    @PostMapping("/users/{userId}/block")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.user-management")
    @Operation(
        summary = "Block User",
        description = "Block a user account (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User blocked successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "User blocked successfully",
                                "code": "200",
                                "data": {
                                    "userId": "USER_ABC123XYZ",
                                    "username": "john.doe",
                                    "name": "John Doe",
                                    "email": "john.doe@example.com",
                                    "profession": "Software Engineer",
                                    "location": "Dhaka, Bangladesh",
                                    "batch": "2018",
                                    "phone": "+8801234567890",
                                    "linkedIn": "https://linkedin.com/in/johndoe",
                                    "website": "https://johndoe.com",
                                    "facebook": "https://facebook.com/johndoe",
                                    "bio": "Passionate software engineer with 5+ years of experience",
                                    "connections": 25,
                                    "active": false,
                                    "createdAt": "2024-01-15T10:30:00",
                                    "updatedAt": "2024-01-20T14:45:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "User Not Found",
                        value = """
                            {
                                "message": "User not found with userId: USER_ABC123XYZ",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<UserResponse>> blockUser(
            @Parameter(description = "User ID", example = "USER_ABC123XYZ") @PathVariable String userId) {
        UserResponse user = adminService.blockUser(userId);
        return ResponseEntity.ok(new ApiResponse<>("User blocked successfully", "200", user));
    }

    @PostMapping("/users/{userId}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.user-management")
    @Operation(
        summary = "Unblock User",
        description = "Unblock a blocked user account (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User unblocked successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "User unblocked successfully",
                                "code": "200",
                                "data": {
                                    "userId": "USER_ABC123XYZ",
                                    "username": "john.doe",
                                    "name": "John Doe",
                                    "email": "john.doe@example.com",
                                    "profession": "Software Engineer",
                                    "location": "Dhaka, Bangladesh",
                                    "batch": "2018",
                                    "phone": "+8801234567890",
                                    "linkedIn": "https://linkedin.com/in/johndoe",
                                    "website": "https://johndoe.com",
                                    "facebook": "https://facebook.com/johndoe",
                                    "bio": "Passionate software engineer with 5+ years of experience",
                                    "connections": 25,
                                    "active": true,
                                    "createdAt": "2024-01-15T10:30:00",
                                    "updatedAt": "2024-01-20T14:45:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "User Not Found",
                        value = """
                            {
                                "message": "User not found with userId: USER_ABC123XYZ",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<UserResponse>> unblockUser(
            @Parameter(description = "User ID", example = "USER_ABC123XYZ") @PathVariable String userId) {
        UserResponse user = adminService.unblockUser(userId);
        return ResponseEntity.ok(new ApiResponse<>("User unblocked successfully", "200", user));
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.user-management")
    @Operation(
        summary = "Delete User",
        description = "Permanently delete a user account (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "User deleted successfully",
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
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "User Not Found",
                        value = """
                            {
                                "message": "User not found with userId: USER_ABC123XYZ",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @Parameter(description = "User ID", example = "USER_ABC123XYZ") @PathVariable String userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse<>("User deleted successfully", "200", null));
    }



    // Announcement Management (ADMIN only)
    @GetMapping("/announcements")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.announcements")
    @Operation(
        summary = "Get All Announcements",
        description = "Retrieve all system announcements (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Announcements retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Announcements retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "title": "Important Announcement",
                                        "message": "This is a test announcement.",
                                        "createdAt": "2025-07-31T01:51:12.870989",
                                        "updatedAt": "2025-07-31T01:51:12.871015"
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
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<List<AnnouncementResponse>>> getAllAnnouncements() {
        List<AnnouncementResponse> announcements = announcementService.getAllAnnouncements();
        return ResponseEntity.ok(new ApiResponse<>("Announcements retrieved successfully", "200", announcements));
    }

    @PostMapping("/announcements")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.announcements")
    @Operation(
        summary = "Create Announcement",
        description = "Create a new system announcement (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Announcement details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AnnouncementRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Regular Announcement",
                        summary = "Create a regular announcement",
                        value = """
                            {
                                "title": "Important Alumni Update",
                                "content": "We are pleased to announce the launch of our new alumni portal. All alumni are encouraged to register and update their profiles.",
                                "category": "GENERAL",
                                "isUrgent": false,
                                "authorName": "Alumni Association",
                                "authorEmail": "alumni@ijaa.com",
                                "imageUrl": "https://example.com/announcement-image.jpg"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Urgent Announcement",
                        summary = "Create an urgent announcement",
                        value = """
                            {
                                "title": "Emergency Alumni Meeting",
                                "content": "There will be an emergency alumni meeting tomorrow at 2 PM. All alumni are requested to attend.",
                                "category": "URGENT",
                                "isUrgent": true,
                                "authorName": "Alumni Association",
                                "authorEmail": "alumni@ijaa.com"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Simple Announcement",
                        summary = "Create a simple announcement",
                        value = """
                            {
                                "title": "Welcome New Alumni",
                                "content": "Welcome to all new alumni members! We look forward to your active participation.",
                                "authorName": "Alumni Association",
                                "authorEmail": "alumni@ijaa.com"
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
            description = "Announcement created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Announcement created successfully",
                                "code": "201",
                                "data": {
                                    "id": 1,
                                    "title": "Important Announcement",
                                    "message": "This is a test announcement.",
                                    "createdAt": "2025-07-31T01:51:12.870989",
                                    "updatedAt": "2025-07-31T01:51:12.871015"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid announcement request",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Announcement Request",
                        value = """
                            {
                                "message": "Invalid announcement request",
                                "code": "400",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<AnnouncementResponse>> createAnnouncement(@Valid @RequestBody AnnouncementRequest announcementRequest) {
        AnnouncementResponse announcement = announcementService.createAnnouncement(announcementRequest);
        return ResponseEntity.ok(new ApiResponse<>("Announcement created successfully", "201", announcement));
    }

    @DeleteMapping("/announcements/{announcementId}")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.announcements")
    @Operation(
        summary = "Delete Announcement",
        description = "Delete a system announcement (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Announcement deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Announcement deleted successfully",
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
            description = "Announcement not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Announcement Not Found",
                        value = """
                            {
                                "message": "Announcement not found with id: 1",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<Void>> deleteAnnouncement(@PathVariable Long announcementId) {
        announcementService.deleteAnnouncement(announcementId);
        return ResponseEntity.ok(new ApiResponse<>("Announcement deleted successfully", "200", null));
    }

    // Report Management (ADMIN only)
    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.reports")
    @Operation(
        summary = "Get All Reports",
        description = "Retrieve all user reports (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Reports retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Reports retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "type": "USER_REPORT",
                                        "userId": "USER_ABC123XYZ",
                                        "message": "This user is spamming.",
                                        "status": "OPEN",
                                        "createdAt": "2025-07-31T01:51:12.870989",
                                        "updatedAt": "2025-07-31T01:51:12.871015"
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
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getAllReports() {
        List<ReportResponse> reports = reportService.getAllReports();
        return ResponseEntity.ok(new ApiResponse<>("Reports retrieved successfully", "200", reports));
    }

    @PostMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.reports")
    @Operation(
        summary = "Create Report",
        description = "Create a new user report (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Report details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReportRequest.class),
                examples = {
                    @ExampleObject(
                        name = "User Report",
                        summary = "Create a user report",
                        value = """
                            {
                                "title": "Inappropriate User Behavior",
                                "description": "User has been posting inappropriate content in the alumni forum",
                                "category": "USER_REPORT",
                                "priority": "HIGH",
                                "reporterName": "Admin User",
                                "reporterEmail": "admin@ijaa.com",
                                "attachmentUrl": "https://example.com/evidence.pdf"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "System Report",
                        summary = "Create a system report",
                        value = """
                            {
                                "title": "System Performance Issue",
                                "description": "The alumni portal is experiencing slow response times",
                                "category": "SYSTEM_REPORT",
                                "priority": "MEDIUM",
                                "reporterName": "System Admin",
                                "reporterEmail": "system@ijaa.com"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Simple Report",
                        summary = "Create a simple report",
                        value = """
                            {
                                "title": "General Feedback",
                                "description": "General feedback about the alumni portal",
                                "reporterName": "John Doe",
                                "reporterEmail": "john.doe@example.com"
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
            description = "Report created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Report created successfully",
                                "code": "201",
                                "data": {
                                    "id": 1,
                                    "type": "USER_REPORT",
                                    "userId": "USER_ABC123XYZ",
                                    "message": "This user is spamming.",
                                    "status": "OPEN",
                                    "createdAt": "2025-07-31T01:51:12.870989",
                                    "updatedAt": "2025-07-31T01:51:12.871015"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid report request",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Report Request",
                        value = """
                            {
                                "message": "Invalid report request",
                                "code": "400",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<ReportResponse>> createReport(@Valid @RequestBody ReportRequest reportRequest) {
        ReportResponse report = reportService.createReport(reportRequest);
        return ResponseEntity.ok(new ApiResponse<>("Report created successfully", "201", report));
    }

    @PutMapping("/reports/{reportId}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.reports")
    @Operation(
        summary = "Resolve Report",
        description = "Mark a user report as resolved (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Report resolved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Report resolved successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "type": "USER_REPORT",
                                    "userId": "USER_ABC123XYZ",
                                    "message": "This user is spamming.",
                                    "status": "RESOLVED",
                                    "createdAt": "2025-07-31T01:51:12.870989",
                                    "updatedAt": "2025-07-31T01:51:12.871015"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Report not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Report Not Found",
                        value = """
                            {
                                "message": "Report not found with id: 1",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient privileges (ADMIN required)",
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
    public ResponseEntity<ApiResponse<ReportResponse>> resolveReport(@PathVariable Long reportId) {
        ReportResponse report = reportService.resolveReport(reportId, "Report resolved by admin");
        return ResponseEntity.ok(new ApiResponse<>("Report resolved successfully", "200", report));
    }

} 