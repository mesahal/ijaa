package com.ijaa.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.InterestDto;
import com.ijaa.user.domain.dto.ProfileDto;
import com.ijaa.user.domain.entity.Experience;
import com.ijaa.user.domain.entity.Interest;
import com.ijaa.user.domain.entity.Profile;
import com.ijaa.user.repository.ExperienceRepository;
import com.ijaa.user.repository.InterestRepository;
import com.ijaa.user.repository.ProfileRepository;
import com.ijaa.user.service.impl.ProfileServiceImpl;
import com.ijaa.user.util.TestDataBuilder;
import com.ijaa.user.util.TestSecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("ProfileService Unit Tests")
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private InterestRepository interestRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Spy
    @InjectMocks
    private ProfileServiceImpl profileService;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_USER_ID = "USER_ABC123XYZ";

    @BeforeEach
    void setUp() {
        Mockito.doReturn(TEST_USERNAME).when(profileService).getCurrentUsername();
        Mockito.doReturn(TEST_USER_ID).when(profileService).getCurrentUserId();
    }

    @Test
    @DisplayName("Should get profile by user ID successfully")
    void getProfileByUserId_Success() {
        // Arrange
        Profile profile = TestDataBuilder.createTestProfile();
        when(profileRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(profile));

        // Act
        ProfileDto result = profileService.getProfileByUserId(TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());
        assertEquals("Test User", result.getName());
        assertEquals("Software Engineer", result.getProfession());

        verify(profileRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should throw RuntimeException when profile not found")
    void getProfileByUserId_ProfileNotFound() {
        // Arrange
        when(profileRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> profileService.getProfileByUserId(TEST_USER_ID)
        );

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should update basic info successfully")
    void updateBasicInfo_Success() {
        // Arrange
        ProfileDto requestDto = TestDataBuilder.createTestProfileDto();
        Profile existingProfile = TestDataBuilder.createTestProfile();
        
        when(profileRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(existingProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(existingProfile);

        // Act
        ProfileDto result = profileService.updateBasicInfo(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());

        verify(profileRepository).findByUsername(TEST_USERNAME);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("Should create new profile when profile doesn't exist")
    void updateBasicInfo_CreateNewProfile() {
        // Arrange
        ProfileDto requestDto = TestDataBuilder.createTestProfileDto();
        Profile newProfile = TestDataBuilder.createTestProfile();
        
        when(profileRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());
        when(profileRepository.save(any(Profile.class))).thenReturn(newProfile);

        // Act
        ProfileDto result = profileService.updateBasicInfo(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());

        verify(profileRepository).findByUsername(TEST_USERNAME);
        verify(profileRepository, times(2)).save(any(Profile.class));
    }

    @Test
    @DisplayName("Should update visibility settings successfully")
    void updateVisibility_Success() {
        // Arrange
        ProfileDto requestDto = TestDataBuilder.createTestProfileDto();
        Profile existingProfile = TestDataBuilder.createTestProfile();
        
        when(profileRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(existingProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(existingProfile);

        // Act
        ProfileDto result = profileService.updateVisibility(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());

        verify(profileRepository).findByUsername(TEST_USERNAME);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when profile not found for visibility update")
    void updateVisibility_ProfileNotFound() {
        // Arrange
        ProfileDto requestDto = TestDataBuilder.createTestProfileDto();
        when(profileRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> profileService.updateVisibility(requestDto)
        );

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepository).findByUsername(TEST_USERNAME);
        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    @DisplayName("Should get experiences by user ID successfully")
    void getExperiencesByUserId_Success() {
        // Arrange
        List<Experience> experiences = TestDataBuilder.createTestExperienceList();
        when(experienceRepository.findByUserIdOrderByCreatedAtDesc(TEST_USER_ID)).thenReturn(experiences);

        // Act
        List<ExperienceDto> result = profileService.getExperiencesByUserId(TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Senior Software Engineer", result.get(0).getTitle());
        assertEquals("Software Engineer", result.get(1).getTitle());

        verify(experienceRepository).findByUserIdOrderByCreatedAtDesc(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should add experience successfully")
    void addExperience_Success() {
        // Arrange
        ExperienceDto requestDto = TestDataBuilder.createTestExperienceDto();
        Experience savedExperience = TestDataBuilder.createTestExperience();
        
        when(experienceRepository.save(any(Experience.class))).thenReturn(savedExperience);

        // Act
        ExperienceDto result = profileService.addExperience(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());
        assertEquals("Senior Software Engineer", result.getTitle());

        verify(experienceRepository).save(any(Experience.class));
    }

    @Test
    @DisplayName("Should delete experience successfully")
    void deleteExperience_Success() {
        // Arrange
        Experience experience = TestDataBuilder.createTestExperience();
        when(experienceRepository.findByUserIdAndUserId(TEST_USER_ID, TEST_USER_ID))
                .thenReturn(Optional.of(experience));

        // Act
        profileService.deleteExperience(TEST_USER_ID);

        // Assert
        verify(experienceRepository).findByUserIdAndUserId(TEST_USER_ID, TEST_USER_ID);
        verify(experienceRepository).delete(experience);
    }

    @Test
    @DisplayName("Should throw RuntimeException when experience not found for deletion")
    void deleteExperience_ExperienceNotFound() {
        // Arrange
        when(experienceRepository.findByUserIdAndUserId(TEST_USER_ID, TEST_USER_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> profileService.deleteExperience(TEST_USER_ID)
        );

        assertEquals("Experience not found or unauthorized", exception.getMessage());
        verify(experienceRepository).findByUserIdAndUserId(TEST_USER_ID, TEST_USER_ID);
        verify(experienceRepository, never()).delete(any(Experience.class));
    }

    @Test
    @DisplayName("Should get interests by user ID successfully")
    void getInterestsByUserId_Success() {
        // Arrange
        List<Interest> interests = TestDataBuilder.createTestInterestList();
        when(interestRepository.findByUserIdOrderByCreatedAtDesc(TEST_USER_ID)).thenReturn(interests);

        // Act
        List<InterestDto> result = profileService.getInterestsByUserId(TEST_USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Java Programming", result.get(0).getInterest());
        assertEquals("Spring Boot", result.get(1).getInterest());

        verify(interestRepository).findByUserIdOrderByCreatedAtDesc(TEST_USER_ID);
    }

    @Test
    @DisplayName("Should add interest successfully")
    void addInterest_Success() {
        // Arrange
        String interestName = "Python Programming";
        Interest savedInterest = Interest.builder()
                .id(1L)
                .username(TEST_USERNAME)
                .userId(TEST_USER_ID)
                .interest(interestName)
                .build();
        
        when(interestRepository.existsByUserIdAndInterestIgnoreCase(TEST_USER_ID, interestName))
                .thenReturn(false);
        when(interestRepository.save(any(Interest.class))).thenReturn(savedInterest);

        // Act
        InterestDto result = profileService.addInterest(interestName);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());
        assertEquals(interestName, result.getInterest());

        verify(interestRepository).existsByUserIdAndInterestIgnoreCase(TEST_USER_ID, interestName);
        verify(interestRepository).save(any(Interest.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when interest is null")
    void addInterest_NullInterest() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> profileService.addInterest(null)
        );

        assertEquals("Interest cannot be empty", exception.getMessage());
        verify(interestRepository, never()).save(any(Interest.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when interest is empty")
    void addInterest_EmptyInterest() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> profileService.addInterest("")
        );

        assertEquals("Interest cannot be empty", exception.getMessage());
        verify(interestRepository, never()).save(any(Interest.class));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when interest already exists")
    void addInterest_InterestAlreadyExists() {
        // Arrange
        String interestName = "Java Programming";
        when(interestRepository.existsByUserIdAndInterestIgnoreCase(TEST_USER_ID, interestName))
                .thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> profileService.addInterest(interestName)
        );

        assertEquals("Interest already exists", exception.getMessage());
        verify(interestRepository).existsByUserIdAndInterestIgnoreCase(TEST_USER_ID, interestName);
        verify(interestRepository, never()).save(any(Interest.class));
    }

    @Test
    @DisplayName("Should delete interest successfully")
    void deleteInterest_Success() {
        // Arrange
        Interest interest = TestDataBuilder.createTestInterest();
        when(interestRepository.findByUserIdAndUserId(TEST_USER_ID, TEST_USER_ID))
                .thenReturn(Optional.of(interest));

        // Act
        profileService.deleteInterest(TEST_USER_ID);

        // Assert
        verify(interestRepository).findByUserIdAndUserId(TEST_USER_ID, TEST_USER_ID);
        verify(interestRepository).delete(interest);
    }

    @Test
    @DisplayName("Should throw RuntimeException when interest not found for deletion")
    void deleteInterest_InterestNotFound() {
        // Arrange
        when(interestRepository.findByUserIdAndUserId(TEST_USER_ID, TEST_USER_ID))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> profileService.deleteInterest(TEST_USER_ID)
        );

        assertEquals("Interest not found or unauthorized", exception.getMessage());
        verify(interestRepository).findByUserIdAndUserId(TEST_USER_ID, TEST_USER_ID);
        verify(interestRepository, never()).delete(any(Interest.class));
    }
} 