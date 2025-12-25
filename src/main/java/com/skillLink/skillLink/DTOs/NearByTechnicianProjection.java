package com.skillLink.skillLink.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class NearByTechnicianProjection {
    private Long GetId;
    private String getFirstname;
    private  String getLastname;
    private String getUsername;
    private String getPhone;
    private String getProfileImageUrl;
    private String getLocationName;
    private double DistanceInKm;
    private String getBio;
    private List<String> getServices;
}
