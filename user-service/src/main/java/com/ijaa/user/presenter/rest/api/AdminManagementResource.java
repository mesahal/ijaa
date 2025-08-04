package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.utils.AppUtils;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.domain.enums.AdminRole;
import com.ijaa.user.domain.request.AnnouncementRequest;
import com.ijaa.user.domain.request.EventRequest;
import com.ijaa.user.domain.request.ReportRequest;
import com.ijaa.user.domain.response.AdminProfileResponse;
import com.ijaa.user.domain.response.AnnouncementResponse;
import com.ijaa.user.domain.response.EventResponse;
import com.ijaa.user.domain.response.ReportResponse;
import com.ijaa.user.domain.response.UserResponse;
import com.ijaa.user.service.AdminService;
import com.ijaa.user.service.AnnouncementService;
import com.ijaa.user.service.EventService;
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
@Tag(name = "Admin Management", description = "APIs for admin management, user management, events, announcements, reports, and feature flags")
public class AdminManagementResource {

    private final AdminService adminService;
    private final EventService eventService;
    private final AnnouncementService announcementService;
    private final ReportService reportService;
    private final FeatureFlagService featureFlagService;

    // Admin Management (ADMIN only)
    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
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

    @PutMapping("/admins/{adminId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update Admin Role",
        description = "Update an admin's role (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Admin role updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Admin role updated successfully",
                                "code": "200",
                                "data": {
                                    "id": 2,
                                    "name": "New Admin",
                                    "email": "newadmin@ijaa.com",
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
                                "message": "Admin not found",
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
    public ResponseEntity<ApiResponse<AdminProfileResponse>> updateAdminRole(
            @Parameter(description = "Admin ID", example = "1") @PathVariable Long adminId,
            @Parameter(description = "New role for the admin", example = "ADMIN") @RequestParam AdminRole newRole) {
        AdminProfileResponse admin = adminService.updateAdminRole(adminId, newRole);
        return ResponseEntity.ok(new ApiResponse<>("Admin role updated successfully", "200", admin));
    }

    @PostMapping("/admins/{adminId}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminProfileResponse>> deactivateAdmin(@PathVariable Long adminId) {
        AdminProfileResponse admin = adminService.deactivateAdmin(adminId);
        return ResponseEntity.ok(new ApiResponse<>("Admin deactivated successfully", "200", admin));
    }

    @PostMapping("/admins/{adminId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminProfileResponse>> activateAdmin(@PathVariable Long adminId) {
        AdminProfileResponse admin = adminService.activateAdmin(adminId);
        return ResponseEntity.ok(new ApiResponse<>("Admin activated successfully", "200", admin));
    }

    // User Management (ADMIN only)
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get All Users",
        description = "Retrieve all users (ADMIN only)",
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
                                        "name": "john.doe",
                                        "email": null,
                                        "active": true,
                                        "createdAt": null,
                                        "updatedAt": null
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
                                    "name": "john.doe",
                                    "email": null,
                                    "active": false,
                                    "createdAt": null,
                                    "updatedAt": null
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
    @Operation(
        summary = "Unblock User",
        description = "Unblock a user account (ADMIN only)",
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
                                    "name": "john.doe",
                                    "email": null,
                                    "active": true,
                                    "createdAt": null,
                                    "updatedAt": null
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
    @Operation(
        summary = "Delete User",
        description = "Delete a user account (ADMIN only)",
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

    // Event Management (ADMIN only)
    @GetMapping("/events")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get All Events",
        description = "Retrieve all events (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Events retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Events retrieved successfully",
                                "code": "200",
                                "data": [
                                    {
                                        "id": 1,
                                        "title": "New Year Celebration",
                                        "description": "Celebrate the new year with fireworks and music.",
                                        "startDate": "2025-01-01T10:00:00",
                                        "endDate": "2025-01-01T12:00:00",
                                        "location": "Central Park",
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
    public ResponseEntity<ApiResponse<List<EventResponse>>> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents();
        return ResponseEntity.ok(new ApiResponse<>("Events retrieved successfully", "200", events));
    }

    @PostMapping("/events")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Create Event",
        description = "Create a new event (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Event created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Event created successfully",
                                "code": "201",
                                "data": {
                                    "id": 1,
                                    "title": "New Year Celebration",
                                    "description": "Celebrate the new year with fireworks and music.",
                                    "startDate": "2025-01-01T10:00:00",
                                    "endDate": "2025-01-01T12:00:00",
                                    "location": "Central Park",
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
            description = "Invalid event request",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Event Request",
                        value = """
                            {
                                "message": "Invalid event request",
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
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(@Valid @RequestBody EventRequest eventRequest) {
        EventResponse event = eventService.createEvent(eventRequest);
        return ResponseEntity.ok(new ApiResponse<>("Event created successfully", "201", event));
    }

    @PutMapping("/events/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update Event",
        description = "Update an existing event (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Event updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Event updated successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "title": "New Year Celebration",
                                    "description": "Celebrate the new year with fireworks and music.",
                                    "startDate": "2025-01-01T10:00:00",
                                    "endDate": "2025-01-01T12:00:00",
                                    "location": "Central Park",
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
            description = "Event not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Event Not Found",
                        value = """
                            {
                                "message": "Event not found with id: 1",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid event request",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Event Request",
                        value = """
                            {
                                "message": "Invalid event request",
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
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(@PathVariable Long eventId, @Valid @RequestBody EventRequest eventRequest) {
        EventResponse event = eventService.updateEvent(eventId, eventRequest);
        return ResponseEntity.ok(new ApiResponse<>("Event updated successfully", "200", event));
    }

    @DeleteMapping("/events/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Delete Event",
        description = "Delete an event (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Event deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Event deleted successfully",
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
            description = "Event not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Event Not Found",
                        value = """
                            {
                                "message": "Event not found with id: 1",
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
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok(new ApiResponse<>("Event deleted successfully", "200", null));
    }

    // Announcement Management (ADMIN only)
    @GetMapping("/announcements")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get All Announcements",
        description = "Retrieve all announcements (ADMIN only)",
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
    @Operation(
        summary = "Create Announcement",
        description = "Create a new announcement (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
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
    @Operation(
        summary = "Delete Announcement",
        description = "Delete an announcement (ADMIN only)",
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
    @Operation(
        summary = "Get All Reports",
        description = "Retrieve all reports (ADMIN only)",
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
    @Operation(
        summary = "Create Report",
        description = "Create a new report (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
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
    @Operation(
        summary = "Resolve Report",
        description = "Resolve a report (ADMIN only)",
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

    // Feature Flag Management (ADMIN only)
    @GetMapping("/feature-flags")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Get All Feature Flags",
        description = "Retrieve all feature flags (ADMIN only)",
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
                                        "description": "Enable new user interface"
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
    public ResponseEntity<ApiResponse<List<FeatureFlag>>> getAllFeatureFlags() {
        List<FeatureFlag> featureFlags = featureFlagService.getAllFeatureFlags();
        return ResponseEntity.ok(new ApiResponse<>("Feature flags retrieved successfully", "200", featureFlags));
    }

    @PutMapping("/feature-flags/{featureName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
        summary = "Update Feature Flag",
        description = "Update a feature flag's enabled status (ADMIN only)",
        security = @SecurityRequirement(name = "Bearer Authentication")
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
                                    "description": "Enable new user interface"
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
                        name = "Feature Flag Not Found",
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
    public ResponseEntity<ApiResponse<FeatureFlag>> updateFeatureFlag(
            @Parameter(description = "Feature flag name", example = "NEW_UI") @PathVariable String featureName,
            @Parameter(description = "Enable or disable the feature flag", example = "true") @RequestParam boolean enabled) {
        FeatureFlag featureFlag = featureFlagService.updateFeatureFlag(featureName, enabled);
        return ResponseEntity.ok(new ApiResponse<>("Feature flag updated successfully", "200", featureFlag));
    }
} 