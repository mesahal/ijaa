package com.ijaa.event.domain.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "Paginated response wrapper")
public class PagedResponse<T> {
    @Schema(description = "List of items in the current page")
    private List<T> content;
    
    @Schema(description = "Current page number (0-based)", example = "0")
    private int page;
    
    @Schema(description = "Number of items per page", example = "10")
    private int size;
    
    @Schema(description = "Total number of elements across all pages", example = "100")
    private long totalElements;
    
    @Schema(description = "Total number of pages", example = "10")
    private int totalPages;
    
    @Schema(description = "Whether this is the last page", example = "false")
    private boolean last;

    // Constructor for the 7-parameter usage in service implementations
    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean first, boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    public static <T> PagedResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);
        boolean last = page >= totalPages - 1;
        
        return new PagedResponse<>(content, page, size, totalElements, totalPages, false, last);
    }
}
