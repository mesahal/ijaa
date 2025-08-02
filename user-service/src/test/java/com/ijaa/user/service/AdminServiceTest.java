package com.ijaa.user.service;

import com.ijaa.user.common.exceptions.AdminAlreadyExistsException;
import com.ijaa.user.common.exceptions.AdminNotFoundException;
import com.ijaa.user.common.exceptions.AuthenticationFailedException;
import com.ijaa.user.domain.entity.Admin;
import com.ijaa.user.domain.enums.AdminRole;
import com.ijaa.user.domain.request.AdminLoginRequest;
import com.ijaa.user.domain.request.AdminSignupRequest;
import com.ijaa.user.domain.response.AdminAuthResponse;
import com.ijaa.user.domain.response.AdminProfileResponse;
import com.ijaa.user.repository.AdminRepository;
import com.ijaa.user.repository.UserRepository;
import com.ijaa.user.service.impl.AdminServiceImpl;
import com.ijaa.user.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Admin testAdmin;
    private AdminSignupRequest signupRequest;
    private AdminLoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testAdmin = new Admin();
        testAdmin.setId(1L);
        testAdmin.setName("Test Admin");
        testAdmin.setEmail("test@admin.com");
        testAdmin.setPasswordHash("encodedPassword");
        testAdmin.setRole(AdminRole.SUPER_ADMIN);
        testAdmin.setActive(true);

        signupRequest = new AdminSignupRequest();
        signupRequest.setName("Test Admin");
        signupRequest.setEmail("test@admin.com");
        signupRequest.setPassword("password123");
        signupRequest.setRole(AdminRole.SUPER_ADMIN);

        loginRequest = new AdminLoginRequest();
        loginRequest.setEmail("test@admin.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void testSignup_Success() {
        // Given
        when(adminRepository.existsByEmail(anyString())).thenReturn(false);
        when(adminRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);
        when(jwtService.generateToken(anyString())).thenReturn("jwtToken");

        // When
        AdminAuthResponse response = adminService.signup(signupRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals(testAdmin.getId(), response.getAdminId());
        assertEquals(testAdmin.getName(), response.getName());
        assertEquals(testAdmin.getEmail(), response.getEmail());
        assertEquals(testAdmin.getRole(), response.getRole());
        assertTrue(response.getActive());

        verify(adminRepository).existsByEmail(signupRequest.getEmail());
        verify(adminRepository).count();
        verify(passwordEncoder).encode(signupRequest.getPassword());
        verify(adminRepository).save(any(Admin.class));
        verify(jwtService).generateToken(testAdmin.getEmail());
    }

    @Test
    void testSignup_AdminAlreadyExists() {
        // Given
        when(adminRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(AdminAlreadyExistsException.class, () -> adminService.signup(signupRequest));
        verify(adminRepository).existsByEmail(signupRequest.getEmail());
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void testLogin_Success() {
        // Given
        when(adminRepository.findByEmailAndActiveTrue(anyString())).thenReturn(Optional.of(testAdmin));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(anyString())).thenReturn("jwtToken");

        // When
        AdminAuthResponse response = adminService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals(testAdmin.getId(), response.getAdminId());
        assertEquals(testAdmin.getName(), response.getName());
        assertEquals(testAdmin.getEmail(), response.getEmail());
        assertEquals(testAdmin.getRole(), response.getRole());
        assertTrue(response.getActive());

        verify(adminRepository).findByEmailAndActiveTrue(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), testAdmin.getPasswordHash());
        verify(jwtService).generateToken(testAdmin.getEmail());
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Given
        when(adminRepository.findByEmailAndActiveTrue(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(AuthenticationFailedException.class, () -> adminService.login(loginRequest));
        verify(adminRepository).findByEmailAndActiveTrue(loginRequest.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testGetProfile_Success() {
        // Given
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(testAdmin));

        // When
        AdminProfileResponse response = adminService.getProfile(1L);

        // Then
        assertNotNull(response);
        assertEquals(testAdmin.getId(), response.getId());
        assertEquals(testAdmin.getName(), response.getName());
        assertEquals(testAdmin.getEmail(), response.getEmail());
        assertEquals(testAdmin.getRole(), response.getRole());
        assertEquals(testAdmin.getActive(), response.getActive());

        verify(adminRepository).findById(1L);
    }

    @Test
    void testGetProfile_AdminNotFound() {
        // Given
        when(adminRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(AdminNotFoundException.class, () -> adminService.getProfile(1L));
        verify(adminRepository).findById(1L);
    }

    @Test
    void testGetAllAdmins_Success() {
        // Given
        List<Admin> admins = Arrays.asList(testAdmin);
        when(adminRepository.findAll()).thenReturn(admins);

        // When
        List<AdminProfileResponse> responses = adminService.getAllAdmins();

        // Then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(testAdmin.getId(), responses.get(0).getId());
        assertEquals(testAdmin.getName(), responses.get(0).getName());

        verify(adminRepository).findAll();
    }

    @Test
    void testIsFirstAdmin_True() {
        // Given
        when(adminRepository.count()).thenReturn(0L);

        // When
        boolean result = adminService.isFirstAdmin();

        // Then
        assertTrue(result);
        verify(adminRepository).count();
    }

    @Test
    void testIsFirstAdmin_False() {
        // Given
        when(adminRepository.count()).thenReturn(1L);

        // When
        boolean result = adminService.isFirstAdmin();

        // Then
        assertFalse(result);
        verify(adminRepository).count();
    }
} 