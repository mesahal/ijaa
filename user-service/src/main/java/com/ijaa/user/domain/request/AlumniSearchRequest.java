package com.ijaa.user.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "Request model for alumni search with various filters and pagination")
public class AlumniSearchRequest {
    
    @Schema(description = "Search query to find alumni by name, profession, or bio", example = "software engineer")
    private String searchQuery;
    
    @Schema(description = "Filter by graduation batch year", example = "2020")
//    @Pattern(regexp = "^\\d{4}$", message = "Batch must be a 4-digit year")
    private String batch;
    
    @Schema(description = "Filter by profession or industry", example = "Technology")
    private String profession;
    
    @Schema(description = "Filter by city ID", example = "163")
    private Long cityId;
    
    @Schema(description = "Filter by country ID", example = "101")
    private Long countryId;
    
    @Schema(description = "Sort order for results", example = "relevance", allowableValues = {"relevance", "name", "batch", "connections"})
    @Pattern(regexp = "^(relevance|name|batch|connections)$", message = "SortBy must be one of: relevance, name, batch, connections")
    private String sortBy = "relevance";
    
    @Schema(description = "Page number (0-based)", example = "0", minimum = "0")
    @Min(value = 0, message = "Page number must be 0 or greater")
    @Max(value = 1000, message = "Page number cannot exceed 1000")
    private int page = 0;
    
    @Schema(description = "Number of results per page", example = "12", minimum = "1", maximum = "100")
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100")
    private int size = 12;
}
