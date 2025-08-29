package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.annotation.RequiresFeature;
import com.ijaa.user.common.utils.AppUtils;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.request.AdminLoginRequest;
import com.ijaa.user.domain.request.AdminPasswordChangeRequest;
import com.ijaa.user.domain.request.AdminSignupRequest;
import com.ijaa.user.domain.response.AdminAuthResponse;
import com.ijaa.user.domain.response.AdminProfileResponse;
import com.ijaa.user.domain.response.DashboardStatsResponse;
import com.ijaa.user.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppUtils.ADMIN_BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Admin Authentication", description = "APIs for admin authentication and profile management")
public class AdminAuthResource {

    private final AdminService adminService;

    @PostMapping("/signup")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isFirstAdmin()")
    @RequiresFeature("admin.auth")
    @Operation(
        summary = "Admin Registration",
        description = "Register a new admin (first admin: no auth required, subsequent admins: ADMIN role required)",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Admin registration details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdminSignupRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Valid Admin Registration",
                        summary = "Valid admin registration data",
                        value = """
                            {
                                "name": "New Admin",
                                "email": "newadmin@ijaa.com",
                                "password": "securePassword123",
                                "role": "ADMIN"
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
            description = "Registration successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Admin registration successful",
                                "code": "201",
                                "data": {
                                    "token": "eyJhbGciOiJIUzUxMiJ9...",
                                    "adminId": 2,
                                    "name": "New Admin",
                                    "email": "newadmin@ijaa.com",
                                    "role": "ADMIN",
                                    "active": true
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Admin already exists",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Admin Already Exists",
                        value = """
                            {
                                "message": "Admin already exists",
                                "code": "409",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Insufficient privileges",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Insufficient Privileges",
                        value = """
                            {
                                "message": "Only existing ADMIN can create new ADMIN accounts",
                                "code": "403",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<AdminAuthResponse>> signup(@Valid @RequestBody AdminSignupRequest request) {
        AdminAuthResponse response = adminService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Admin registration successful", "201", response));
    }

    @PostMapping("/login")
    @Operation(
        summary = "Admin Login",
        description = "Authenticate admin and return JWT token",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Admin login credentials",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdminLoginRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Valid Admin Login",
                        summary = "Valid admin login data",
                        value = """
                            {
                                "email": "afrinjahaneva@gmail.com",
                                "password": "Admin@123"
                            }
                            """
                    )
                }
            )
        ),
        responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "Admin login successful",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AdminAuthResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "Successful Login",
                            summary = "Admin login successful response",
                            value = """
                                {
                                    "message": "Admin login successful",
                                    "code": "200",
                                    "data": {
                                        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                        "adminId": 1,
                                        "name": "Admin User",
                                        "email": "admin@ijaa.com",
                                        "role": "ADMIN",
                                        "active": true
                                    }
                                }
                                """
                        )
                    }
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "401",
                description = "Invalid credentials",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "Invalid Credentials",
                            summary = "Invalid admin credentials",
                            value = """
                                {
                                    "message": "Invalid email or password",
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
                description = "Feature disabled",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "Feature Disabled",
                            summary = "Admin auth feature is disabled",
                            value = """
                                {
                                    "message": "Feature 'admin.auth' is disabled",
                                    "code": "403",
                                    "data": null
                                }
                                """
                        )
                    }
                )
            )
        }
    )
    public ResponseEntity<ApiResponse<AdminAuthResponse>> login(@Valid @RequestBody AdminLoginRequest request) {
        // log.info("Admin login attempt for email: {}", request.getEmail()); // Original code had this line commented out
        
        AdminAuthResponse response = adminService.login(request);
        
        // log.info("Admin login successful for email: {}", request.getEmail()); // Original code had this line commented out
        return ResponseEntity.ok(new ApiResponse<>("Admin login successful", "200", response));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.auth")
    @Operation(
        summary = "Get Admin Profile",
        description = "Retrieve the profile of the currently authenticated admin",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Profile retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Admin profile retrieved successfully",
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
    public ResponseEntity<ApiResponse<AdminProfileResponse>> getProfile() {
        // Get current authenticated admin's profile
        AdminProfileResponse response = adminService.getCurrentAdminProfile();
        
        return ResponseEntity.ok(new ApiResponse<>("Admin profile retrieved successfully", "200", response));
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.features")
    @Operation(
        summary = "Get Admin Dashboard",
        description = "Retrieve dashboard statistics and overview for admin",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Dashboard stats retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Dashboard stats retrieved successfully",
                                "code": "200",
                                "data": {
                                    "totalUsers": 150,
                                    "activeUsers": 120,
                                    "blockedUsers": 5,
                                    "totalAdmins": 3,
                                    "activeAdmins": 2,

                                    "totalAnnouncements": 0,
                                    "pendingReports": 0,
                                    "topBatches": [],
                                    "recentActivities": []
                                }
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
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        DashboardStatsResponse stats = adminService.getDashboardStats();
        return ResponseEntity.ok(new ApiResponse<>("Dashboard stats retrieved successfully", "200", stats));
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    @RequiresFeature("admin.auth")
    @Operation(
        summary = "Change Admin Password",
        description = "Change the password of the currently authenticated admin",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Password change details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdminPasswordChangeRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Valid Password Change",
                        summary = "Valid password change data",
                        value = """
                            {
                                "currentPassword": "oldPassword123",
                                "newPassword": "newSecurePassword123",
                                "confirmPassword": "newSecurePassword123"
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
            description = "Password changed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Password changed successfully",
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
            responseCode = "400",
            description = "Invalid password change request",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Current Password Incorrect",
                        value = """
                            {
                                "message": "Current password is incorrect",
                                "code": "400",
                                "data": null
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Passwords Don't Match",
                        value = """
                            {
                                "message": "New password and confirm password do not match",
                                "code": "400",
                                "data": null
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Same Password",
                        value = """
                            {
                                "message": "New password must be different from current password",
                                "code": "400",
                                "data": null
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
                                "message": "Authentication required to change password",
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
    public ResponseEntity<ApiResponse<AdminProfileResponse>> changePassword(@Valid @RequestBody AdminPasswordChangeRequest request) {
        AdminProfileResponse response = adminService.changePassword(request);
        return ResponseEntity.ok(new ApiResponse<>("Password changed successfully", "200", response));
    }
} 
