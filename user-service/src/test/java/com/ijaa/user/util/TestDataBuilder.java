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
        User user = new User();
        user.setUserId("USER_ABC123XYZ");
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setActive(true);
        return user;
    }

    public static User createTestUser(String userId, String username) {
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword("encodedPassword");
        user.setActive(true);
        return user;
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
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUsername("testuser");
        profile.setUserId("USER_ABC123XYZ");
        profile.setName("Test User");
        profile.setProfession("Software Engineer");
        profile.setLocation("Dhaka, Bangladesh");
        profile.setBio("A passionate software engineer");
        profile.setPhone("+8801234567890");
        profile.setLinkedIn("linkedin.com/in/testuser");
        profile.setWebsite("testuser.com");
        profile.setBatch("2018");
        profile.setEmail("test@example.com");
        profile.setFacebook("facebook.com/testuser");
        profile.setShowPhone(true);
        profile.setShowLinkedIn(true);
        profile.setShowWebsite(true);
        profile.setShowEmail(true);
        profile.setShowFacebook(false);
        profile.setConnections(10);
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        return profile;
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
        Experience experience = new Experience();
        experience.setId(1L);
        experience.setUsername("testuser");
        experience.setUserId("USER_ABC123XYZ");
        experience.setTitle("Senior Software Engineer");
        experience.setCompany("Tech Corp");
        experience.setPeriod("2020-2023");
        experience.setDescription("Led development of microservices");
        experience.setCreatedAt(LocalDateTime.now());
        return experience;
    }

    public static ExperienceDto createTestExperienceDto() {
        ExperienceDto dto = new ExperienceDto();
        dto.setId(1L);
        dto.setUserId("USER_ABC123XYZ");
        dto.setTitle("Senior Software Engineer");
        dto.setCompany("Tech Corp");
        dto.setPeriod("2020-2023");
        dto.setDescription("Led development of microservices");
        dto.setCreatedAt(java.time.LocalDateTime.now());
        return dto;
    }

    public static List<Experience> createTestExperienceList() {
        Experience experience2 = new Experience();
        experience2.setId(2L);
        experience2.setUsername("testuser");
        experience2.setUserId("USER_ABC123XYZ");
        experience2.setTitle("Software Engineer");
        experience2.setCompany("Startup Inc");
        experience2.setPeriod("2018-2020");
        experience2.setDescription("Full-stack development");
        experience2.setCreatedAt(LocalDateTime.now());
        
        return Arrays.asList(
                createTestExperience(),
                experience2
        );
    }

    // Interest related test data
    public static Interest createTestInterest() {
        Interest interest = new Interest();
        interest.setId(1L);
        interest.setUsername("testuser");
        interest.setUserId("USER_ABC123XYZ");
        interest.setInterest("Java Programming");
        interest.setCreatedAt(LocalDateTime.now());
        return interest;
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
        Interest interest2 = new Interest();
        interest2.setId(2L);
        interest2.setUsername("testuser");
        interest2.setUserId("USER_ABC123XYZ");
        interest2.setInterest("Spring Boot");
        interest2.setCreatedAt(LocalDateTime.now());
        
        return Arrays.asList(
                createTestInterest(),
                interest2
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
