// ProfileResource.java
package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.annotation.RequiresFeature;
import com.ijaa.user.common.utils.AppUtils;
import com.ijaa.user.common.utils.FeatureFlagUtils;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.InterestDto;
import com.ijaa.user.domain.dto.ProfileDto;
import com.ijaa.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import com.ijaa.user.domain.request.InterestRequest;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@RequiredArgsConstructor
@Tag(name = "User Profile Management", description = "APIs for user profile management")
public class ProfileResource {

    private final ProfileService profileService;
    private final FeatureFlagUtils featureFlagUtils;

    // Get user's profile by userId - Public endpoint
    @GetMapping("/profile/{userId}")
    @RequiresFeature("user.profile")
    @Operation(summary = "Get User Profile", description = "Get user profile by userId")
    public ResponseEntity<ApiResponse<ProfileDto>> getProfileByUserId(
            @PathVariable String userId) {
        ProfileDto profileDto = profileService.getProfileByUserId(userId);
        featureFlagUtils.logFeatureUsage(FeatureFlagUtils.USER_PROFILE_FEATURES, userId);
        return ResponseEntity.ok(
                new ApiResponse<>("Profile fetched successfully", "200", profileDto)
        );
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("user.profile")
    @Operation(
        summary = "Update Basic Profile Info",
        description = "Update basic profile information (USER role required)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Profile update details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProfileDto.class),
                examples = {
                    @ExampleObject(
                        name = "Update Basic Info",
                        summary = "Update basic profile information",
                        value = """
                                {
                                   "userId": "user123",
                                   "name": "John Doe",
                                   "profession": "Software Engineer",
                                   "location": "New York, NY",
                                   "bio": "A passionate software developer with expertise in Java and Spring Boot.",
                                   "phone": "+1-555-123-4567",
                                   "linkedIn": "https://www.linkedin.com/in/johndoe/",
                                   "website": "https://www.johndoe.dev",
                                   "batch": "2020",
                                   "facebook": "https://www.facebook.com/johndoe",
                                   "email": "johndoe@example.com",
                                   "showPhone": true,
                                   "showLinkedIn": true,
                                   "showWebsite": true,
                                   "showEmail": true,
                                   "showFacebook": false,
                                   "connections": 150
                                 }
                            """
                    ),
                    @ExampleObject(
                        name = "Minimal Update",
                        summary = "Update with minimal fields",
                        value = """
                            {
                                "userId": "user123",
                                "name": "John Doe",
                                "profession": "Software Engineer",
                                "location": "New York, NY"
                            }
                            """
                    )
                }
            )
        )
    )
    public ResponseEntity<ApiResponse<ProfileDto>> updateBasicInfo(@Valid @RequestBody ProfileDto profileDto) {
        ProfileDto updatedProfile = profileService.updateBasicInfo(profileDto);
        return ResponseEntity.ok(
                new ApiResponse<>("Profile updated successfully", "200", updatedProfile)
        );
    }

    @PutMapping("/visibility")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Update Profile Visibility",
        description = "Update profile visibility settings (USER role required)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Visibility settings",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProfileDto.class),
                examples = {
                    @ExampleObject(
                        name = "Update Visibility",
                        summary = "Update profile visibility settings",
                        value = """
                            {
                                "userId": "user123",
                                "showPhone": true,
                                "showLinkedIn": true,
                                "showWebsite": false,
                                "showEmail": true,
                                "showFacebook": false
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Public Profile",
                        summary = "Make profile fully public",
                        value = """
                            {
                                "userId": "user123",
                                "showPhone": true,
                                "showLinkedIn": true,
                                "showWebsite": true,
                                "showEmail": true,
                                "showFacebook": true
                            }
                            """
                    )
                }
            )
        )
    )
    public ResponseEntity<ApiResponse<ProfileDto>> updateVisibility(@Valid @RequestBody ProfileDto profileDto) {
        ProfileDto updatedProfile = profileService.updateVisibility(profileDto);
        return ResponseEntity.ok(
                new ApiResponse<>("Visibility updated successfully", "200", updatedProfile)
        );
    }

    // Experience endpoints
    @GetMapping("/experiences/{userId}")
    @Operation(summary = "Get User Experiences", description = "Get user experiences by userId")
    public ResponseEntity<ApiResponse<List<ExperienceDto>>> getExperiencesByUserId(@PathVariable String userId) {
        List<ExperienceDto> experiences = profileService.getExperiencesByUserId(userId);
        return ResponseEntity.ok(
                new ApiResponse<>("Experiences fetched successfully", "200", experiences)
        );
    }

    @PostMapping("/experiences")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("user.experiences")
    @Operation(
        summary = "Add Experience",
        description = "Add a new experience to user profile (USER role required)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Experience details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExperienceDto.class),
                examples = {
                    @ExampleObject(
                        name = "Add Work Experience",
                        summary = "Add work experience",
                        value = """
                            {
                                "userId": "user123",
                                "title": "Senior Software Engineer",
                                "company": "Tech Corp",
                                "description": "Led development of microservices architecture",
                                "startDate": "2022-01-01",
                                "endDate": "2024-01-01",
                                "current": false
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Current Position",
                        summary = "Add current position",
                        value = """
                            {
                                "userId": "user123",
                                "title": "Lead Developer",
                                "company": "Innovation Inc",
                                "description": "Leading a team of 10 developers",
                                "startDate": "2024-01-01",
                                "current": true
                            }
                            """
                    )
                }
            )
        )
    )
    public ResponseEntity<ApiResponse<ExperienceDto>> addExperience(@Valid @RequestBody ExperienceDto experienceDto) {
        ExperienceDto addedExperience = profileService.addExperience(experienceDto);
        featureFlagUtils.logFeatureUsage(FeatureFlagUtils.USER_EXPERIENCES, experienceDto.getUserId());
        return ResponseEntity.ok(
                new ApiResponse<>("Experience added successfully", "200", addedExperience)
        );
    }

    @DeleteMapping("/experiences/{experienceId}")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("user.experiences")
    @Operation(
        summary = "Delete Experience", 
        description = "Delete a specific experience by ID (USER role required)",
        parameters = {
            @Parameter(name = "experienceId", description = "ID of the experience to delete", required = true)
        }
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Experience deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Experience deleted successfully",
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
            description = "Experience not found or not owned by user",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Experience not found or unauthorized",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteExperience(@PathVariable Long experienceId) {
        profileService.deleteExperience(experienceId);
        return ResponseEntity.ok(
                new ApiResponse<>("Experience deleted successfully", "200", null)
        );
    }

    @PutMapping("/experiences/{experienceId}")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("user.experiences")
    @Operation(
        summary = "Update Experience", 
        description = "Update a specific experience by ID (USER role required)",
        parameters = {
            @Parameter(name = "experienceId", description = "ID of the experience to update", required = true)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Experience update details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExperienceDto.class),
                examples = {
                    @ExampleObject(
                        name = "Update Experience",
                        summary = "Update experience details",
                        value = """
                            {
                                "title": "Senior Software Engineer",
                                "company": "Tech Innovations Inc",
                                "period": "2022-2024",
                                "description": "Led development of microservices architecture"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Partial Update",
                        summary = "Update only specific fields",
                        value = """
                            {
                                "title": "Lead Developer",
                                "company": "Updated Company Name"
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
            description = "Experience updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Experience updated successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "title": "Senior Software Engineer",
                                    "company": "Tech Innovations Inc",
                                    "period": "2022-2024",
                                    "description": "Led development of microservices architecture",
                                    "createdAt": "2024-01-15T10:30:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Experience not found or not owned by user",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Experience not found or unauthorized",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<ExperienceDto>> updateExperience(
            @PathVariable Long experienceId,
            @Valid @RequestBody ExperienceDto experienceDto) {
        ExperienceDto updatedExperience = profileService.updateExperience(experienceId, experienceDto);
        return ResponseEntity.ok(
                new ApiResponse<>("Experience updated successfully", "200", updatedExperience)
        );
    }

    // Interest endpoints
    @GetMapping("/interests/{userId}")
    @Operation(summary = "Get User Interests", description = "Get user interests by userId")
    public ResponseEntity<ApiResponse<List<InterestDto>>> getInterestsByUserId(@PathVariable String userId) {
        List<InterestDto> interests = profileService.getInterestsByUserId(userId);
        return ResponseEntity.ok(
                new ApiResponse<>("Interests fetched successfully", "200", interests)
        );
    }

    @PostMapping("/interests")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("user.interests")
    @Operation(
        summary = "Add Interest",
        description = "Add a new interest to user profile (USER role required)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Interest details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InterestRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Add Interest",
                        summary = "Add a new interest",
                        value = """
                            {
                                "interest": "Machine Learning"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Add Another Interest",
                        summary = "Add another interest",
                        value = """
                            {
                                "interest": "Web Development"
                            }
                            """
                    )
                }
            )
        )
    )
    public ResponseEntity<ApiResponse<InterestDto>> addInterest(@Valid @RequestBody InterestRequest request) {
        InterestDto addedInterest = profileService.addInterest(request.getInterest());
        featureFlagUtils.logFeatureUsage(FeatureFlagUtils.USER_INTERESTS, "user");
        return ResponseEntity.ok(
                new ApiResponse<>("Interest added successfully", "200", addedInterest)
        );
    }

    @DeleteMapping("/interests/{interestId}")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("user.interests")
    @Operation(
        summary = "Delete Interest", 
        description = "Delete a specific interest by ID (USER role required)",
        parameters = {
            @Parameter(name = "interestId", description = "ID of the interest to delete", required = true)
        }
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Interest deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Interest deleted successfully",
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
            description = "Interest not found or not owned by user",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Interest not found or unauthorized",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteInterest(@PathVariable Long interestId) {
        profileService.deleteInterest(interestId);
        return ResponseEntity.ok(
                new ApiResponse<>("Interest deleted successfully", "200", null)
        );
    }

    @PutMapping("/interests/{interestId}")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("user.interests")
    @Operation(
        summary = "Update Interest", 
        description = "Update a specific interest by ID (USER role required)",
        parameters = {
            @Parameter(name = "interestId", description = "ID of the interest to update", required = true)
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Interest update details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = InterestRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Update Interest",
                        summary = "Update interest name",
                        value = """
                            {
                                "interest": "Artificial Intelligence"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Update to Different Interest",
                        summary = "Change interest completely",
                        value = """
                            {
                                "interest": "Data Science"
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
            description = "Interest updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Interest updated successfully",
                                "code": "200",
                                "data": {
                                    "id": 1,
                                    "interest": "Artificial Intelligence",
                                    "createdAt": "2024-01-15T10:30:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Interest not found or not owned by user",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Interest not found or unauthorized",
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
            description = "Interest already exists or invalid input",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Bad Request",
                        value = """
                            {
                                "message": "Interest already exists",
                                "code": "400",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<InterestDto>> updateInterest(
            @PathVariable Long interestId,
            @Valid @RequestBody InterestRequest request) {
        InterestDto updatedInterest = profileService.updateInterest(interestId, request.getInterest());
        return ResponseEntity.ok(
                new ApiResponse<>("Interest updated successfully", "200", updatedInterest)
        );
    }
}
