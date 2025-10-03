package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.annotation.RequiresFeature;
import com.ijaa.user.common.utils.AppUtils;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.response.AdminProfileResponse;
import com.ijaa.user.domain.response.UserResponse;
import com.ijaa.user.service.AdminService;
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

import java.util.List;

@RestController
@RequestMapping(AppUtils.ADMIN_BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Admin Management")
public class AdminManagementResource {

    private final AdminService adminService;
    
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
        description = "Retrieve all users with enhanced profile information including profession, city, country, batch, social links, and connections (ADMIN only)",
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
                                        "cityId": 1,
                                        "countryId": 1,
                                        "cityName": "Dhaka",
                                        "countryName": "Bangladesh",
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
                                    "cityId": 1,
                                    "countryId": 1,
                                    "cityName": "Dhaka",
                                    "countryName": "Bangladesh",
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
                                    "cityId": 1,
                                    "countryId": 1,
                                    "cityName": "Dhaka",
                                    "countryName": "Bangladesh",
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

}