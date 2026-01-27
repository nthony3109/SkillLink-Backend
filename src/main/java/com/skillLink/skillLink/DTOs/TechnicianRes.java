package com.skillLink.skillLink.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianRes {
    private Long technicianId;
    private String firstname;
    private String lastname;
    private String username;
    private String phone;
    private String email;
    private String locationName;
    private  String profileImageUrl;
    private String bio;
    private List<String> services;

}
