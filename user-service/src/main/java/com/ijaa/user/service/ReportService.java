package com.ijaa.user.service;

import com.ijaa.user.domain.request.ReportRequest;
import com.ijaa.user.domain.response.ReportResponse;

import java.util.List;

public interface ReportService {
    
    // Get all reports
    List<ReportResponse> getAllReports();
    
    // Get all pending reports
    List<ReportResponse> getPendingReports();
    
    // Get report by ID
    ReportResponse getReportById(Long reportId);
    
    // Create new report
    ReportResponse createReport(ReportRequest reportRequest);
    
    // Update existing report
    ReportResponse updateReport(Long reportId, ReportRequest reportRequest);
    
    // Delete report
    void deleteReport(Long reportId);
    
    // Resolve report
    ReportResponse resolveReport(Long reportId, String adminNotes);
    
    // Assign report to admin
    ReportResponse assignReport(Long reportId, String assignedTo);
    
    // Update report status
    ReportResponse updateReportStatus(Long reportId, String status, String adminNotes);
    
    // Dashboard statistics
    Long getTotalReports();
    Long getPendingReportsCount();
} 