package com.ijaa.user.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class AlumniSearchDto {
    private String userId;
    private String name;
    private String batch;
    private String profession;
    private String location;
    private String avatar;
    private String bio;
    private Integer connections;
    private Boolean isConnected;
    private List<String> interests; // This field is already present
}
