package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.annotation.RequiresFeature;
import com.ijaa.user.common.utils.AppUtils;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.request.GoogleSignInRequest;
import com.ijaa.user.domain.response.AuthResponse;
import com.ijaa.user.service.AuthService;
import com.ijaa.user.service.GoogleOAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Google OAuth", description = "APIs for Google OAuth authentication")
public class GoogleOAuthResource {

    private final AuthService authService;
    private final GoogleOAuthService googleOAuthService;

    @PostMapping("/google/signin")
    @RequiresFeature("user.google-signin")
    @Operation(
        summary = "Google Sign-In",
        description = "Authenticate user using Google OAuth2. Creates new user account if email doesn't exist, or logs in existing user.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Google OAuth tokens",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GoogleSignInRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Valid Google Sign-In",
                        summary = "Google OAuth tokens",
                        value = """
                            {
                                "googleToken": "ya29.a0AfB_byC...",
                                "idToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
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
            description = "Google Sign-In successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "success": true,
                                "message": "Google Sign-In successful",
                                "data": {
                                    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                                    "userId": "USER_ABC123XYZ"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid Google OAuth tokens",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Token",
                        value = """
                            {
                                "success": false,
                                "message": "Invalid Google OAuth token",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "Email already registered with local account",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Email Conflict",
                        value = """
                            {
                                "success": false,
                                "message": "Email already registered with local account",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Server Error",
                        value = """
                            {
                                "success": false,
                                "message": "Google Sign-In failed",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<AuthResponse>> googleSignIn(@Valid @RequestBody GoogleSignInRequest request) {
        try {
            log.info("Processing Google Sign-In request for token: {}", 
                request.getIdToken() != null ? request.getIdToken().substring(0, Math.min(20, request.getIdToken().length())) + "..." : "null");
            
            AuthResponse authResponse = authService.googleSignIn(request);
            
            ApiResponse<AuthResponse> response = new ApiResponse<>(
                "Google Sign-In successful",
                "200",
                authResponse
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Google Sign-In failed: {}", e.getMessage(), e);
            
            ApiResponse<AuthResponse> response = new ApiResponse<>(
                "Google Sign-In failed: " + e.getMessage(),
                "500",
                null
            );
            
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e.getMessage().contains("Invalid") || e.getMessage().contains("token")) {
                status = HttpStatus.BAD_REQUEST;
            } else if (e.getMessage().contains("already registered")) {
                status = HttpStatus.CONFLICT;
            }
            
            return ResponseEntity.status(status).body(response);
        }
    }

    @GetMapping("/google/config")
    @Operation(
        summary = "Get Google OAuth Configuration",
        description = "Get Google OAuth client configuration for frontend integration"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Google OAuth configuration retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "success": true,
                                "message": "Google OAuth configuration retrieved successfully",
                                "data": {
                                    "clientId": "123456789-abcdef.apps.googleusercontent.com"
                                }
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<String>> getGoogleOAuthConfig() {
        try {
            String config = googleOAuthService.getGoogleOAuthConfig();
            
            ApiResponse<String> response = new ApiResponse<>(
                "Google OAuth configuration retrieved successfully",
                "200",
                config
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Failed to get Google OAuth config: {}", e.getMessage(), e);
            
            ApiResponse<String> response = new ApiResponse<>(
                "Failed to get Google OAuth configuration: " + e.getMessage(),
                "500",
                null
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
