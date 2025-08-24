package com.ijaa.user.service;

import com.ijaa.user.common.exceptions.AdminAlreadyActiveException;
import com.ijaa.user.common.exceptions.AdminAlreadyExistsException;
import com.ijaa.user.common.exceptions.AdminAlreadyInactiveException;
import com.ijaa.user.common.exceptions.AdminSelfDeactivationException;
import com.ijaa.user.common.exceptions.PasswordChangeException;
import com.ijaa.user.common.exceptions.UserAlreadyBlockedException;
import com.ijaa.user.common.exceptions.UserAlreadyUnblockedException;
import com.ijaa.user.domain.entity.Admin;
import com.ijaa.user.domain.entity.User;
import com.ijaa.user.domain.enums.AdminRole;
import com.ijaa.user.domain.request.AdminLoginRequest;
import com.ijaa.user.domain.request.AdminPasswordChangeRequest;
import com.ijaa.user.domain.request.AdminSignupRequest;
import com.ijaa.user.domain.response.AdminAuthResponse;
import com.ijaa.user.domain.response.AdminProfileResponse;
import com.ijaa.user.domain.response.UserResponse;
import com.ijaa.user.repository.AdminRepository;
import com.ijaa.user.repository.UserRepository;
import com.ijaa.user.common.utils.UniqueIdGenerator;
import com.ijaa.user.service.impl.AdminServiceImpl;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import org.mockito.MockedStatic;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private UniqueIdGenerator idGenerator;

    @Mock
    private com.ijaa.user.service.AnnouncementService announcementService;

    @Mock
    private com.ijaa.user.service.ReportService reportService;

    @InjectMocks
    private AdminServiceImpl adminService;

    private Admin testAdmin;
    private AdminSignupRequest signupRequest;
    private AdminLoginRequest loginRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        testAdmin = new Admin();
        testAdmin.setId(1L);
        testAdmin.setName("Admin User");
        testAdmin.setEmail("admin@test.com");
        testAdmin.setPasswordHash("encodedPassword");
        testAdmin.setRole(AdminRole.ADMIN);
        testAdmin.setActive(true);

        signupRequest = new AdminSignupRequest();
        signupRequest.setName("Admin User");
        signupRequest.setEmail("admin@test.com");
        signupRequest.setPassword("password123");
        signupRequest.setRole(AdminRole.ADMIN);

        loginRequest = new AdminLoginRequest();
        loginRequest.setEmail("admin@test.com");
        loginRequest.setPassword("password123");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUserId("USER_123456");
        testUser.setUsername("user");
        testUser.setActive(true);
    }

    @Test
    void shouldSignupAdminWhenValidRequestProvided() {
        // Given
        when(adminRepository.existsByEmail("admin@test.com")).thenReturn(false);
        when(adminRepository.count()).thenReturn(0L); // First admin
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);
        when(jwtService.generateAdminToken("admin@test.com", "ADMIN")).thenReturn("test-jwt-token");

        // When
        AdminAuthResponse result = adminService.signup(signupRequest);

        // Then
        assertNotNull(result);
        assertEquals("test-jwt-token", result.getToken());
        verify(adminRepository).existsByEmail("admin@test.com");
        verify(passwordEncoder).encode("password123");
        verify(adminRepository).save(any(Admin.class));
        verify(jwtService).generateAdminToken("admin@test.com", "ADMIN");
    }

    @Test
    void shouldThrowExceptionWhenAdminEmailAlreadyExists() {
        // Given
        when(adminRepository.existsByEmail("admin@test.com")).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> adminService.signup(signupRequest));
        verify(adminRepository).existsByEmail("admin@test.com");
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void shouldLoginAdminWhenValidCredentialsProvided() {
        // Given
        when(adminRepository.findByEmailAndActiveTrue("admin@test.com")).thenReturn(Optional.of(testAdmin));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtService.generateAdminToken("admin@test.com", "ADMIN")).thenReturn("test-jwt-token");

        // When
        AdminAuthResponse result = adminService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals("test-jwt-token", result.getToken());
        verify(adminRepository).findByEmailAndActiveTrue("admin@test.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtService).generateAdminToken("admin@test.com", "ADMIN");
    }

    @Test
    void shouldThrowExceptionWhenAdminNotFound() {
        // Given
        when(adminRepository.findByEmailAndActiveTrue("admin@test.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> adminService.login(loginRequest));
        verify(adminRepository).findByEmailAndActiveTrue("admin@test.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void shouldThrowExceptionWhenInvalidPasswordProvided() {
        // Given
        when(adminRepository.findByEmailAndActiveTrue("admin@test.com")).thenReturn(Optional.of(testAdmin));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> adminService.login(loginRequest));
        verify(adminRepository).findByEmailAndActiveTrue("admin@test.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtService, never()).generateAdminToken(anyString(), anyString());
    }

    @Test
    void shouldGetAllUsersWhenCalled() {
        // Given
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserResponse> result = adminService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void shouldGetAdminProfileWhenValidIdProvided() {
        // Given
        when(adminRepository.findById(1L)).thenReturn(Optional.of(testAdmin));

        // When
        AdminProfileResponse result = adminService.getProfile(1L);

        // Then
        assertNotNull(result);
        assertEquals("Admin User", result.getName());
        assertEquals("admin@test.com", result.getEmail());
        assertEquals(AdminRole.ADMIN, result.getRole());
        verify(adminRepository).findById(1L);
    }

    @Test
    void shouldGetAllAdminsWhenCalled() {
        // Given
        List<Admin> admins = Arrays.asList(testAdmin);
        when(adminRepository.findAll()).thenReturn(admins);

        // When
        List<AdminProfileResponse> result = adminService.getAllAdmins();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(adminRepository).findAll();
    }



    @Test
    void shouldDeactivateAdminWhenValidIdProvided() {
        // Given
        when(adminRepository.findById(1L)).thenReturn(Optional.of(testAdmin));
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

        // When
        AdminProfileResponse result = adminService.deactivateAdmin(1L);

        // Then
        assertNotNull(result);
        verify(adminRepository).findById(1L);
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void shouldActivateAdminWhenValidIdProvided() {
        // Given
        Admin inactiveAdmin = new Admin();
        inactiveAdmin.setId(1L);
        inactiveAdmin.setName("Admin User");
        inactiveAdmin.setEmail("admin@test.com");
        inactiveAdmin.setPasswordHash("encodedPassword");
        inactiveAdmin.setRole(AdminRole.ADMIN);
        inactiveAdmin.setActive(false); // Set to inactive so it can be activated
        
        when(adminRepository.findById(1L)).thenReturn(Optional.of(inactiveAdmin));
        when(adminRepository.save(any(Admin.class))).thenReturn(inactiveAdmin);

        // When
        AdminProfileResponse result = adminService.activateAdmin(1L);

        // Then
        assertNotNull(result);
        verify(adminRepository).findById(1L);
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void shouldReturnTrueWhenFirstAdmin() {
        // Given
        when(adminRepository.count()).thenReturn(0L);

        // When
        boolean result = adminService.isFirstAdmin();

        // Then
        assertTrue(result);
        verify(adminRepository).count();
    }

    @Test
    void shouldReturnFalseWhenNotFirstAdmin() {
        // Given
        when(adminRepository.count()).thenReturn(5L);

        // When
        boolean result = adminService.isFirstAdmin();

        // Then
        assertFalse(result);
        verify(adminRepository).count();
    }

    @Test
    void shouldDeleteUserWhenValidUserIdProvided() {
        // Given
        when(userRepository.findByUserId("USER_123456")).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        // When
        adminService.deleteUser("USER_123456");

        // Then
        verify(userRepository).findByUserId("USER_123456");
        verify(userRepository).delete(testUser);
    }

    @Test
    void shouldCreateAdminWhenValidRequestProvided() {
        // Given
        when(adminRepository.existsByEmail("admin@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

        // When
        AdminAuthResponse result = adminService.signup(signupRequest);

        // Then
        assertNotNull(result);
        assertEquals("Admin User", result.getName());
        assertEquals("admin@test.com", result.getEmail());
        assertEquals(AdminRole.ADMIN, result.getRole());
        verify(adminRepository).existsByEmail("admin@test.com");
        verify(passwordEncoder).encode("password123");
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingAdminWithExistingEmail() {
        // Given
        when(adminRepository.existsByEmail("admin@test.com")).thenReturn(true);

        // When & Then
        assertThrows(AdminAlreadyExistsException.class, () -> adminService.signup(signupRequest));
        verify(adminRepository).existsByEmail("admin@test.com");
        verify(adminRepository, never()).save(any(Admin.class));
    }

    // New test cases for admin validation features

    @Test
    void shouldThrowAdminSelfDeactivationExceptionWhenAdminTriesToDeactivateThemselves() {
        // Given
        Admin currentAdmin = new Admin();
        currentAdmin.setId(1L);
        currentAdmin.setEmail("admin@test.com");
        currentAdmin.setActive(true);
        
        when(adminRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(currentAdmin));

        // Mock SecurityContextHolder to return current admin
        try (MockedStatic<org.springframework.security.core.context.SecurityContextHolder> mockedSecurityContext = 
                mockStatic(org.springframework.security.core.context.SecurityContextHolder.class)) {
            
            org.springframework.security.core.Authentication mockAuth = mock(org.springframework.security.core.Authentication.class);
            when(mockAuth.isAuthenticated()).thenReturn(true);
            when(mockAuth.getName()).thenReturn("admin@test.com");
            
            org.springframework.security.core.context.SecurityContext mockContext = mock(org.springframework.security.core.context.SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuth);
            mockedSecurityContext.when(org.springframework.security.core.context.SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When & Then
            assertThrows(AdminSelfDeactivationException.class, () -> {
                adminService.deactivateAdmin(1L);
            });
            
            verify(adminRepository).findByEmail("admin@test.com");
            verify(adminRepository, never()).findById(any(Long.class));
            verify(adminRepository, never()).save(any(Admin.class));
        }
    }

    @Test
    void shouldThrowAdminAlreadyActiveExceptionWhenActivatingAlreadyActiveAdmin() {
        // Given
        Admin activeAdmin = new Admin();
        activeAdmin.setId(2L);
        activeAdmin.setActive(true);
        
        when(adminRepository.findById(2L)).thenReturn(Optional.of(activeAdmin));

        // When & Then
        assertThrows(AdminAlreadyActiveException.class, () -> {
            adminService.activateAdmin(2L);
        });
        
        verify(adminRepository).findById(2L);
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void shouldThrowAdminAlreadyInactiveExceptionWhenDeactivatingAlreadyInactiveAdmin() {
        // Given
        Admin inactiveAdmin = new Admin();
        inactiveAdmin.setId(3L);
        inactiveAdmin.setActive(false);
        
        when(adminRepository.findById(3L)).thenReturn(Optional.of(inactiveAdmin));

        // When & Then
        assertThrows(AdminAlreadyInactiveException.class, () -> {
            adminService.deactivateAdmin(3L);
        });
        
        verify(adminRepository).findById(3L);
        verify(adminRepository, never()).save(any(Admin.class));
    }

    @Test
    void shouldSuccessfullyDeactivateAdminWhenValidRequestAndNotSelfDeactivation() {
        // Given
        Admin targetAdmin = new Admin();
        targetAdmin.setId(2L);
        targetAdmin.setActive(true);
        
        Admin currentAdmin = new Admin();
        currentAdmin.setId(1L);
        currentAdmin.setEmail("current@test.com");
        
        when(adminRepository.findByEmail("current@test.com")).thenReturn(Optional.of(currentAdmin));
        when(adminRepository.findById(2L)).thenReturn(Optional.of(targetAdmin));
        when(adminRepository.save(any(Admin.class))).thenReturn(targetAdmin);

        // Mock SecurityContextHolder to return current admin
        try (MockedStatic<org.springframework.security.core.context.SecurityContextHolder> mockedSecurityContext = 
                mockStatic(org.springframework.security.core.context.SecurityContextHolder.class)) {
            
            org.springframework.security.core.Authentication mockAuth = mock(org.springframework.security.core.Authentication.class);
            when(mockAuth.isAuthenticated()).thenReturn(true);
            when(mockAuth.getName()).thenReturn("current@test.com");
            
            org.springframework.security.core.context.SecurityContext mockContext = mock(org.springframework.security.core.context.SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuth);
            mockedSecurityContext.when(org.springframework.security.core.context.SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When
            AdminProfileResponse result = adminService.deactivateAdmin(2L);

            // Then
            assertNotNull(result);
            verify(adminRepository).findByEmail("current@test.com");
            verify(adminRepository).findById(2L);
            verify(adminRepository).save(any(Admin.class));
        }
    }

    @Test
    void shouldSuccessfullyActivateAdminWhenValidRequestAndAdminIsInactive() {
        // Given
        Admin inactiveAdmin = new Admin();
        inactiveAdmin.setId(2L);
        inactiveAdmin.setActive(false);
        
        when(adminRepository.findById(2L)).thenReturn(Optional.of(inactiveAdmin));
        when(adminRepository.save(any(Admin.class))).thenReturn(inactiveAdmin);

        // When
        AdminProfileResponse result = adminService.activateAdmin(2L);

        // Then
        assertNotNull(result);
        verify(adminRepository).findById(2L);
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void shouldHandleNullCurrentAdminIdGracefully() {
        // Given
        Admin targetAdmin = new Admin();
        targetAdmin.setId(2L);
        targetAdmin.setActive(true);
        
        when(adminRepository.findById(2L)).thenReturn(Optional.of(targetAdmin));
        when(adminRepository.save(any(Admin.class))).thenReturn(targetAdmin);

        // Mock SecurityContextHolder to return null authentication
        try (MockedStatic<org.springframework.security.core.context.SecurityContextHolder> mockedSecurityContext = 
                mockStatic(org.springframework.security.core.context.SecurityContextHolder.class)) {
            
            org.springframework.security.core.context.SecurityContext mockContext = mock(org.springframework.security.core.context.SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(null);
            mockedSecurityContext.when(org.springframework.security.core.context.SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When
            AdminProfileResponse result = adminService.deactivateAdmin(2L);

            // Then
            assertNotNull(result);
            verify(adminRepository).findById(2L);
            verify(adminRepository).save(any(Admin.class));
        }
    }

    // New test cases for user block/unblock validation features

    @Test
    void shouldThrowUserAlreadyBlockedExceptionWhenBlockingAlreadyBlockedUser() {
        // Given
        User blockedUser = new User();
        blockedUser.setId(1L);
        blockedUser.setUserId("USER_123456");
        blockedUser.setUsername("blockeduser");
        blockedUser.setActive(false); // Already blocked
        
        when(userRepository.findByUserId("USER_123456")).thenReturn(Optional.of(blockedUser));

        // When & Then
        assertThrows(UserAlreadyBlockedException.class, () -> {
            adminService.blockUser("USER_123456");
        });
        
        verify(userRepository).findByUserId("USER_123456");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowUserAlreadyUnblockedExceptionWhenUnblockingAlreadyUnblockedUser() {
        // Given
        User unblockedUser = new User();
        unblockedUser.setId(2L);
        unblockedUser.setUserId("USER_789012");
        unblockedUser.setUsername("unblockeduser");
        unblockedUser.setActive(true); // Already unblocked
        
        when(userRepository.findByUserId("USER_789012")).thenReturn(Optional.of(unblockedUser));

        // When & Then
        assertThrows(UserAlreadyUnblockedException.class, () -> {
            adminService.unblockUser("USER_789012");
        });
        
        verify(userRepository).findByUserId("USER_789012");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldSuccessfullyBlockUserWhenUserIsNotBlocked() {
        // Given
        User activeUser = new User();
        activeUser.setId(3L);
        activeUser.setUserId("USER_345678");
        activeUser.setUsername("activeuser");
        activeUser.setActive(true); // Currently active
        
        when(userRepository.findByUserId("USER_345678")).thenReturn(Optional.of(activeUser));
        when(userRepository.save(any(User.class))).thenReturn(activeUser);

        // When
        UserResponse result = adminService.blockUser("USER_345678");

        // Then
        assertNotNull(result);
        assertEquals("USER_345678", result.getUserId());
        verify(userRepository).findByUserId("USER_345678");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldSuccessfullyUnblockUserWhenUserIsBlocked() {
        // Given
        User blockedUser = new User();
        blockedUser.setId(4L);
        blockedUser.setUserId("USER_901234");
        blockedUser.setUsername("blockeduser");
        blockedUser.setActive(false); // Currently blocked
        
        when(userRepository.findByUserId("USER_901234")).thenReturn(Optional.of(blockedUser));
        when(userRepository.save(any(User.class))).thenReturn(blockedUser);

        // When
        UserResponse result = adminService.unblockUser("USER_901234");

        // Then
        assertNotNull(result);
        assertEquals("USER_901234", result.getUserId());
        verify(userRepository).findByUserId("USER_901234");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenBlockingNonExistentUser() {
        // Given
        when(userRepository.findByUserId("NON_EXISTENT_USER")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            adminService.blockUser("NON_EXISTENT_USER");
        });
        
        verify(userRepository).findByUserId("NON_EXISTENT_USER");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUnblockingNonExistentUser() {
        // Given
        when(userRepository.findByUserId("NON_EXISTENT_USER")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            adminService.unblockUser("NON_EXISTENT_USER");
        });
        
        verify(userRepository).findByUserId("NON_EXISTENT_USER");
        verify(userRepository, never()).save(any(User.class));
    }

    // Password Change Tests
    @Test
    void shouldSuccessfullyChangePasswordWhenValidRequestProvided() {
        // Given
        AdminPasswordChangeRequest request = new AdminPasswordChangeRequest();
        request.setCurrentPassword("oldPassword123");
        request.setNewPassword("newPassword123");
        request.setConfirmPassword("newPassword123");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(testAdmin));
        when(adminRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testAdmin));
        when(passwordEncoder.matches("oldPassword123", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.matches("newPassword123", "encodedPassword")).thenReturn(false);
        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword");
        when(adminRepository.save(any(Admin.class))).thenReturn(testAdmin);

        // Mock SecurityContextHolder to return current admin
        try (MockedStatic<org.springframework.security.core.context.SecurityContextHolder> mockedSecurityContext = 
                mockStatic(org.springframework.security.core.context.SecurityContextHolder.class)) {
            
            org.springframework.security.core.Authentication mockAuth = mock(org.springframework.security.core.Authentication.class);
            when(mockAuth.isAuthenticated()).thenReturn(true);
            when(mockAuth.getName()).thenReturn("admin@test.com");
            
            org.springframework.security.core.context.SecurityContext mockContext = mock(org.springframework.security.core.context.SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuth);
            
            mockedSecurityContext.when(org.springframework.security.core.context.SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When
            AdminProfileResponse result = adminService.changePassword(request);

            // Then
            assertNotNull(result);
            assertEquals(testAdmin.getId(), result.getId());
            verify(adminRepository).findById(1L);
            verify(passwordEncoder).matches("oldPassword123", "encodedPassword");
            verify(passwordEncoder).encode("newPassword123");
            verify(adminRepository).save(any(Admin.class));
        }
    }

    @Test
    void shouldThrowPasswordChangeExceptionWhenCurrentPasswordIsIncorrect() {
        // Given
        AdminPasswordChangeRequest request = new AdminPasswordChangeRequest();
        request.setCurrentPassword("wrongPassword");
        request.setNewPassword("newPassword123");
        request.setConfirmPassword("newPassword123");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(testAdmin));
        when(adminRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testAdmin));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // Mock SecurityContextHolder to return current admin
        try (MockedStatic<org.springframework.security.core.context.SecurityContextHolder> mockedSecurityContext = 
                mockStatic(org.springframework.security.core.context.SecurityContextHolder.class)) {
            
            org.springframework.security.core.Authentication mockAuth = mock(org.springframework.security.core.Authentication.class);
            when(mockAuth.isAuthenticated()).thenReturn(true);
            when(mockAuth.getName()).thenReturn("admin@test.com");
            
            org.springframework.security.core.context.SecurityContext mockContext = mock(org.springframework.security.core.context.SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuth);
            
            mockedSecurityContext.when(org.springframework.security.core.context.SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When & Then
            assertThrows(PasswordChangeException.class, () -> {
                adminService.changePassword(request);
            });
            
            verify(adminRepository).findById(1L);
            verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
            verify(passwordEncoder, never()).encode(anyString());
            verify(adminRepository, never()).save(any(Admin.class));
        }
    }

    @Test
    void shouldThrowPasswordChangeExceptionWhenNewPasswordAndConfirmPasswordDoNotMatch() {
        // Given
        AdminPasswordChangeRequest request = new AdminPasswordChangeRequest();
        request.setCurrentPassword("oldPassword123");
        request.setNewPassword("newPassword123");
        request.setConfirmPassword("differentPassword");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(testAdmin));
        when(adminRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testAdmin));
        when(passwordEncoder.matches("oldPassword123", "encodedPassword")).thenReturn(true);

        // Mock SecurityContextHolder to return current admin
        try (MockedStatic<org.springframework.security.core.context.SecurityContextHolder> mockedSecurityContext = 
                mockStatic(org.springframework.security.core.context.SecurityContextHolder.class)) {
            
            org.springframework.security.core.Authentication mockAuth = mock(org.springframework.security.core.Authentication.class);
            when(mockAuth.isAuthenticated()).thenReturn(true);
            when(mockAuth.getName()).thenReturn("admin@test.com");
            
            org.springframework.security.core.context.SecurityContext mockContext = mock(org.springframework.security.core.context.SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuth);
            
            mockedSecurityContext.when(org.springframework.security.core.context.SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When & Then
            assertThrows(PasswordChangeException.class, () -> {
                adminService.changePassword(request);
            });
            
            verify(adminRepository).findById(1L);
            verify(passwordEncoder).matches("oldPassword123", "encodedPassword");
            verify(passwordEncoder, never()).encode(anyString());
            verify(adminRepository, never()).save(any(Admin.class));
        }
    }

    @Test
    void shouldThrowPasswordChangeExceptionWhenNewPasswordIsSameAsCurrentPassword() {
        // Given
        AdminPasswordChangeRequest request = new AdminPasswordChangeRequest();
        request.setCurrentPassword("oldPassword123");
        request.setNewPassword("oldPassword123");
        request.setConfirmPassword("oldPassword123");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(testAdmin));
        when(adminRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(testAdmin));
        when(passwordEncoder.matches("oldPassword123", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.matches("oldPassword123", "encodedPassword")).thenReturn(true).thenReturn(true); // Same password

        // Mock SecurityContextHolder to return current admin
        try (MockedStatic<org.springframework.security.core.context.SecurityContextHolder> mockedSecurityContext = 
                mockStatic(org.springframework.security.core.context.SecurityContextHolder.class)) {
            
            org.springframework.security.core.Authentication mockAuth = mock(org.springframework.security.core.Authentication.class);
            when(mockAuth.isAuthenticated()).thenReturn(true);
            when(mockAuth.getName()).thenReturn("admin@test.com");
            
            org.springframework.security.core.context.SecurityContext mockContext = mock(org.springframework.security.core.context.SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuth);
            
            mockedSecurityContext.when(org.springframework.security.core.context.SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When & Then
            assertThrows(PasswordChangeException.class, () -> {
                adminService.changePassword(request);
            });
            
            verify(adminRepository).findById(1L);
            verify(passwordEncoder).matches("oldPassword123", "encodedPassword");
            verify(passwordEncoder, never()).encode(anyString());
            verify(adminRepository, never()).save(any(Admin.class));
        }
    }

    @Test
    void shouldThrowAuthenticationFailedExceptionWhenNoAuthentication() {
        // Given
        AdminPasswordChangeRequest request = new AdminPasswordChangeRequest();
        request.setCurrentPassword("oldPassword123");
        request.setNewPassword("newPassword123");
        request.setConfirmPassword("newPassword123");

        // Mock SecurityContextHolder to return null authentication
        try (MockedStatic<org.springframework.security.core.context.SecurityContextHolder> mockedSecurityContext = 
                mockStatic(org.springframework.security.core.context.SecurityContextHolder.class)) {
            
            org.springframework.security.core.context.SecurityContext mockContext = mock(org.springframework.security.core.context.SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(null);
            
            mockedSecurityContext.when(org.springframework.security.core.context.SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When & Then
            assertThrows(com.ijaa.user.common.exceptions.AuthenticationFailedException.class, () -> {
                adminService.changePassword(request);
            });
            
            verify(adminRepository, never()).findByEmail(anyString());
            verify(passwordEncoder, never()).matches(anyString(), anyString());
            verify(adminRepository, never()).save(any(Admin.class));
        }
    }

    @Test
    void shouldThrowAdminNotFoundExceptionWhenAdminNotFoundInDatabase() {
        // Given
        AdminPasswordChangeRequest request = new AdminPasswordChangeRequest();
        request.setCurrentPassword("oldPassword123");
        request.setNewPassword("newPassword123");
        request.setConfirmPassword("newPassword123");

        when(adminRepository.findById(1L)).thenReturn(Optional.empty());
        when(adminRepository.findByEmail("admin@test.com")).thenReturn(Optional.empty());

        // Mock SecurityContextHolder to return current admin
        try (MockedStatic<org.springframework.security.core.context.SecurityContextHolder> mockedSecurityContext = 
                mockStatic(org.springframework.security.core.context.SecurityContextHolder.class)) {
            
            org.springframework.security.core.Authentication mockAuth = mock(org.springframework.security.core.Authentication.class);
            when(mockAuth.isAuthenticated()).thenReturn(true);
            when(mockAuth.getName()).thenReturn("admin@test.com");
            
            org.springframework.security.core.context.SecurityContext mockContext = mock(org.springframework.security.core.context.SecurityContext.class);
            when(mockContext.getAuthentication()).thenReturn(mockAuth);
            
            mockedSecurityContext.when(org.springframework.security.core.context.SecurityContextHolder::getContext)
                    .thenReturn(mockContext);

            // When & Then
            assertThrows(com.ijaa.user.common.exceptions.AdminNotFoundException.class, () -> {
                adminService.changePassword(request);
            });
            
            verify(adminRepository).findById(1L);
            verify(passwordEncoder, never()).matches(anyString(), anyString());
            verify(adminRepository, never()).save(any(Admin.class));
        }
    }
}
