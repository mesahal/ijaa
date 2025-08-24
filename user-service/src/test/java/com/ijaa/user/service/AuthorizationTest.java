package com.ijaa.user.service;

import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.dto.AlumniSearchDto;
import com.ijaa.user.domain.dto.ProfileDto;
import com.ijaa.user.domain.request.AlumniSearchRequest;
import com.ijaa.user.service.AlumniSearchService;
import com.ijaa.user.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthorizationTest {

    @Autowired
    private AlumniSearchService alumniSearchService;

    @Autowired
    private ProfileService profileService;

    @Test
    @WithMockUser(roles = "USER")
    void testAlumniSearchWithUserRole_ShouldSucceed() {
        // Given
        AlumniSearchRequest request = new AlumniSearchRequest();
        request.setSearchQuery("test");
        request.setPage(0);
        request.setSize(10);

        // When & Then
        assertDoesNotThrow(() -> {
            var result = alumniSearchService.searchAlumni(request);
            assertNotNull(result);
        });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAlumniSearchWithAdminRole_ShouldSucceed() {
        // Given
        AlumniSearchRequest request = new AlumniSearchRequest();
        request.setSearchQuery("test");
        request.setPage(0);
        request.setSize(10);

        // When & Then
        assertDoesNotThrow(() -> {
            var result = alumniSearchService.searchAlumni(request);
            assertNotNull(result);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testProfileUpdateWithUserRole_ShouldSucceed() {
        // Given
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUserId("testuser");
        profileDto.setName("Test User");
        profileDto.setProfession("Software Engineer");

        // When & Then
        assertDoesNotThrow(() -> {
            var result = profileService.updateBasicInfo(profileDto);
            assertNotNull(result);
        });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testProfileUpdateWithAdminRole_ShouldSucceed() {
        // Given
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUserId("testuser");
        profileDto.setName("Test User");
        profileDto.setProfession("Software Engineer");

        // When & Then
        assertDoesNotThrow(() -> {
            var result = profileService.updateBasicInfo(profileDto);
            assertNotNull(result);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testProfileVisibilityUpdateWithUserRole_ShouldSucceed() {
        // Given
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUserId("testuser");
        profileDto.setShowPhone(true);
        profileDto.setShowLinkedIn(true);

        // When & Then
        assertDoesNotThrow(() -> {
            var result = profileService.updateVisibility(profileDto);
            assertNotNull(result);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddExperienceWithUserRole_ShouldSucceed() {
        // Given
        var experienceDto = new com.ijaa.user.domain.dto.ExperienceDto();
        experienceDto.setUserId("testuser");
        experienceDto.setTitle("Software Engineer");
        experienceDto.setCompany("Test Company");

        // When & Then
        assertDoesNotThrow(() -> {
            var result = profileService.addExperience(experienceDto);
            assertNotNull(result);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddInterestWithUserRole_ShouldSucceed() {
        // When & Then
        assertDoesNotThrow(() -> {
            var result = profileService.addInterest("Programming");
            assertNotNull(result);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteExperienceWithUserRole_ShouldSucceed() {
        // When & Then
        assertDoesNotThrow(() -> {
            profileService.deleteExperience(1L);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteInterestWithUserRole_ShouldSucceed() {
        // When & Then
        assertDoesNotThrow(() -> {
            profileService.deleteInterest(1L);
        });
    }

    @Test
    void testPublicEndpoints_ShouldBeAccessibleWithoutAuthentication() {
        // Test that public endpoints like getting profile by userId are accessible
        assertDoesNotThrow(() -> {
            var result = profileService.getProfileByUserId("testuser");
            // This might return null if user doesn't exist, but shouldn't throw security exception
        });
    }

    @Test
    void testPublicEndpoints_ShouldBeAccessibleWithoutAuthentication_Experiences() {
        // Test that public endpoints like getting experiences are accessible
        assertDoesNotThrow(() -> {
            var result = profileService.getExperiencesByUserId("testuser");
            assertNotNull(result);
        });
    }

    @Test
    void testPublicEndpoints_ShouldBeAccessibleWithoutAuthentication_Interests() {
        // Test that public endpoints like getting interests are accessible
        assertDoesNotThrow(() -> {
            var result = profileService.getInterestsByUserId("testuser");
            assertNotNull(result);
        });
    }
}




