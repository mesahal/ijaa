package com.ijaa.user.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.enums.AdminRole;
import com.ijaa.user.domain.request.AdminLoginRequest;
import com.ijaa.user.domain.request.AdminSignupRequest;
import com.ijaa.user.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import com.ijaa.user.config.TestConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.ijaa.user.domain.response.AdminAuthResponse;

@WebMvcTest(AdminAuthResource.class)
@Import(TestConfig.class)
class AdminAuthResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    @Test
    void testAdminSignup_Success() throws Exception {
        // Given
        AdminSignupRequest request = new AdminSignupRequest();
        request.setName("Test Admin");
        request.setEmail("test@admin.com");
        request.setPassword("password123");
        request.setRole(AdminRole.ADMIN);

        // Mock admin service response
        AdminAuthResponse mockResponse = new AdminAuthResponse();
        mockResponse.setToken("mock-jwt-token");
        mockResponse.setAdminId(1L);
        mockResponse.setName("Test Admin");
        mockResponse.setEmail("admin@test.com");
        mockResponse.setRole(AdminRole.ADMIN);
        mockResponse.setActive(true);

        // When & Then
        mockMvc.perform(post("/api/admin/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Admin registration successful"))
                .andExpect(jsonPath("$.code").value("201"));
    }

    @Test
    void testAdminLogin_Success() throws Exception {
        // Given
        AdminLoginRequest request = new AdminLoginRequest();
        request.setEmail("test@admin.com");
        request.setPassword("password123");

        // Mock admin service response
        AdminAuthResponse mockResponse = new AdminAuthResponse();
        mockResponse.setToken("mock-jwt-token");
        mockResponse.setAdminId(1L);
        mockResponse.setName("Test Admin");
        mockResponse.setEmail("admin@test.com");
        mockResponse.setRole(AdminRole.ADMIN);
        mockResponse.setActive(true);

        // When & Then
        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Admin login successful"))
                .andExpect(jsonPath("$.code").value("200"));
    }

    @Test
    void testAdminSignup_InvalidRequest() throws Exception {
        // Given
        AdminSignupRequest request = new AdminSignupRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/admin/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAdminLogin_InvalidRequest() throws Exception {
        // Given
        AdminLoginRequest request = new AdminLoginRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetProfile_Authenticated() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/admin/profile"))
                .andExpect(status().isMethodNotAllowed()); // GET method expected
    }

    @Test
    void testGetProfile_Unauthenticated() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/admin/profile"))
                .andExpect(status().isUnauthorized());
    }
} 