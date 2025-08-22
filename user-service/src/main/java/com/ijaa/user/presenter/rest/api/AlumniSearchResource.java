package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.utils.AppUtils;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.common.PagedResponse;
import com.ijaa.user.domain.dto.AlumniSearchDto;
import com.ijaa.user.domain.request.AlumniSearchRequest;
import com.ijaa.user.service.AlumniSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/alumni")
@RequiredArgsConstructor
@Tag(name = "Alumni Search", description = "Alumni search and discovery APIs for finding and connecting with other alumni")
public class AlumniSearchResource {

    private final AlumniSearchService alumniSearchService;

    @PostMapping("/search")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Search Alumni (POST)",
        description = "Search for alumni using advanced filters and criteria. Supports pagination and multiple search parameters. (USER role required)",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Alumni search criteria with filters",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AlumniSearchRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Basic Search",
                        value = """
                            {
                                "searchQuery": "software engineer",
                                "batch": "2020",
                                "profession": "Technology",
                                "location": "Bangalore",
                                "sortBy": "relevance",
                                "page": 0,
                                "size": 12
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Location-based Search",
                        value = """
                            {
                                "location": "Mumbai",
                                "profession": "Finance",
                                "sortBy": "batch",
                                "page": 0,
                                "size": 20
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Batch-based Search",
                        value = """
                            {
                                "batch": "2019",
                                "sortBy": "name",
                                "page": 0,
                                "size": 10
                            }
                            """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Alumni search completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Alumni search completed successfully",
                                "code": "200",
                                "data": {
                                    "content": [
                                        {
                                            "userId": "user123",
                                            "name": "John Doe",
                                            "batch": "2020",
                                            "profession": "Software Engineer",
                                            "location": "Bangalore, India",
                                            "avatar": "https://example.com/avatar.jpg",
                                            "bio": "Passionate software engineer with 3+ years of experience",
                                            "connections": 45,
                                            "isConnected": false,
                                            "interests": ["Java", "Spring Boot", "Microservices"]
                                        }
                                    ],
                                    "totalElements": 1,
                                    "totalPages": 1,
                                    "currentPage": 0,
                                    "size": 12,
                                    "first": true,
                                    "last": true
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid search criteria provided",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Invalid search criteria provided",
                                "code": "400",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Missing or invalid authentication token",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Unauthorized",
                        value = """
                            {
                                "message": "User context not found in request headers",
                                "code": "401",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error during search operation",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Server Error",
                        value = """
                            {
                                "message": "An error occurred while searching alumni",
                                "code": "500",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<PagedResponse<AlumniSearchDto>>> searchAlumni(
            @RequestBody AlumniSearchRequest request) {

        PagedResponse<AlumniSearchDto> result = alumniSearchService.searchAlumni(request);
        return ResponseEntity.ok(
                new ApiResponse<>("Alumni search completed successfully", "200", result)
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    @Operation(
        summary = "Search Alumni (GET)",
        description = "Search for alumni using query parameters. Supports basic filtering and pagination. (USER role required)",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Alumni search completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.user.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Alumni search completed successfully",
                                "code": "200",
                                "data": {
                                    "content": [
                                        {
                                            "userId": "user456",
                                            "name": "Jane Smith",
                                            "batch": "2019",
                                            "profession": "Data Scientist",
                                            "location": "Mumbai, India",
                                            "avatar": "https://example.com/avatar2.jpg",
                                            "bio": "Data scientist specializing in machine learning",
                                            "connections": 32,
                                            "isConnected": true,
                                            "interests": ["Python", "Machine Learning", "Data Analysis"]
                                        }
                                    ],
                                    "totalElements": 1,
                                    "totalPages": 1,
                                    "currentPage": 0,
                                    "size": 12,
                                    "first": true,
                                    "last": true
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized - Missing or invalid authentication token",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Unauthorized",
                        value = """
                            {
                                "message": "User context not found in request headers",
                                "code": "401",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<PagedResponse<AlumniSearchDto>>> searchAlumniGet(
            @Parameter(description = "Search query to find alumni by name, profession, or bio", example = "software engineer")
            @RequestParam(required = false) String searchQuery,
            
            @Parameter(description = "Filter by graduation batch year", example = "2020")
            @RequestParam(required = false) String batch,
            
            @Parameter(description = "Filter by profession or industry", example = "Technology")
            @RequestParam(required = false) String profession,
            
            @Parameter(description = "Filter by location", example = "Bangalore")
            @RequestParam(required = false) String location,
            
            @Parameter(description = "Sort order for results", example = "relevance", schema = @Schema(allowableValues = {"relevance", "name", "batch", "connections"}))
            @RequestParam(defaultValue = "relevance") String sortBy,
            
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Number of results per page", example = "12")
            @RequestParam(defaultValue = "12") int size) {

        AlumniSearchRequest request = new AlumniSearchRequest();
        request.setSearchQuery(searchQuery);
        request.setBatch(batch);
        request.setProfession(profession);
        request.setLocation(location);
        request.setSortBy(sortBy);
        request.setPage(page);
        request.setSize(size);

        PagedResponse<AlumniSearchDto> result = alumniSearchService.searchAlumni(request);
        return ResponseEntity.ok(
                new ApiResponse<>("Alumni search completed successfully", "200", result)
        );
    }
}
