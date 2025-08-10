// ProfileResource.java
package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.utils.AppUtils;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.InterestDto;
import com.ijaa.user.domain.dto.ProfileDto;
import com.ijaa.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import com.ijaa.user.domain.request.InterestRequest;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@RequiredArgsConstructor
@Tag(name = "User Profile Management", description = "APIs for user profile management")
public class ProfileResource {

    private final ProfileService profileService;

    // Get user's profile by userId
    @GetMapping("/profile/{userId}")
    @Operation(summary = "Get User Profile", description = "Get user profile by userId")
    public ResponseEntity<ApiResponse<ProfileDto>> getProfileByUserId(
            @PathVariable String userId) {
        ProfileDto profileDto = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(
                new ApiResponse<>("Profile fetched successfully", "200", profileDto)
        );
    }

    @PutMapping("/basic")
    @Operation(
        summary = "Update Basic Profile Info",
        description = "Update basic profile information",
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
                                "bio": "Passionate software engineer with 5+ years of experience",
                                "phone": "+1-555-123-4567",
                                "linkedIn": "https://linkedin.com/in/johndoe",
                                "website": "https://johndoe.com",
                                "batch": "2018",
                                "facebook": "https://facebook.com/johndoe",
                                "email": "john.doe@example.com"
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
    @Operation(
        summary = "Update Profile Visibility",
        description = "Update profile visibility settings",
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
    @Operation(
        summary = "Add Experience",
        description = "Add a new experience to user profile",
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
        return ResponseEntity.ok(
                new ApiResponse<>("Experience added successfully", "200", addedExperience)
        );
    }

    @DeleteMapping("/experiences/{userId}")
    @Operation(summary = "Delete Experience", description = "Delete user experience")
    public ResponseEntity<ApiResponse<Void>> deleteExperience(@PathVariable String userId) {
        profileService.deleteExperience(userId);
        return ResponseEntity.ok(
                new ApiResponse<>("Experience deleted successfully", "200", null)
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
    @Operation(
        summary = "Add Interest",
        description = "Add a new interest to user profile",
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
        return ResponseEntity.ok(
                new ApiResponse<>("Interest added successfully", "200", addedInterest)
        );
    }

    @DeleteMapping("/interests/{userId}")
    @Operation(summary = "Delete Interest", description = "Delete user interest")
    public ResponseEntity<ApiResponse<Void>> deleteInterest(@PathVariable String userId) {
        profileService.deleteInterest(userId);
        return ResponseEntity.ok(
                new ApiResponse<>("Interest deleted successfully", "200", null)
        );
    }
}
