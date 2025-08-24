package com.ijaa.user.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    
    @NotBlank(message = "Report title is required")
    private String title;
    
    @NotBlank(message = "Report description is required")
    private String description;
    
    private String category;
    
    private String priority = "MEDIUM";
    
    @NotBlank(message = "Reporter name is required")
    private String reporterName;
    
    @Email(message = "Reporter email must be valid")
    @NotBlank(message = "Reporter email is required")
    private String reporterEmail;
    
    private String attachmentUrl;
} 