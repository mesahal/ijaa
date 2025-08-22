package com.ijaa.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.InterestDto;
import com.ijaa.user.domain.dto.ProfileDto;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

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
    private ProfileDto profileDto;
    private Experience testExperience;
    private Interest testInterest;
    private MockHttpServletRequest mockRequest;

    @BeforeEach
    void setUp() throws Exception {
        // Setup mock HTTP request context
        mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("X-USER_ID", "eyJ1c2VySWQiOiJVU0VSXzEyMzQ1NiIsInVzZXJuYW1lIjoidGVzdHVzZXIifQ==");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));

        // Mock ObjectMapper for user context decoding
        com.ijaa.user.domain.entity.CurrentUserContext mockUserContext = new com.ijaa.user.domain.entity.CurrentUserContext();
        mockUserContext.setUsername("testuser");
        lenient().when(objectMapper.readValue(any(String.class), eq(com.ijaa.user.domain.entity.CurrentUserContext.class)))
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
    void shouldGetProfileByUserIdWhenValidUserIdProvided() {
        // Given
        when(profileRepository.findByUserId("USER_123456")).thenReturn(Optional.of(testProfile));

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
        when(profileRepository.findByUsername("testuser")).thenReturn(Optional.of(testProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // When
        ProfileDto result = profileService.updateBasicInfo(profileDto);

        // Then
        assertNotNull(result);
        assertEquals("Jane Smith", result.getName());
        assertEquals("jane.smith@example.com", result.getEmail());
        assertEquals("Product Manager", result.getProfession());
        verify(profileRepository, atLeastOnce()).findByUsername("testuser");
        verify(profileRepository, atLeastOnce()).save(any(Profile.class));
    }

    @Test
    void shouldUpdateBasicInfoWhenProfileNotFoundCreatesNewProfile() {
        // Given
        when(profileRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // When
        ProfileDto result = profileService.updateBasicInfo(profileDto);

        // Then
        assertNotNull(result);
        verify(profileRepository, atLeastOnce()).findByUsername("testuser");
        verify(profileRepository, atLeastOnce()).save(any(Profile.class)); // Profile creation and update
    }

    @Test
    void shouldUpdateVisibilityWhenValidProfileDtoProvided() {
        // Given
        when(profileRepository.findByUsername("testuser")).thenReturn(Optional.of(testProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

        // When
        ProfileDto result = profileService.updateVisibility(profileDto);

        // Then
        assertNotNull(result);
        assertTrue(result.getShowPhone());
        assertTrue(result.getShowEmail());
        verify(profileRepository, atLeastOnce()).findByUsername("testuser");
        verify(profileRepository, atLeastOnce()).save(any(Profile.class));
    }

    // Experience Tests
    @Test
    void shouldAddExperienceWhenValidExperienceDtoProvided() {
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
        assertEquals("Software Engineer", result.getTitle());
        assertEquals("Tech Corp", result.getCompany());
        verify(userRepository, atLeastOnce()).findByUsername("testuser");
        verify(experienceRepository).save(any(Experience.class));
    }

    @Test
    void shouldAddExperienceWithoutRequiringProfile() {
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
        assertEquals("Software Engineer", result.getTitle());
        verify(userRepository, atLeastOnce()).findByUsername("testuser");
        verify(experienceRepository).save(any(Experience.class));
    }

    @Test
    void shouldGetExperiencesByUserIdWhenValidUserIdProvided() {
        // Given
        List<Experience> experiences = Arrays.asList(testExperience);
        when(experienceRepository.findByUserIdOrderByCreatedAtDesc("USER_123456")).thenReturn(experiences);

        // When
        List<ExperienceDto> result = profileService.getExperiencesByUserId("USER_123456");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Software Engineer", result.get(0).getTitle());
        verify(experienceRepository).findByUserIdOrderByCreatedAtDesc("USER_123456");
    }

    // Interest Tests
    @Test
    void shouldAddInterestWhenValidInterestNameProvided() {
        // Given
        when(interestRepository.existsByUserIdAndInterestIgnoreCase("USER_123456", "Programming")).thenReturn(false);
        when(interestRepository.save(any(Interest.class))).thenReturn(testInterest);

        // When
        InterestDto result = profileService.addInterest("Programming");

        // Then
        assertNotNull(result);
        assertEquals("Programming", result.getInterest());
        verify(userRepository, atLeastOnce()).findByUsername("testuser");
        verify(interestRepository).existsByUserIdAndInterestIgnoreCase("USER_123456", "Programming");
        verify(interestRepository).save(any(Interest.class));
    }

    @Test
    void shouldAddInterestWithoutRequiringProfile() {
        // Given
        when(interestRepository.existsByUserIdAndInterestIgnoreCase("USER_123456", "Programming")).thenReturn(false);
        when(interestRepository.save(any(Interest.class))).thenReturn(testInterest);

        // When
        InterestDto result = profileService.addInterest("Programming");

        // Then
        assertNotNull(result);
        assertEquals("Programming", result.getInterest());
        verify(userRepository, atLeastOnce()).findByUsername("testuser");
        verify(interestRepository).save(any(Interest.class));
    }

    @Test
    void shouldThrowExceptionWhenInterestAlreadyExists() {
        // Given
        when(interestRepository.existsByUserIdAndInterestIgnoreCase("USER_123456", "Programming")).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            profileService.addInterest("Programming");
        });
        verify(userRepository, atLeastOnce()).findByUsername("testuser");
        verify(interestRepository).existsByUserIdAndInterestIgnoreCase("USER_123456", "Programming");
        verify(interestRepository, never()).save(any(Interest.class));
    }

    @Test
    void shouldThrowExceptionWhenInterestIsEmpty() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            profileService.addInterest("");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            profileService.addInterest(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            profileService.addInterest("   ");
        });
    }

    @Test
    void shouldGetInterestsByUserIdWhenValidUserIdProvided() {
        // Given
        List<Interest> interests = Arrays.asList(testInterest);
        when(interestRepository.findByUserIdOrderByCreatedAtDesc("USER_123456")).thenReturn(interests);

        // When
        List<InterestDto> result = profileService.getInterestsByUserId("USER_123456");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Programming", result.get(0).getInterest());
        verify(interestRepository).findByUserIdOrderByCreatedAtDesc("USER_123456");
    }

    // Delete Tests
    @Test
    void shouldDeleteExperienceWhenValidExperienceIdProvided() {
        // Given
        when(experienceRepository.findByIdAndUserId(1L, "USER_123456")).thenReturn(Optional.of(testExperience));

        // When
        profileService.deleteExperience(1L);

        // Then
        verify(experienceRepository).findByIdAndUserId(1L, "USER_123456");
        verify(experienceRepository).delete(testExperience);
    }

    @Test
    void shouldThrowExceptionWhenDeletingExperienceNotFound() {
        // Given
        when(experienceRepository.findByIdAndUserId(999L, "USER_123456")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            profileService.deleteExperience(999L);
        });
        assertEquals("Experience not found or unauthorized", exception.getMessage());
        verify(experienceRepository).findByIdAndUserId(999L, "USER_123456");
        verify(experienceRepository, never()).delete(any(Experience.class));
    }

    @Test
    void shouldDeleteInterestWhenValidInterestIdProvided() {
        // Given
        when(interestRepository.findByIdAndUserId(1L, "USER_123456")).thenReturn(Optional.of(testInterest));

        // When
        profileService.deleteInterest(1L);

        // Then
        verify(interestRepository).findByIdAndUserId(1L, "USER_123456");
        verify(interestRepository).delete(testInterest);
    }

    @Test
    void shouldThrowExceptionWhenDeletingInterestNotFound() {
        // Given
        when(interestRepository.findByIdAndUserId(999L, "USER_123456")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            profileService.deleteInterest(999L);
        });
        assertEquals("Interest not found or unauthorized", exception.getMessage());
        verify(interestRepository).findByIdAndUserId(999L, "USER_123456");
        verify(interestRepository, never()).delete(any(Interest.class));
    }
}
