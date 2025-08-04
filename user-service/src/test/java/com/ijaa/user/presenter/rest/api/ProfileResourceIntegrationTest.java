package com.ijaa.user.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.InterestDto;
import com.ijaa.user.domain.dto.ProfileDto;
import com.ijaa.user.service.ProfileService;
import com.ijaa.user.util.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.context.annotation.Import;
import com.ijaa.user.config.TestConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(ProfileResource.class)
@Import(TestConfig.class)
@DisplayName("ProfileResource Integration Tests")
class ProfileResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileService profileService;

    private static final String BASE_URL = "/api/v1/user";
    private static final String TEST_USER_ID = "USER_ABC123XYZ";

    @BeforeEach
    void setUp() {
        reset(profileService);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should get profile by user ID successfully")
    void getProfileByUserId_Success() throws Exception {
        // Arrange
        ProfileDto profileDto = TestDataBuilder.createTestProfileDto();
        when(profileService.getProfileByUserId(TEST_USER_ID)).thenReturn(profileDto);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/profile/{userId}", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Profile fetched successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.userId").value(TEST_USER_ID))
                .andExpect(jsonPath("$.data.name").value("Test User"))
                .andExpect(jsonPath("$.data.profession").value("Software Engineer"));

        verify(profileService).getProfileByUserId(TEST_USER_ID);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should update basic info successfully")
    void updateBasicInfo_Success() throws Exception {
        // Arrange
        ProfileDto requestDto = TestDataBuilder.createTestProfileDto();
        ProfileDto responseDto = TestDataBuilder.createTestProfileDto();
        when(profileService.updateBasicInfo(any(ProfileDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put(BASE_URL + "/basic")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Profile updated successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.userId").value(TEST_USER_ID));

        verify(profileService).updateBasicInfo(any(ProfileDto.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should update visibility settings successfully")
    void updateVisibility_Success() throws Exception {
        // Arrange
        ProfileDto requestDto = TestDataBuilder.createTestProfileDto();
        ProfileDto responseDto = TestDataBuilder.createTestProfileDto();
        when(profileService.updateVisibility(any(ProfileDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put(BASE_URL + "/visibility")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Visibility updated successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.userId").value(TEST_USER_ID));

        verify(profileService).updateVisibility(any(ProfileDto.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should get experiences by user ID successfully")
    void getExperiencesByUserId_Success() throws Exception {
        // Arrange
        List<ExperienceDto> experiences = Arrays.asList(
                TestDataBuilder.createTestExperienceDto(),
                TestDataBuilder.createTestExperienceDto()
        );
        when(profileService.getExperiencesByUserId(TEST_USER_ID)).thenReturn(experiences);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/experiences/{userId}", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Experiences fetched successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));

        verify(profileService).getExperiencesByUserId(TEST_USER_ID);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should add experience successfully")
    void addExperience_Success() throws Exception {
        // Arrange
        ExperienceDto requestDto = TestDataBuilder.createTestExperienceDto();
        ExperienceDto responseDto = TestDataBuilder.createTestExperienceDto();
        when(profileService.addExperience(any(ExperienceDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/experiences")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Experience added successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.userId").value(TEST_USER_ID));

        verify(profileService).addExperience(any(ExperienceDto.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should delete experience successfully")
    void deleteExperience_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/experiences/{userId}", TEST_USER_ID)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Experience deleted successfully"))
                .andExpect(jsonPath("$.code").value("200"));

        verify(profileService).deleteExperience(TEST_USER_ID);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should get interests by user ID successfully")
    void getInterestsByUserId_Success() throws Exception {
        // Arrange
        List<InterestDto> interests = Arrays.asList(
                TestDataBuilder.createTestInterestDto(),
                TestDataBuilder.createTestInterestDto()
        );
        when(profileService.getInterestsByUserId(TEST_USER_ID)).thenReturn(interests);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/interests/{userId}", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Interests fetched successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));

        verify(profileService).getInterestsByUserId(TEST_USER_ID);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should add interest successfully")
    void addInterest_Success() throws Exception {
        // Arrange
        String interestName = "Python Programming";
        InterestDto responseDto = TestDataBuilder.createTestInterestDto();
        when(profileService.addInterest(interestName)).thenReturn(responseDto);

        Map<String, String> request = Map.of("interest", interestName);

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/interests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Interest added successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.userId").value(TEST_USER_ID));

        verify(profileService).addInterest(interestName);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should delete interest successfully")
    void deleteInterest_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/interests/{userId}", TEST_USER_ID)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Interest deleted successfully"))
                .andExpect(jsonPath("$.code").value("200"));

        verify(profileService).deleteInterest(TEST_USER_ID);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when update basic info request is invalid")
    void updateBasicInfo_InvalidRequest() throws Exception {
        // Arrange
        ProfileDto invalidDto = new ProfileDto(); // All fields null/blank

        // Act & Assert
        mockMvc.perform(put(BASE_URL + "/basic")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(profileService, never()).updateBasicInfo(any(ProfileDto.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when add experience request is invalid")
    void addExperience_InvalidRequest() throws Exception {
        // Arrange
        String invalidJson = "{\"title\":\"\",\"company\":\"\"}"; // Empty fields

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/experiences")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(profileService, never()).addExperience(any(ExperienceDto.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when add interest request is missing interest field")
    void addInterest_MissingInterestField() throws Exception {
        // Arrange
        Map<String, String> request = Map.of("otherField", "value"); // Missing interest field

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/interests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(profileService, never()).addInterest(any());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when add interest request has empty interest")
    void addInterest_EmptyInterest() throws Exception {
        // Arrange
        Map<String, String> request = Map.of("interest", "");

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/interests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(profileService, never()).addInterest(any());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when request body is malformed JSON")
    void updateBasicInfo_MalformedJson() throws Exception {
        // Arrange
        String malformedJson = "{\"name\":\"Test User\",\"profession\":}"; // Invalid JSON

        // Act & Assert
        mockMvc.perform(put(BASE_URL + "/basic")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());

        verify(profileService, never()).updateBasicInfo(any(ProfileDto.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 415 when content type is not JSON")
    void updateBasicInfo_WrongContentType() throws Exception {
        // Arrange
        ProfileDto requestDto = TestDataBuilder.createTestProfileDto();

        // Act & Assert
        mockMvc.perform(put(BASE_URL + "/basic")
                        .with(csrf())
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isUnsupportedMediaType());

        verify(profileService, never()).updateBasicInfo(any(ProfileDto.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when service throws exception")
    void getProfileByUserId_ServiceException() throws Exception {
        // Arrange
        when(profileService.getProfileByUserId(TEST_USER_ID))
                .thenThrow(new RuntimeException("Profile not found"));

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/profile/{userId}", TEST_USER_ID))
                .andExpect(status().isNotFound());

        verify(profileService).getProfileByUserId(TEST_USER_ID);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Should return 400 when add interest service throws exception")
    void addInterest_ServiceException() throws Exception {
        // Arrange
        String interestName = "Java Programming";
        when(profileService.addInterest(interestName))
                .thenThrow(new IllegalArgumentException("Interest already exists"));

        Map<String, String> request = Map.of("interest", interestName);

        // Act & Assert
        mockMvc.perform(post(BASE_URL + "/interests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        verify(profileService).addInterest(interestName);
    }
} 