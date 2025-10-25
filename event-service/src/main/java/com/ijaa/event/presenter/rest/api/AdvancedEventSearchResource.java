package com.ijaa.event.presenter.rest.api;

import com.ijaa.event.common.annotation.RequiresFeature;
import com.ijaa.event.common.annotation.RequiresRole;
import com.ijaa.event.common.utils.AppUtils;
import com.ijaa.event.common.utils.FeatureFlagUtils;
import com.ijaa.event.domain.common.ApiResponse;
import com.ijaa.event.domain.common.PagedResponse;
import com.ijaa.event.domain.request.AdvancedEventSearchRequest;
import com.ijaa.event.domain.response.EventResponse;
import com.ijaa.event.service.AdvancedEventSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(AppUtils.BASE_URL + "/advanced-search")
@RequiredArgsConstructor
@Tag(name = "Advanced Event Search")
public class AdvancedEventSearchResource {

    private final AdvancedEventSearchService advancedEventSearchService;
    private final FeatureFlagUtils featureFlagUtils;

    @PostMapping("/advanced")
    @RequiresRole("USER")
    @RequiresFeature("search.advanced-filters")
    @Operation(
        summary = "Unified Advanced Event Search",
        description = """
            Comprehensive event search with flexible filtering capabilities.
            
            **Features:**
            - Search by location, organizer, event type, dates
            - Filter upcoming events only
            - Online/offline filtering
            - Privacy and participant filters
            - Flexible pagination or limit-based results
            - Sorting by multiple criteria
            
            **All parameters are optional** - combine any filters as needed.
            """,
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Advanced search criteria (all fields optional)",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AdvancedEventSearchRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Search by Location",
                        summary = "Find events in a specific location",
                        value = """
                            {
                                "location": "IIT Campus",
                                "limit": 10
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Search by Organizer",
                        summary = "Find events by organizer name",
                        value = """
                            {
                                "organizerName": "John Doe",
                                "limit": 10
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Upcoming Events Only",
                        summary = "Get upcoming events with optional filters",
                        value = """
                            {
                                "upcomingOnly": true,
                                "location": "IIT Campus",
                                "eventType": "MEETING",
                                "limit": 10
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Complex Multi-Filter Search",
                        summary = "Combine multiple filters for precise results",
                        value = """
                            {
                                "query": "networking",
                                "location": "IIT",
                                "eventType": "NETWORKING",
                                "organizerName": "Alumni",
                                "upcomingOnly": true,
                                "isOnline": false,
                                "minParticipants": 20,
                                "sortBy": "start_date",
                                "sortOrder": "asc",
                                "page": 0,
                                "size": 20
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Online Events Search",
                        summary = "Search for online events with date range",
                        value = """
                            {
                                "isOnline": true,
                                "eventType": "WEBINAR",
                                "startDate": "2024-12-01T00:00:00",
                                "endDate": "2024-12-31T23:59:59",
                                "page": 0,
                                "size": 20
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
            description = "Events found successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Events found successfully",
                                "code": "200",
                                "data": {
                                    "content": [
                                        {
                                            "id": 1,
                                            "title": "Alumni Meet 2024",
                                            "description": "Annual alumni gathering",
                                            "startDate": "2024-12-25T18:00:00",
                                            "endDate": "2024-12-25T22:00:00",
                                            "location": "IIT Campus",
                                            "eventType": "MEETING",
                                            "active": true,
                                            "isOnline": false,
                                            "meetingLink": null,
                                            "maxParticipants": 100,
                                            "currentParticipants": 45,
                                            "organizerName": "John Doe",
                                            "organizerEmail": "john@example.com",
                                            "createdByUsername": "johndoe",
                                            "privacy": "PUBLIC",
                                            "inviteMessage": "Join us for the annual alumni meet",
                                            "createdAt": "2024-12-01T10:00:00",
                                            "updatedAt": "2024-12-01T10:00:00"
                                        }
                                    ],
                                    "totalElements": 1,
                                    "totalPages": 1,
                                    "currentPage": 0,
                                    "size": 10
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid search criteria",
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
            description = "Unauthorized - Missing or invalid token",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Unauthorized",
                        value = """
                            {
                                "message": "Missing Authorization Header",
                                "code": "401",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<PagedResponse<EventResponse>>> searchEvents(
            @RequestBody AdvancedEventSearchRequest request) {
        
        log.info("Processing advanced search request with filters: {}", request);
        
        PagedResponse<EventResponse> response = advancedEventSearchService.searchEvents(request);
        
        // Log feature usage
        featureFlagUtils.logFeatureUsage(FeatureFlagUtils.ADVANCED_SEARCH, "user");
        
        return ResponseEntity.ok(new ApiResponse<>("Events found successfully", "200", response));
    }

} 
