package com.ijaa.user.service;

import com.ijaa.user.domain.dto.ProfileDto;
import com.ijaa.user.domain.entity.Profile;
import com.ijaa.user.domain.entity.User;
import com.ijaa.user.repository.ProfileRepository;
import com.ijaa.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProfileService profileService;

    private User testUser;
    private Profile testProfile;
    private ProfileDto profileDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUserId("USER_123456");
        testUser.setUsername("testuser");
        testUser.setActive(true);

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

        profileDto = new ProfileDto();
        profileDto.setUserId("USER_123456");
        profileDto.setName("Jane Smith");
        profileDto.setEmail("jane.smith@example.com");
        profileDto.setPhone("+0987654321");
        profileDto.setBio("Product Manager");
        profileDto.setLocation("San Francisco");
        profileDto.setProfession("Product Manager");
        profileDto.setLinkedIn("linkedin.com/janesmith");
        profileDto.setWebsite("janesmith.com");
        profileDto.setBatch("2019");
        profileDto.setShowPhone(true);
        profileDto.setShowEmail(true);
        profileDto.setConnections(15);
    }

    @Test
    void shouldGetProfileByUserIdWhenValidUserIdProvided() {
        // Given
        when(profileRepository.findByUserId("USER_123456")).thenReturn(java.util.Optional.of(testProfile));

        // When
        ProfileDto result = profileService.getProfileByUserId("USER_123456");

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("Software Engineer", result.getProfession());
        verify(profileRepository).findByUserId("USER_123456");
    }

    @Test
    void shouldUpdateBasicInfoWhenValidProfileDtoProvided() {
        // Given
        when(profileRepository.findByUserId("USER_123456")).thenReturn(java.util.Optional.of(testProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // When
        ProfileDto result = profileService.updateBasicInfo(profileDto);

        // Then
        assertNotNull(result);
        assertEquals("Jane Smith", result.getName());
        assertEquals("jane.smith@example.com", result.getEmail());
        assertEquals("Product Manager", result.getProfession());
        verify(profileRepository).findByUserId("USER_123456");
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void shouldUpdateVisibilityWhenValidProfileDtoProvided() {
        // Given
        when(profileRepository.findByUserId("USER_123456")).thenReturn(java.util.Optional.of(testProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // When
        ProfileDto result = profileService.updateVisibility(profileDto);

        // Then
        assertNotNull(result);
        assertTrue(result.getShowPhone());
        assertTrue(result.getShowEmail());
        verify(profileRepository).findByUserId("USER_123456");
        verify(profileRepository).save(any(Profile.class));
    }
}
