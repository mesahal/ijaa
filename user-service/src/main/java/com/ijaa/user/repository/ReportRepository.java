package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    
    // Find all reports by status
    List<Report> findByStatus(String status);
    
    // Find all reports ordered by creation date (newest first)
    List<Report> findAllByOrderByCreatedAtDesc();
    
    // Find reports by category
    List<Report> findByCategory(String category);
    
    // Find reports by priority
    List<Report> findByPriority(String priority);
    
    // Find reports by status and priority
    List<Report> findByStatusAndPriority(String status, String priority);
    
    // Find reports by reporter email
    List<Report> findByReporterEmail(String reporterEmail);
    
    // Find reports by assigned admin
    List<Report> findByAssignedTo(String assignedTo);
    
    // Find pending reports
    List<Report> findByStatusOrderByCreatedAtDesc(String status);
    
    // Dashboard statistics methods
    @Query("SELECT COUNT(r) FROM Report r WHERE r.status = 'PENDING'")
    Long countPendingReports();
    
    @Query("SELECT COUNT(r) FROM Report r WHERE r.status = 'IN_PROGRESS'")
    Long countInProgressReports();
    
    @Query("SELECT COUNT(r) FROM Report r WHERE r.status = 'RESOLVED'")
    Long countResolvedReports();
    
    @Query("SELECT COUNT(r) FROM Report r WHERE r.priority = 'URGENT' AND r.status != 'RESOLVED'")
    Long countUrgentActiveReports();
} 