package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.utils.AppUtils;
import com.ijaa.user.domain.common.ApiResponse;
import com.ijaa.user.domain.common.PagedResponse;
import com.ijaa.user.domain.dto.AlumniSearchDto;
import com.ijaa.user.domain.request.AlumniSearchRequest;
import com.ijaa.user.service.AlumniSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/alumni")
@RequiredArgsConstructor
public class AlumniSearchResource {

    private final AlumniSearchService alumniSearchService;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<AlumniSearchDto>>> searchAlumni(
            @RequestBody AlumniSearchRequest request) {

        PagedResponse<AlumniSearchDto> result = alumniSearchService.searchAlumni(request);
        return ResponseEntity.ok(
                new ApiResponse<>("Alumni search completed successfully", "200", result)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<AlumniSearchDto>>> searchAlumniGet(
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) String batch,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "relevance") String sortBy,
            @RequestParam(defaultValue = "0") int page,
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

    @PostMapping("/sync-profile")
    public ResponseEntity<ApiResponse<Void>> syncProfile() {
        alumniSearchService.syncAlumniProfile();
        return ResponseEntity.ok(
                new ApiResponse<>("Profile synced successfully", "200", null)
        );
    }
}
