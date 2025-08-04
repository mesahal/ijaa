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

@RestController
@RequestMapping(AppUtils.BASE_URL)
@RequiredArgsConstructor
public class ProfileResource {

    private final ProfileService profileService;

    // Get user's profile by userId
    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse<ProfileDto>> getProfileByUserId(
            @PathVariable String userId) {
        ProfileDto profileDto = profileService.getProfileByUserId(userId);
        return ResponseEntity.ok(
                new ApiResponse<>("Profile fetched successfully", "200", profileDto)
        );
    }

    @PutMapping("/basic")
    public ResponseEntity<ApiResponse<ProfileDto>> updateBasicInfo(@Valid @RequestBody ProfileDto profileDto) {
        ProfileDto updatedProfile = profileService.updateBasicInfo(profileDto);
        return ResponseEntity.ok(
                new ApiResponse<>("Profile updated successfully", "200", updatedProfile)
        );
    }

    @PutMapping("/visibility")
    public ResponseEntity<ApiResponse<ProfileDto>> updateVisibility(@Valid @RequestBody ProfileDto profileDto) {
        ProfileDto updatedProfile = profileService.updateVisibility(profileDto);
        return ResponseEntity.ok(
                new ApiResponse<>("Visibility updated successfully", "200", updatedProfile)
        );
    }

    // Experience endpoints
    @GetMapping("/experiences/{userId}")
    public ResponseEntity<ApiResponse<List<ExperienceDto>>> getExperiencesByUserId(@PathVariable String userId) {
        List<ExperienceDto> experiences = profileService.getExperiencesByUserId(userId);
        return ResponseEntity.ok(
                new ApiResponse<>("Experiences fetched successfully", "200", experiences)
        );
    }

    @PostMapping("/experiences")
    public ResponseEntity<ApiResponse<ExperienceDto>> addExperience(@Valid @RequestBody ExperienceDto experienceDto) {
        ExperienceDto addedExperience = profileService.addExperience(experienceDto);
        return ResponseEntity.ok(
                new ApiResponse<>("Experience added successfully", "200", addedExperience)
        );
    }

    @DeleteMapping("/experiences/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteExperience(@PathVariable String userId) {
        profileService.deleteExperience(userId);
        return ResponseEntity.ok(
                new ApiResponse<>("Experience deleted successfully", "200", null)
        );
    }

    // Interest endpoints
    @GetMapping("/interests/{userId}")
    public ResponseEntity<ApiResponse<List<InterestDto>>> getInterestsByUserId(@PathVariable String userId) {
        List<InterestDto> interests = profileService.getInterestsByUserId(userId);
        return ResponseEntity.ok(
                new ApiResponse<>("Interests fetched successfully", "200", interests)
        );
    }

    @PostMapping("/interests")
    public ResponseEntity<ApiResponse<InterestDto>> addInterest(@Valid @RequestBody InterestRequest request) {
        InterestDto addedInterest = profileService.addInterest(request.getInterest());
        return ResponseEntity.ok(
                new ApiResponse<>("Interest added successfully", "200", addedInterest)
        );
    }

    @DeleteMapping("/interests/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteInterest(@PathVariable String userId) {
        profileService.deleteInterest(userId);
        return ResponseEntity.ok(
                new ApiResponse<>("Interest deleted successfully", "200", null)
        );
    }
}
