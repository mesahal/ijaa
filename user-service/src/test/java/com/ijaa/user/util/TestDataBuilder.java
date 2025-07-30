package com.ijaa.user.util;

import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.InterestDto;
import com.ijaa.user.domain.dto.ProfileDto;
import com.ijaa.user.domain.entity.Experience;
import com.ijaa.user.domain.entity.Interest;
import com.ijaa.user.domain.entity.Profile;
import com.ijaa.user.domain.entity.User;
import com.ijaa.user.domain.request.SignInRequest;
import com.ijaa.user.domain.request.SignUpRequest;
import com.ijaa.user.domain.response.AuthResponse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestDataBuilder {

    // User related test data
    public static User createTestUser() {
        return new User(null, "USER_ABC123XYZ", "testuser", "encodedPassword");
    }

    public static User createTestUser(String userId, String username) {
        return new User(null, userId, username, "encodedPassword");
    }

    public static SignUpRequest createSignUpRequest() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        return request;
    }

    public static SignUpRequest createSignUpRequest(String username, String password) {
        SignUpRequest request = new SignUpRequest();
        request.setUsername(username);
        request.setPassword(password);
        return request;
    }

    public static SignInRequest createSignInRequest() {
        SignInRequest request = new SignInRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        return request;
    }

    public static SignInRequest createSignInRequest(String username, String password) {
        SignInRequest request = new SignInRequest();
        request.setUsername(username);
        request.setPassword(password);
        return request;
    }

    public static AuthResponse createAuthResponse() {
        return new AuthResponse("test.jwt.token", "USER_ABC123XYZ");
    }

    public static AuthResponse createAuthResponse(String token, String userId) {
        return new AuthResponse(token, userId);
    }

    // Profile related test data
    public static Profile createTestProfile() {
        return Profile.builder()
                .id(1L)
                .username("testuser")
                .userId("USER_ABC123XYZ")
                .name("Test User")
                .profession("Software Engineer")
                .location("Dhaka, Bangladesh")
                .bio("A passionate software engineer")
                .phone("+8801234567890")
                .linkedIn("linkedin.com/in/testuser")
                .website("testuser.com")
                .batch("2018")
                .email("test@example.com")
                .facebook("facebook.com/testuser")
                .showPhone(true)
                .showLinkedIn(true)
                .showWebsite(true)
                .showEmail(true)
                .showFacebook(false)
                .connections(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static ProfileDto createTestProfileDto() {
        ProfileDto dto = new ProfileDto();
        dto.setUserId("USER_ABC123XYZ");
        dto.setName("Test User");
        dto.setProfession("Software Engineer");
        dto.setLocation("Dhaka, Bangladesh");
        dto.setBio("A passionate software engineer");
        dto.setPhone("+8801234567890");
        dto.setLinkedIn("linkedin.com/in/testuser");
        dto.setWebsite("testuser.com");
        dto.setBatch("2018");
        dto.setEmail("test@example.com");
        dto.setFacebook("facebook.com/testuser");
        dto.setShowPhone(true);
        dto.setShowLinkedIn(true);
        dto.setShowWebsite(true);
        dto.setShowEmail(true);
        dto.setShowFacebook(false);
        dto.setConnections(10);
        return dto;
    }

    // Experience related test data
    public static Experience createTestExperience() {
        return Experience.builder()
                .id(1L)
                .username("testuser")
                .userId("USER_ABC123XYZ")
                .title("Senior Software Engineer")
                .company("Tech Corp")
                .period("2020-2023")
                .description("Led development of microservices")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ExperienceDto createTestExperienceDto() {
        ExperienceDto dto = new ExperienceDto();
        dto.setId(1L);
        dto.setUserId("USER_ABC123XYZ");
        dto.setTitle("Senior Software Engineer");
        dto.setCompany("Tech Corp");
        dto.setLocation("Dhaka, Bangladesh");
        dto.setStartDate(java.time.LocalDate.of(2020, 1, 1));
        dto.setPeriod("2020-2023");
        dto.setDescription("Led development of microservices");
        dto.setCreatedAt(java.time.LocalDateTime.now());
        return dto;
    }

    public static List<Experience> createTestExperienceList() {
        return Arrays.asList(
                createTestExperience(),
                Experience.builder()
                        .id(2L)
                        .username("testuser")
                        .userId("USER_ABC123XYZ")
                        .title("Software Engineer")
                        .company("Startup Inc")
                        .period("2018-2020")
                        .description("Full-stack development")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    // Interest related test data
    public static Interest createTestInterest() {
        return Interest.builder()
                .id(1L)
                .username("testuser")
                .userId("USER_ABC123XYZ")
                .interest("Java Programming")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static InterestDto createTestInterestDto() {
        InterestDto dto = new InterestDto();
        dto.setId(1L);
        dto.setUserId("USER_ABC123XYZ");
        dto.setInterest("Java Programming");
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    public static List<Interest> createTestInterestList() {
        return Arrays.asList(
                createTestInterest(),
                Interest.builder()
                        .id(2L)
                        .username("testuser")
                        .userId("USER_ABC123XYZ")
                        .interest("Spring Boot")
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    // Invalid test data
    public static SignUpRequest createInvalidSignUpRequest() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername(""); // Empty username
        request.setPassword("123"); // Too short password
        return request;
    }

    public static SignInRequest createInvalidSignInRequest() {
        SignInRequest request = new SignInRequest();
        request.setUsername(""); // Empty username
        request.setPassword(""); // Empty password
        return request;
    }
} 