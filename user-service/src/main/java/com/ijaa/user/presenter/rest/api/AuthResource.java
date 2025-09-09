package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.annotation.RequiresFeature;
import com.ijaa.user.common.utils.AppUtils;
import com.ijaa.user.common.utils.FeatureFlagUtils;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.request.SignInRequest;
import com.ijaa.user.domain.request.SignUpRequest;
import com.ijaa.user.domain.request.UserPasswordChangeRequest;
import com.ijaa.user.domain.response.AuthResponse;
import com.ijaa.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping(AppUtils.BASE_URL)
@Tag(name = "User Authentication")
public class AuthResource {

    private final AuthService authService;
    private final FeatureFlagUtils featureFlagUtils;

    @PostMapping("/signin")
    @RequiresFeature("user.login")
    @Operation(
        summary = "User Login",
        description = "Authenticate a user with username and password",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User login credentials",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SignInRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Valid Login",
                        summary = "Valid login credentials",
                        value = """
                            {
                                "username": "mdsahal.info@gmail.com",
                                "password": "Admin@123"
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
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Login successful",
                                "code": "200",
                                "data": {
                                    "token": "eyJhbGciOiJIUzUxMiJ9...",
                                    "userId": "user123"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid credentials",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Credentials",
                        value = """
                            {
                                "message": "Invalid credentials",
                                "code": "400",
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
                                "message": "User not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<AuthResponse>> signIn(
            @Valid @RequestBody SignInRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.verify(request);
        
        // Get refresh token for user and set it in cookie
        String refreshToken = authService.getRefreshTokenForUser(request.getUsername());
        if (refreshToken != null) {
            setRefreshTokenCookie(response, refreshToken);
        }
        
        return ResponseEntity.ok(
                new ApiResponse<>("Login successful", "200", authResponse)
        );
    }

    @PostMapping("/signup")
    @RequiresFeature("user.registration")
    @Operation(
        summary = "User Registration",
        description = "Register a new user with username and password",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User registration details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SignUpRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Valid Registration",
                        summary = "Valid registration data",
                        value = """
                            {
                                "username": "john.doe",
                                "password": "password123"
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
                                "message": "Registration successful",
                                "code": "201",
                                "data": {
                                    "token": "eyJhbGciOiJIUzUxMiJ9...",
                                    "userId": "user123"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Username already exists",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Username Already Exists",
                        value = """
                            {
                                "message": "Username already taken",
                                "code": "409",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Invalid request data",
                                "code": "400",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<AuthResponse>> signUp(
            @Valid @RequestBody SignUpRequest request) {
        AuthResponse authResponse = authService.registerUser(request);
        featureFlagUtils.logFeatureUsage(FeatureFlagUtils.USER_REGISTRATION, authResponse.getUserId());
        return ResponseEntity.ok(
                new ApiResponse<>("Registration successful", "201", authResponse)
        );
    }

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("user.password-change")
    @Operation(
        summary = "Change User Password",
        description = "Change the current user's password (USER only)",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Password change details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserPasswordChangeRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Valid Password Change",
                        summary = "Valid password change request",
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
                                "data": null
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
            description = "Forbidden - Insufficient privileges (USER required)",
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
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody UserPasswordChangeRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok(new ApiResponse<>("Password changed successfully", "200", null));
    }

    @PostMapping("/refresh")
    @Operation(
        summary = "Refresh Access Token",
        description = "Generate a new access token using a valid refresh token from cookie"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Token refreshed successfully",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Token Refreshed",
                        value = """
                            {
                                "message": "Token refreshed successfully",
                                "code": "200",
                                "data": {
                                    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                    "tokenType": "Bearer",
                                    "userId": "USER_ABC123"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Invalid or expired refresh token",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Refresh Token",
                        value = """
                            {
                                "message": "Invalid or expired refresh token",
                                "code": "401",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(HttpServletRequest request) {
        String refreshToken = getRefreshTokenFromCookie(request);
        
        if (refreshToken == null) {
            return ResponseEntity.status(401)
                .body(new ApiResponse<>("Refresh token not found", "401", null));
        }
        
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(new ApiResponse<>("Token refreshed successfully", "200", authResponse));
    }

    @PostMapping("/logout")
    @Operation(
        summary = "User Logout",
        description = "Logout user by invalidating refresh token and clearing cookie"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Logout successful",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Logout Success",
                        value = """
                            {
                                "message": "Logout successful",
                                "code": "200",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookie(request);
        
        if (refreshToken != null) {
            authService.logout(refreshToken);
        }
        
        // Clear refresh token cookie
        clearRefreshTokenCookie(response);
        
        return ResponseEntity.ok(new ApiResponse<>("Logout successful", "200", null));
    }

    /**
     * Extract refresh token from cookie
     */
    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Set refresh token cookie
     */
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setPath("/api/v1/user");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days in seconds
        response.addCookie(cookie);
    }

    /**
     * Clear refresh token cookie
     */
    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setPath("/api/v1/user");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
