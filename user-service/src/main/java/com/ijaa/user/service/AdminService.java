package com.ijaa.user.service;

import com.ijaa.user.domain.entity.Admin;
import com.ijaa.user.domain.enums.AdminRole;
import com.ijaa.user.domain.request.AdminLoginRequest;
import com.ijaa.user.domain.request.AdminPasswordChangeRequest;
import com.ijaa.user.domain.request.AdminSignupRequest;
import com.ijaa.user.domain.response.AdminAuthResponse;
import com.ijaa.user.domain.response.AdminProfileResponse;
import com.ijaa.user.domain.response.DashboardStatsResponse;
import com.ijaa.user.domain.response.UserResponse;

import java.util.List;

public interface AdminService {

    AdminAuthResponse signup(AdminSignupRequest request);
    
    AdminAuthResponse login(AdminLoginRequest request);
    
    AdminProfileResponse changePassword(AdminPasswordChangeRequest request);
    
    AdminProfileResponse getProfile(Long adminId);
    
    List<AdminProfileResponse> getAllAdmins();
    

    
    AdminProfileResponse deactivateAdmin(Long adminId);
    
    AdminProfileResponse activateAdmin(Long adminId);
    
    DashboardStatsResponse getDashboardStats();
    
    boolean isFirstAdmin();
    
    boolean hasSuperAdminPrivileges(Long adminId);
    

    
    boolean hasContentManagerPrivileges(Long adminId);
    
    boolean hasModeratorPrivileges(Long adminId);
    
    // User Management Methods
    List<UserResponse> getAllUsers();
    
    UserResponse blockUser(String userId);
    
    UserResponse unblockUser(String userId);
    
    void deleteUser(String userId);
} 