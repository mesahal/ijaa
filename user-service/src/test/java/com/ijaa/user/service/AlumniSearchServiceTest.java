package com.ijaa.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.common.PagedResponse;
import com.ijaa.user.domain.dto.AlumniSearchDto;
import com.ijaa.user.domain.dto.InterestDto;
import com.ijaa.user.domain.entity.CurrentUserContext;
import com.ijaa.user.domain.entity.Interest;
import com.ijaa.user.domain.entity.Profile;
import com.ijaa.user.domain.request.AlumniSearchRequest;
import com.ijaa.user.repository.ConnectionRepository;
import com.ijaa.user.repository.InterestRepository;
import com.ijaa.user.repository.ProfileRepository;
import com.ijaa.user.service.impl.AlumniSearchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlumniSearchServiceTest {

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private InterestRepository interestRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private AlumniSearchServiceImpl alumniSearchService;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        // Create user context
        CurrentUserContext userContext = new CurrentUserContext();
        userContext.setUsername("testuser");
        userContext.setUserId("USER_123456");
        
        try {
            // Convert to JSON and then Base64 encode
            String userContextJson = objectMapper.writeValueAsString(userContext);
            String base64UserContext = Base64.getUrlEncoder().encodeToString(userContextJson.getBytes(StandardCharsets.UTF_8));
            
            request.addHeader("X-USER_ID", base64UserContext);
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        } catch (Exception e) {
            fail("Failed to setup user context: " + e.getMessage());
        }
    }

    @Test
    void testSearchAlumniWithBasicQuery() {
        // Arrange
        AlumniSearchRequest searchRequest = new AlumniSearchRequest();
        searchRequest.setSearchQuery("software engineer");
        searchRequest.setPage(0);
        searchRequest.setSize(10);

        Profile profile1 = createTestProfile("user1", "John Doe", "2020", "Software Engineer", "Bangalore");
        Profile profile2 = createTestProfile("user2", "Jane Smith", "2019", "Software Engineer", "Mumbai");
        
        List<Profile> profiles = Arrays.asList(profile1, profile2);
        Page<Profile> profilePage = new PageImpl<>(profiles, PageRequest.of(0, 10), 2);

        Interest interest1 = new Interest();
        interest1.setUserId("user1");
        interest1.setInterest("Java");
        
        Interest interest2 = new Interest();
        interest2.setUserId("user1");
        interest2.setInterest("Spring Boot");
        
        Interest interest3 = new Interest();
        interest3.setUserId("user2");
        interest3.setInterest("Python");

        when(connectionRepository.findConnectedUsernames("testuser"))
                .thenReturn(Arrays.asList("user1"));
        when(profileRepository.findProfilesWithFilters(
                eq("software engineer"), 
                isNull(), 
                isNull(), 
                isNull(), 
                eq("testuser"), 
                any(Pageable.class)))
                .thenReturn(profilePage);
        when(interestRepository.findByUserIdIn(Arrays.asList("user1", "user2")))
                .thenReturn(Arrays.asList(interest1, interest2, interest3));

        // Act
        PagedResponse<AlumniSearchDto> result = alumniSearchService.searchAlumni(searchRequest);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());

        AlumniSearchDto firstResult = result.getContent().get(0);
        assertEquals("user1", firstResult.getUserId());
        assertEquals("John Doe", firstResult.getName());
        assertEquals("2020", firstResult.getBatch());
        assertEquals("Software Engineer", firstResult.getProfession());
        assertEquals("Bangalore", firstResult.getLocation());
        assertTrue(firstResult.getIsConnected());
        assertEquals(2, firstResult.getInterests().size());
        assertTrue(firstResult.getInterests().contains("Java"));
        assertTrue(firstResult.getInterests().contains("Spring Boot"));

        AlumniSearchDto secondResult = result.getContent().get(1);
        assertEquals("user2", secondResult.getUserId());
        assertEquals("Jane Smith", secondResult.getName());
        assertEquals("2019", secondResult.getBatch());
        assertEquals("Software Engineer", secondResult.getProfession());
        assertEquals("Mumbai", secondResult.getLocation());
        assertFalse(secondResult.getIsConnected());
        assertEquals(1, secondResult.getInterests().size());
        assertTrue(secondResult.getInterests().contains("Python"));
    }

    @Test
    void testSearchAlumniWithLocationFilter() {
        // Arrange
        AlumniSearchRequest searchRequest = new AlumniSearchRequest();
        searchRequest.setLocation("Bangalore");
        searchRequest.setPage(0);
        searchRequest.setSize(5);

        Profile profile = createTestProfile("user1", "John Doe", "2020", "Software Engineer", "Bangalore");
        List<Profile> profiles = Arrays.asList(profile);
        Page<Profile> profilePage = new PageImpl<>(profiles, PageRequest.of(0, 5), 1);

        when(connectionRepository.findConnectedUsernames("testuser"))
                .thenReturn(Arrays.asList());
        when(profileRepository.findProfilesWithFilters(
                isNull(), 
                isNull(), 
                isNull(), 
                eq("Bangalore"), 
                eq("testuser"), 
                any(Pageable.class)))
                .thenReturn(profilePage);
        when(interestRepository.findByUserIdIn(Arrays.asList("user1")))
                .thenReturn(Arrays.asList());

        // Act
        PagedResponse<AlumniSearchDto> result = alumniSearchService.searchAlumni(searchRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalElements());
        
        AlumniSearchDto alumni = result.getContent().get(0);
        assertEquals("Bangalore", alumni.getLocation());
        assertFalse(alumni.getIsConnected());
        assertTrue(alumni.getInterests().isEmpty());
    }

    @Test
    void testSearchAlumniWithBatchFilter() {
        // Arrange
        AlumniSearchRequest searchRequest = new AlumniSearchRequest();
        searchRequest.setBatch("2020");
        searchRequest.setSortBy("name");
        searchRequest.setPage(0);
        searchRequest.setSize(10);

        Profile profile1 = createTestProfile("user1", "Alice Johnson", "2020", "Data Scientist", "Delhi");
        Profile profile2 = createTestProfile("user2", "Bob Wilson", "2020", "Product Manager", "Chennai");
        
        List<Profile> profiles = Arrays.asList(profile1, profile2);
        Page<Profile> profilePage = new PageImpl<>(profiles, PageRequest.of(0, 10), 2);

        when(connectionRepository.findConnectedUsernames("testuser"))
                .thenReturn(Arrays.asList("user1"));
        when(profileRepository.findProfilesWithFilters(
                isNull(), 
                eq("2020"), 
                isNull(), 
                isNull(), 
                eq("testuser"), 
                any(Pageable.class)))
                .thenReturn(profilePage);
        when(interestRepository.findByUserIdIn(Arrays.asList("user1", "user2")))
                .thenReturn(Arrays.asList());

        // Act
        PagedResponse<AlumniSearchDto> result = alumniSearchService.searchAlumni(searchRequest);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        
        AlumniSearchDto firstResult = result.getContent().get(0);
        assertEquals("2020", firstResult.getBatch());
        assertTrue(firstResult.getIsConnected());
        
        AlumniSearchDto secondResult = result.getContent().get(1);
        assertEquals("2020", secondResult.getBatch());
        assertFalse(secondResult.getIsConnected());
    }

    @Test
    void testSearchAlumniWithProfessionFilter() {
        // Arrange
        AlumniSearchRequest searchRequest = new AlumniSearchRequest();
        searchRequest.setProfession("Technology");
        searchRequest.setPage(0);
        searchRequest.setSize(10);

        Profile profile = createTestProfile("user1", "John Doe", "2020", "Software Engineer", "Bangalore");
        List<Profile> profiles = Arrays.asList(profile);
        Page<Profile> profilePage = new PageImpl<>(profiles, PageRequest.of(0, 10), 1);

        when(connectionRepository.findConnectedUsernames("testuser"))
                .thenReturn(Arrays.asList());
        when(profileRepository.findProfilesWithFilters(
                isNull(), 
                isNull(), 
                eq("Technology"), 
                isNull(), 
                eq("testuser"), 
                any(Pageable.class)))
                .thenReturn(profilePage);
        when(interestRepository.findByUserIdIn(Arrays.asList("user1")))
                .thenReturn(Arrays.asList());

        // Act
        PagedResponse<AlumniSearchDto> result = alumniSearchService.searchAlumni(searchRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        
        AlumniSearchDto alumni = result.getContent().get(0);
        assertEquals("Software Engineer", alumni.getProfession());
    }

    @Test
    void testSearchAlumniWithPagination() {
        // Arrange
        AlumniSearchRequest searchRequest = new AlumniSearchRequest();
        searchRequest.setPage(1);
        searchRequest.setSize(2);

        Profile profile1 = createTestProfile("user1", "John Doe", "2020", "Software Engineer", "Bangalore");
        Profile profile2 = createTestProfile("user2", "Jane Smith", "2019", "Data Scientist", "Mumbai");
        Profile profile3 = createTestProfile("user3", "Bob Wilson", "2021", "Product Manager", "Delhi");
        
        List<Profile> profiles = Arrays.asList(profile1, profile2, profile3);
        Page<Profile> profilePage = new PageImpl<>(profiles, PageRequest.of(1, 2), 3);

        when(connectionRepository.findConnectedUsernames("testuser"))
                .thenReturn(Arrays.asList());
        when(profileRepository.findProfilesWithFilters(
                isNull(), 
                isNull(), 
                isNull(), 
                isNull(), 
                eq("testuser"), 
                any(Pageable.class)))
                .thenReturn(profilePage);
        when(interestRepository.findByUserIdIn(Arrays.asList("user1", "user2", "user3")))
                .thenReturn(Arrays.asList());

        // Act
        PagedResponse<AlumniSearchDto> result = alumniSearchService.searchAlumni(searchRequest);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getPage());
        assertEquals(2, result.getSize());
        assertFalse(result.isFirst());
        assertTrue(result.isLast());
    }

    @Test
    void testSearchAlumniWithEmptyResults() {
        // Arrange
        AlumniSearchRequest searchRequest = new AlumniSearchRequest();
        searchRequest.setSearchQuery("nonexistent");
        searchRequest.setPage(0);
        searchRequest.setSize(10);

        Page<Profile> emptyPage = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);

        when(connectionRepository.findConnectedUsernames("testuser"))
                .thenReturn(Arrays.asList());
        when(profileRepository.findProfilesWithFilters(
                eq("nonexistent"), 
                isNull(), 
                isNull(), 
                isNull(), 
                eq("testuser"), 
                any(Pageable.class)))
                .thenReturn(emptyPage);
        when(interestRepository.findByUserIdIn(anyList()))
                .thenReturn(Arrays.asList());

        // Act
        PagedResponse<AlumniSearchDto> result = alumniSearchService.searchAlumni(searchRequest);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
        assertTrue(result.isFirst());
        assertTrue(result.isLast());
    }

    private Profile createTestProfile(String userId, String name, String batch, String profession, String location) {
        Profile profile = new Profile();
        profile.setUserId(userId);
        profile.setUsername(userId);
        profile.setName(name);
        profile.setBatch(batch);
        profile.setProfession(profession);
        profile.setLocation(location);
        profile.setBio("Test bio for " + name);
        profile.setConnections(10);
        return profile;
    }
}
