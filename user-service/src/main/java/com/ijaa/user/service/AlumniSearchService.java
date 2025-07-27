package com.ijaa.user.service;

import com.ijaa.user.domain.common.PagedResponse;
import com.ijaa.user.domain.dto.AlumniSearchDto;
import com.ijaa.user.domain.request.AlumniSearchRequest;

public interface AlumniSearchService {
    PagedResponse<AlumniSearchDto> searchAlumni(AlumniSearchRequest request);
    void syncAlumniProfile();
}
