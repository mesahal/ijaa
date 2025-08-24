package com.ijaa.user.service.impl;

import com.ijaa.user.domain.entity.Report;
import com.ijaa.user.domain.request.ReportRequest;
import com.ijaa.user.domain.response.ReportResponse;
import com.ijaa.user.repository.ReportRepository;
import com.ijaa.user.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    public List<ReportResponse> getAllReports() {
        return reportRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::createReportResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponse> getPendingReports() {
        return reportRepository.findByStatusOrderByCreatedAtDesc("PENDING").stream()
                .map(this::createReportResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReportResponse getReportById(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + reportId));
        
        return createReportResponse(report);
    }

    @Override
    public ReportResponse createReport(ReportRequest reportRequest) {
        Report report = new Report();
        report.setTitle(reportRequest.getTitle());
        report.setDescription(reportRequest.getDescription());
        report.setCategory(reportRequest.getCategory());
        report.setStatus("PENDING");
        report.setPriority(reportRequest.getPriority() != null ? reportRequest.getPriority() : "MEDIUM");
        report.setReporterName(reportRequest.getReporterName());
        report.setReporterEmail(reportRequest.getReporterEmail());
        report.setAttachmentUrl(reportRequest.getAttachmentUrl());
        
        Report savedReport = reportRepository.save(report);
        return createReportResponse(savedReport);
    }

    @Override
    public ReportResponse updateReport(Long reportId, ReportRequest reportRequest) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + reportId));
        
        // Update report fields
        report.setTitle(reportRequest.getTitle());
        report.setDescription(reportRequest.getDescription());
        report.setCategory(reportRequest.getCategory());
        report.setPriority(reportRequest.getPriority() != null ? reportRequest.getPriority() : "MEDIUM");
        report.setReporterName(reportRequest.getReporterName());
        report.setReporterEmail(reportRequest.getReporterEmail());
        report.setAttachmentUrl(reportRequest.getAttachmentUrl());
        
        Report updatedReport = reportRepository.save(report);
        return createReportResponse(updatedReport);
    }

    @Override
    public void deleteReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + reportId));
        
        reportRepository.delete(report);
    }

    @Override
    public ReportResponse resolveReport(Long reportId, String adminNotes) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + reportId));
        
        report.setStatus("RESOLVED");
        report.setAdminNotes(adminNotes);
        
        Report updatedReport = reportRepository.save(report);
        return createReportResponse(updatedReport);
    }

    @Override
    public ReportResponse assignReport(Long reportId, String assignedTo) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + reportId));
        
        report.setAssignedTo(assignedTo);
        report.setStatus("IN_PROGRESS");
        
        Report updatedReport = reportRepository.save(report);
        return createReportResponse(updatedReport);
    }

    @Override
    public ReportResponse updateReportStatus(Long reportId, String status, String adminNotes) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + reportId));
        
        report.setStatus(status);
        if (adminNotes != null && !adminNotes.trim().isEmpty()) {
            report.setAdminNotes(adminNotes);
        }
        
        Report updatedReport = reportRepository.save(report);
        return createReportResponse(updatedReport);
    }

    @Override
    public Long getTotalReports() {
        return reportRepository.count();
    }

    @Override
    public Long getPendingReportsCount() {
        return reportRepository.countPendingReports();
    }

    private ReportResponse createReportResponse(Report report) {
        ReportResponse response = new ReportResponse();
        response.setId(report.getId());
        response.setTitle(report.getTitle());
        response.setDescription(report.getDescription());
        response.setCategory(report.getCategory());
        response.setStatus(report.getStatus());
        response.setPriority(report.getPriority());
        response.setReporterName(report.getReporterName());
        response.setReporterEmail(report.getReporterEmail());
        response.setAssignedTo(report.getAssignedTo());
        response.setAdminNotes(report.getAdminNotes());
        response.setAttachmentUrl(report.getAttachmentUrl());
        response.setCreatedAt(report.getCreatedAt());
        response.setUpdatedAt(report.getUpdatedAt());
        return response;
    }
} 