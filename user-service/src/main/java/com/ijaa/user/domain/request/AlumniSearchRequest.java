package com.ijaa.user.domain.request;

import lombok.Data;

@Data
public class AlumniSearchRequest {
    private String searchQuery;
    private String batch;
    private String profession;
    private String location;
    private String sortBy = "relevance";
    private int page = 0;
    private int size = 12;
}
