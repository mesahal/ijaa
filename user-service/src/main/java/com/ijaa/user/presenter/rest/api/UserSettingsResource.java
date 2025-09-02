package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.dto.UserSettingsDto;
import com.ijaa.user.domain.enums.Theme;
import com.ijaa.user.domain.request.UserSettingsRequest;
import com.ijaa.user.service.UserContextService;
import com.ijaa.user.service.UserSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/settings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Settings", description = "User settings management APIs")
public class UserSettingsResource {

    private final UserSettingsService userSettingsService;
    private final UserContextService userContextService;

    @GetMapping
    @Operation(summary = "Get user settings", description = "Retrieve current user settings including theme preference")
    public ResponseEntity<ApiResponse<UserSettingsDto>> getUserSettings() {
        try {
            String userId = userContextService.getCurrentUserId();
            UserSettingsDto settings = userSettingsService.getUserSettings(userId);
            
            return ResponseEntity.ok(new ApiResponse<>("User settings retrieved successfully", "200", settings));
        } catch (Exception e) {
            log.error("Error retrieving user settings", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to retrieve user settings", "500", null));
        }
    }

    @PutMapping
    @Operation(summary = "Update user settings", description = "Update user settings including theme preference")
    public ResponseEntity<ApiResponse<UserSettingsDto>> updateUserSettings(@RequestBody UserSettingsRequest request) {
        try {
            String userId = userContextService.getCurrentUserId();
            UserSettingsDto updatedSettings = userSettingsService.updateUserSettings(userId, request);
            
            return ResponseEntity.ok(new ApiResponse<>("User settings updated successfully", "200", updatedSettings));
        } catch (Exception e) {
            log.error("Error updating user settings", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to update user settings", "500", null));
        }
    }

    @GetMapping("/theme")
    @Operation(summary = "Get user theme", description = "Retrieve current user theme preference")
    public ResponseEntity<ApiResponse<Theme>> getUserTheme() {
        try {
            String userId = userContextService.getCurrentUserId();
            Theme theme = userSettingsService.getThemeForUser(userId);
            
            return ResponseEntity.ok(new ApiResponse<>("User theme retrieved successfully", "200", theme));
        } catch (Exception e) {
            log.error("Error retrieving user theme", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to retrieve user theme", "500", null));
        }
    }

    @GetMapping("/themes")
    @Operation(summary = "Get available themes", description = "Get list of available theme options")
    public ResponseEntity<ApiResponse<List<Theme>>> getAvailableThemes() {
        try {
            List<Theme> themes = List.of(Theme.values());
            return ResponseEntity.ok(new ApiResponse<>("Available themes retrieved successfully", "200", themes));
        } catch (Exception e) {
            log.error("Error retrieving available themes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to retrieve available themes", "500", null));
        }
    }
}
