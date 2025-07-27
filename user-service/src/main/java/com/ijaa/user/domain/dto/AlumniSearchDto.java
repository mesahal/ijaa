package com.ijaa.user.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class AlumniSearchDto {
    private Long id;
    private String name;
    private String batch;
    private String department;
    private String profession;
    private String company;
    private String location;
    private String avatar;
    private String bio;
    private Integer connections;
    private Boolean isConnected;
    private List<String> skills;
}
