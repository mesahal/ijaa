package com.ijaa.user.service.impl;

import com.ijaa.user.common.exceptions.AdminAlreadyActiveException;
import com.ijaa.user.common.exceptions.AdminAlreadyExistsException;
import com.ijaa.user.common.exceptions.AdminAlreadyInactiveException;
import com.ijaa.user.common.exceptions.AdminNotFoundException;
import com.ijaa.user.common.exceptions.AdminSelfDeactivationException;
import com.ijaa.user.common.exceptions.AuthenticationFailedException;
import com.ijaa.user.common.exceptions.InsufficientPrivilegesException;
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
import com.ijaa.user.domain.response.DashboardStatsResponse;
import com.ijaa.user.domain.response.UserResponse;
import com.ijaa.user.repository.AdminRepository;
import com.ijaa.user.repository.UserRepository;
import com.ijaa.user.service.AdminService;
import com.ijaa.user.service.AnnouncementService;

import com.ijaa.user.service.ReportService;
import com.ijaa.user.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    private final AnnouncementService announcementService;
    private final ReportService reportService;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public AdminAuthResponse signup(AdminSignupRequest request) {
        // Check if admin already exists
        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new AdminAlreadyExistsException("Admin with email " + request.getEmail() + " already exists");
        }

        // Check if this is the first admin creation
        boolean isFirstAdmin = isFirstAdmin();
        
        if (isFirstAdmin) {
            // First admin creation - no authentication required
            // Only allow ADMIN role for first admin
            if (request.getRole() != AdminRole.ADMIN) {
                throw new InsufficientPrivilegesException("First admin must have ADMIN role");
            }
        } else {
            // Subsequent admin creation - check authentication
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new InsufficientPrivilegesException("Authentication required to create new admin");
            }
            
            // Check if the authenticated user is an admin
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            
            if (!isAdmin) {
                throw new InsufficientPrivilegesException("Only existing ADMIN can create new ADMIN accounts");
            }
        }

        // Create new admin
        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        admin.setRole(request.getRole());
        admin.setActive(true);

        Admin savedAdmin = adminRepository.save(admin);

        // Generate JWT token with admin role
        String token = jwtService.generateAdminToken(savedAdmin.getEmail(), savedAdmin.getRole().getRole());

        return createAuthResponse(savedAdmin, token);
    }

    @Override
    public AdminAuthResponse login(AdminLoginRequest request) {
        Admin admin = adminRepository.findByEmailAndActiveTrue(request.getEmail())
                .orElseThrow(() -> new AuthenticationFailedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), admin.getPasswordHash())) {
            throw new AuthenticationFailedException("Invalid email or password");
        }

        // Generate JWT token with admin role
        String token = jwtService.generateAdminToken(admin.getEmail(), admin.getRole().getRole());

        return createAuthResponse(admin, token);
    }

    @Override
    public AdminProfileResponse changePassword(AdminPasswordChangeRequest request) {
        // Get current authenticated admin
        Long currentAdminId = getCurrentAdminId();
        if (currentAdminId == null) {
            throw new AuthenticationFailedException("Authentication required to change password");
        }

        Admin admin = adminRepository.findById(currentAdminId)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPasswordHash())) {
            throw new PasswordChangeException("Current password is incorrect");
        }

        // Validate new password confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordChangeException("New password and confirm password do not match");
        }

        // Check if new password is same as current password
        if (passwordEncoder.matches(request.getNewPassword(), admin.getPasswordHash())) {
            throw new PasswordChangeException("New password must be different from current password");
        }

        // Update password
        admin.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        Admin updatedAdmin = adminRepository.save(admin);

        return createProfileResponse(updatedAdmin);
    }

    @Override
    public AdminProfileResponse getProfile(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with id: " + adminId));

        return createProfileResponse(admin);
    }

    @Override
    public List<AdminProfileResponse> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::createProfileResponse)
                .collect(Collectors.toList());
    }



    @Override
    public AdminProfileResponse deactivateAdmin(Long adminId) {
        // Get the current authenticated admin
        Long currentAdminId = getCurrentAdminId();
        
        // Check if admin is trying to deactivate themselves
        if (currentAdminId != null && currentAdminId.equals(adminId)) {
            throw new AdminSelfDeactivationException("Admin cannot deactivate their own account");
        }
        
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with id: " + adminId));

        // Check if admin is already inactive
        if (!admin.getActive()) {
            throw new AdminAlreadyInactiveException("Admin is already deactivated");
        }

        admin.setActive(false);
        Admin updatedAdmin = adminRepository.save(admin);

        return createProfileResponse(updatedAdmin);
    }

    @Override
    public AdminProfileResponse activateAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException("Admin not found with id: " + adminId));

        // Check if admin is already active
        if (admin.getActive()) {
            throw new AdminAlreadyActiveException("Admin is already activated");
        }

        admin.setActive(true);
        Admin updatedAdmin = adminRepository.save(admin);

        return createProfileResponse(updatedAdmin);
    }

    @Override
    public DashboardStatsResponse getDashboardStats() {
        DashboardStatsResponse stats = new DashboardStatsResponse();
        
        // User statistics - now with real data
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByActiveTrue();
        long blockedUsers = userRepository.countByActiveFalse();
        
        stats.setTotalUsers(totalUsers);
        stats.setActiveUsers(activeUsers);
        stats.setBlockedUsers(blockedUsers);
        
        // Admin statistics
        long totalAdmins = adminRepository.count();
        long activeAdmins = adminRepository.countActiveAdmins();
        
        stats.setTotalAdmins(totalAdmins);
        stats.setActiveAdmins(activeAdmins);
        

        
        // Announcement statistics - now with real data
        long totalAnnouncements = announcementService.getTotalAnnouncements();
        long activeAnnouncements = announcementService.getActiveAnnouncementsCount();
        
        stats.setTotalAnnouncements(totalAnnouncements);
        stats.setActiveAnnouncements(activeAnnouncements);
        
        // Report statistics - now with real data
        long totalReports = reportService.getTotalReports();
        long pendingReports = reportService.getPendingReportsCount();
        
        stats.setTotalReports(totalReports);
        stats.setPendingReports(pendingReports);
        
        // Top batches (placeholder)
        stats.setTopBatches(List.of());
        
        // Recent activities (placeholder)
        stats.setRecentActivities(List.of());
        
        return stats;
    }

    @Override
    public boolean isFirstAdmin() {
        return adminRepository.count() == 0;
    }

    @Override
    public boolean hasSuperAdminPrivileges(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        return admin != null && admin.getRole() == AdminRole.ADMIN;
    }



    @Override
    public boolean hasContentManagerPrivileges(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        return admin != null && admin.getRole() == AdminRole.ADMIN;
    }

    @Override
    public boolean hasModeratorPrivileges(Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElse(null);
        return admin != null && admin.getRole() == AdminRole.ADMIN;
    }

    /**
     * Gets the current authenticated admin's ID from the security context
     * @return The current admin's ID, or null if not authenticated or not an admin
     */
    private Long getCurrentAdminId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                Admin admin = adminRepository.findByEmail(email).orElse(null);
                return admin != null ? admin.getId() : null;
            }
        } catch (Exception e) {
            // Log the error but don't throw exception to avoid breaking the flow
            // In a production environment, you might want to log this properly
        }
        return null;
    }

    // User Management Methods
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::createUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse blockUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with userId: " + userId));
        
        // Check if user is already blocked
        if (!user.getActive()) {
            throw new UserAlreadyBlockedException("User is already blocked");
        }
        
        user.setActive(false);
        User updatedUser = userRepository.save(user);
        
        return createUserResponse(updatedUser);
    }

    @Override
    public UserResponse unblockUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with userId: " + userId));
        
        // Check if user is already unblocked
        if (user.getActive()) {
            throw new UserAlreadyUnblockedException("User is already unblocked");
        }
        
        user.setActive(true);
        User updatedUser = userRepository.save(user);
        
        return createUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with userId: " + userId));
        
        userRepository.delete(user);
    }

    private AdminAuthResponse createAuthResponse(Admin admin, String token) {
        AdminAuthResponse response = new AdminAuthResponse();
        response.setToken(token);
        response.setAdminId(admin.getId());
        response.setName(admin.getName());
        response.setEmail(admin.getEmail());
        response.setRole(admin.getRole());
        response.setActive(admin.getActive());
        return response;
    }

    private AdminProfileResponse createProfileResponse(Admin admin) {
        AdminProfileResponse response = new AdminProfileResponse();
        response.setId(admin.getId());
        response.setName(admin.getName());
        response.setEmail(admin.getEmail());
        response.setRole(admin.getRole());
        response.setActive(admin.getActive());
        response.setCreatedAt(admin.getCreatedAt());
        response.setUpdatedAt(admin.getUpdatedAt());
        return response;
    }

    private UserResponse createUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setUserId(user.getUserId());
        response.setUsername(user.getUsername());
        response.setName(user.getUsername()); // Using username as name for now
        response.setEmail(null); // User entity doesn't have email field yet
        response.setActive(user.getActive());
        response.setCreatedAt(null); // User entity doesn't have timestamps yet
        response.setUpdatedAt(null);
        return response;
    }
} 