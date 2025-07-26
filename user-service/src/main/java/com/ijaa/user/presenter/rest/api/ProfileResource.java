package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.utils.AppUtils;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.ProfileDto;
import com.ijaa.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppUtils.BASE_URL)
@RequiredArgsConstructor
public class ProfileResource {

    private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileDto>> getProfile() {
        ProfileDto profileDto = profileService.getProfile();
        return ResponseEntity.ok(
                new ApiResponse<>("Profile fetched successfully", "200", profileDto)
        );
    }

    @PutMapping("/basic")
    public ResponseEntity<ApiResponse<ProfileDto>> updateBasicInfo(@RequestBody ProfileDto profileDto) {
        ProfileDto updatedProfile = profileService.updateBasicInfo(profileDto);
        return ResponseEntity.ok(
                new ApiResponse<>("Profile updated successfully", "200", updatedProfile)
        );
    }

    @PutMapping("/visibility")
    public ResponseEntity<ApiResponse<ProfileDto>> updateVisibility(@RequestBody ProfileDto profileDto) {
        ProfileDto updatedProfile = profileService.updateVisibility(profileDto);
        return ResponseEntity.ok(
                new ApiResponse<>("Visibility updated successfully", "200", updatedProfile)
        );
    }

    // Experience endpoints
    @GetMapping("/experiences")
    public ResponseEntity<ApiResponse<List<ExperienceDto>>> getExperiences() {
        List<ExperienceDto> experiences = profileService.getExperiences();
        return ResponseEntity.ok(
                new ApiResponse<>("Experiences fetched successfully", "200", experiences)
        );
    }

    @PostMapping("/experiences")
    public ResponseEntity<ApiResponse<ExperienceDto>> addExperience(@RequestBody ExperienceDto experienceDto) {
        ExperienceDto addedExperience = profileService.addExperience(experienceDto);
        return ResponseEntity.ok(
                new ApiResponse<>("Experience added successfully", "200", addedExperience)
        );
    }

    @DeleteMapping("/experiences/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExperience(@PathVariable Long id) {
        profileService.deleteExperience(id);
        return ResponseEntity.ok(
                new ApiResponse<>("Experience deleted successfully", "200", null)
        );
    }
}
