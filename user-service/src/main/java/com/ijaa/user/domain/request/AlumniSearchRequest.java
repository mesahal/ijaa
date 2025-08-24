package com.ijaa.user.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request model for alumni search with various filters and pagination")
public class AlumniSearchRequest {
    
    @Schema(description = "Search query to find alumni by name, profession, or bio", example = "software engineer")
    private String searchQuery;
    
    @Schema(description = "Filter by graduation batch year", example = "2020")
    private String batch;
    
    @Schema(description = "Filter by profession or industry", example = "Technology")
    private String profession;
    
    @Schema(description = "Filter by location", example = "Bangalore")
    private String location;
    
    @Schema(description = "Sort order for results", example = "relevance", allowableValues = {"relevance", "name", "batch", "connections"})
    private String sortBy = "relevance";
    
    @Schema(description = "Page number (0-based)", example = "0", minimum = "0")
    private int page = 0;
    
    @Schema(description = "Number of results per page", example = "12", minimum = "1", maximum = "100")
    private int size = 12;
}
