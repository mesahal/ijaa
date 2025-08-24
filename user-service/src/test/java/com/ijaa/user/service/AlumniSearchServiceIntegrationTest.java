package com.ijaa.user.service;

import com.ijaa.user.domain.common.PagedResponse;
import com.ijaa.user.domain.dto.AlumniSearchDto;
import com.ijaa.user.domain.request.AlumniSearchRequest;
import com.ijaa.user.service.AlumniSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AlumniSearchServiceIntegrationTest {

    @Autowired
    private AlumniSearchService alumniSearchService;

    @Test
    void testAlumniSearchServiceIsAvailable() {
        assertNotNull(alumniSearchService, "AlumniSearchService should be available");
    }

    @Test
    void testAlumniSearchRequestCreation() {
        AlumniSearchRequest request = new AlumniSearchRequest();
        request.setSearchQuery("software engineer");
        request.setBatch("2020");
        request.setProfession("Technology");
        request.setLocation("Bangalore");
        request.setSortBy("relevance");
        request.setPage(0);
        request.setSize(10);

        assertEquals("software engineer", request.getSearchQuery());
        assertEquals("2020", request.getBatch());
        assertEquals("Technology", request.getProfession());
        assertEquals("Bangalore", request.getLocation());
        assertEquals("relevance", request.getSortBy());
        assertEquals(0, request.getPage());
        assertEquals(10, request.getSize());
    }

    @Test
    void testAlumniSearchDtoStructure() {
        AlumniSearchDto dto = new AlumniSearchDto();
        dto.setUserId("user123");
        dto.setName("John Doe");
        dto.setBatch("2020");
        dto.setProfession("Software Engineer");
        dto.setLocation("Bangalore, India");
        dto.setAvatar("https://example.com/avatar.jpg");
        dto.setBio("Passionate software engineer");
        dto.setConnections(45);
        dto.setIsConnected(false);

        assertEquals("user123", dto.getUserId());
        assertEquals("John Doe", dto.getName());
        assertEquals("2020", dto.getBatch());
        assertEquals("Software Engineer", dto.getProfession());
        assertEquals("Bangalore, India", dto.getLocation());
        assertEquals("https://example.com/avatar.jpg", dto.getAvatar());
        assertEquals("Passionate software engineer", dto.getBio());
        assertEquals(45, dto.getConnections());
        assertFalse(dto.getIsConnected());
    }

    @Test
    void testPagedResponseStructure() {
        PagedResponse<AlumniSearchDto> response = new PagedResponse<>();
        response.setContent(java.util.List.of());
        response.setPage(0);
        response.setSize(10);
        response.setTotalElements(0);
        response.setTotalPages(0);
        response.setFirst(true);
        response.setLast(true);

        assertNotNull(response.getContent());
        assertEquals(0, response.getPage());
        assertEquals(10, response.getSize());
        assertEquals(0, response.getTotalElements());
        assertEquals(0, response.getTotalPages());
        assertTrue(response.isFirst());
        assertTrue(response.isLast());
    }
}
