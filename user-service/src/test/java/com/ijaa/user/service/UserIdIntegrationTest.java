package com.ijaa.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.InterestDto;
import com.ijaa.user.domain.dto.ProfileDto;
import com.ijaa.user.domain.entity.CurrentUserContext;
import com.ijaa.user.domain.entity.Experience;
import com.ijaa.user.domain.entity.Interest;
import com.ijaa.user.domain.entity.Profile;
import com.ijaa.user.domain.entity.User;
import com.ijaa.user.repository.ExperienceRepository;
import com.ijaa.user.repository.InterestRepository;
import com.ijaa.user.repository.ProfileRepository;
import com.ijaa.user.repository.UserRepository;
import com.ijaa.user.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserIdIntegrationTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private InterestRepository interestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private Profile testProfile;
    private Experience testExperience;
    private Interest testInterest;
    private MockHttpServletRequest mockRequest;

    @BeforeEach
    void setUp() throws Exception {
        // Setup mock HTTP request context with user ID
        mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("X-USER_ID", "eyJ1c2VySWQiOiJVU0VSXzEyMzQ1NiIsInVzZXJuYW1lIjoidGVzdHVzZXIifQ==");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        // Mock ObjectMapper for user context decoding with user ID
        CurrentUserContext mockUserContext = new CurrentUserContext();
        mockUserContext.setUsername("testuser");
        mockUserContext.setUserId("USER_123456");
        lenient().when(objectMapper.readValue(any(String.class), eq(CurrentUserContext.class)))
                .thenReturn(mockUserContext);

        // Mock UserRepository
        User testUser = new User();
        testUser.setUserId("USER_123456");
        testUser.setUsername("testuser");
        lenient().when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        testProfile = new Profile();
        testProfile.setId(1L);
        testProfile.setUserId("USER_123456");
        testProfile.setUsername("testuser");
        testProfile.setName("John Doe");
        testProfile.setEmail("john.doe@example.com");
        testProfile.setPhone("+1234567890");
        testProfile.setBio("Software Engineer");
        testProfile.setLocation("New York");
        testProfile.setProfession("Software Engineer");
        testProfile.setLinkedIn("linkedin.com/johndoe");
        testProfile.setWebsite("johndoe.com");
        testProfile.setBatch("2020");
        testProfile.setShowPhone(true);
        testProfile.setShowEmail(true);
        testProfile.setConnections(10);

        testExperience = new Experience();
        testExperience.setId(1L);
        testExperience.setUserId("USER_123456");
        testExperience.setUsername("testuser");
        testExperience.setTitle("Software Engineer");
        testExperience.setCompany("Tech Corp");
        testExperience.setPeriod("2020-2024");
        testExperience.setDescription("Developed web applications");

        testInterest = new Interest();
        testInterest.setId(1L);
        testInterest.setUserId("USER_123456");
        testInterest.setUsername("testuser");
        testInterest.setInterest("Programming");
    }

    @Test
    void shouldUseUserIdForProfileOperations() {
        // Given
        when(profileRepository.findByUserId("USER_123456")).thenReturn(Optional.of(testProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        ProfileDto profileDto = new ProfileDto();
        profileDto.setName("Jane Smith");
        profileDto.setEmail("jane.smith@example.com");

        // When
        ProfileDto result = profileService.updateBasicInfo(profileDto);

        // Then
        assertNotNull(result);
        verify(profileRepository).findByUserId("USER_123456"); // Should use userId, not username
        verify(profileRepository, never()).findByUsername(anyString()); // Should not use username
    }

    @Test
    void shouldUseUserIdForExperienceOperations() {
        // Given
        when(experienceRepository.save(any(Experience.class))).thenReturn(testExperience);

        ExperienceDto experienceDto = new ExperienceDto();
        experienceDto.setTitle("Software Engineer");
        experienceDto.setCompany("Tech Corp");
        experienceDto.setPeriod("2020-2024");
        experienceDto.setDescription("Developed web applications");

        // When
        ExperienceDto result = profileService.addExperience(experienceDto);

        // Then
        assertNotNull(result);
        assertEquals("USER_123456", result.getUserId()); // Should use userId from context
        verify(experienceRepository).save(any(Experience.class));
    }

    @Test
    void shouldUseUserIdForExperienceDeletion() {
        // Given
        when(experienceRepository.findByIdAndUserId(1L, "USER_123456")).thenReturn(Optional.of(testExperience));

        // When
        profileService.deleteExperience(1L);

        // Then
        verify(experienceRepository).findByIdAndUserId(1L, "USER_123456"); // Should use userId
        verify(experienceRepository).delete(testExperience);
    }

    @Test
    void shouldUseUserIdForInterestOperations() {
        // Given
        when(interestRepository.save(any(Interest.class))).thenReturn(testInterest);
        when(interestRepository.existsByUserIdAndInterestIgnoreCase("USER_123456", "Programming")).thenReturn(false);

        // When
        InterestDto result = profileService.addInterest("Programming");

        // Then
        assertNotNull(result);
        assertEquals("USER_123456", result.getUserId()); // Should use userId from context
        verify(interestRepository).save(any(Interest.class));
    }

    @Test
    void shouldUseUserIdForInterestDeletion() {
        // Given
        when(interestRepository.findByIdAndUserId(1L, "USER_123456")).thenReturn(Optional.of(testInterest));

        // When
        profileService.deleteInterest(1L);

        // Then
        verify(interestRepository).findByIdAndUserId(1L, "USER_123456"); // Should use userId
        verify(interestRepository).delete(testInterest);
    }

    @Test
    void shouldUseUserIdForProfileAuthorization() {
        // Given
        when(profileRepository.findByUserId("USER_123456")).thenReturn(Optional.of(testProfile));

        // When
        ProfileDto result = profileService.getProfileByUserId("USER_123456");

        // Then
        assertNotNull(result);
        // Should check if current user ID matches profile user ID for authorization
        verify(profileRepository).findByUserId("USER_123456");
    }

    @Test
    void shouldCreateNewProfileWithUserId() {
        // Given
        when(profileRepository.findByUserId("USER_123456")).thenReturn(Optional.empty());
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        ProfileDto profileDto = new ProfileDto();
        profileDto.setName("Jane Smith");

        // When
        ProfileDto result = profileService.updateBasicInfo(profileDto);

        // Then
        assertNotNull(result);
        verify(profileRepository).findByUserId("USER_123456"); // Should use userId to find profile
        verify(profileRepository, times(2)).save(any(Profile.class)); // Called twice: once in createNewProfile, once in updateBasicInfo
        
        // Verify that at least one of the saved profiles has the correct userId
        verify(profileRepository, atLeastOnce()).save(argThat(profile -> 
            "USER_123456".equals(profile.getUserId()) && 
            "testuser".equals(profile.getUsername())
        ));
    }

    @Test
    void shouldUseUserIdForExperienceUpdate() {
        // Given
        when(experienceRepository.findByIdAndUserId(1L, "USER_123456")).thenReturn(Optional.of(testExperience));
        when(experienceRepository.save(any(Experience.class))).thenReturn(testExperience);

        ExperienceDto experienceDto = new ExperienceDto();
        experienceDto.setTitle("Senior Software Engineer");
        experienceDto.setCompany("New Tech Corp");

        // When
        ExperienceDto result = profileService.updateExperience(1L, experienceDto);

        // Then
        assertNotNull(result);
        verify(experienceRepository).findByIdAndUserId(1L, "USER_123456"); // Should use userId
        verify(experienceRepository).save(any(Experience.class));
    }

    @Test
    void shouldUseUserIdForInterestUpdate() {
        // Given
        when(interestRepository.findByIdAndUserId(1L, "USER_123456")).thenReturn(Optional.of(testInterest));
        when(interestRepository.existsByUserIdAndInterestIgnoreCase("USER_123456", "New Interest")).thenReturn(false);
        when(interestRepository.save(any(Interest.class))).thenReturn(testInterest);

        // When
        InterestDto result = profileService.updateInterest(1L, "New Interest");

        // Then
        assertNotNull(result);
        verify(interestRepository).findByIdAndUserId(1L, "USER_123456"); // Should use userId
        verify(interestRepository).existsByUserIdAndInterestIgnoreCase("USER_123456", "New Interest");
        verify(interestRepository).save(any(Interest.class));
    }
}
