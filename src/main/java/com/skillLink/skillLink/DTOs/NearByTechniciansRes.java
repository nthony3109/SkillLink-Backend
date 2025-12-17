package com.skillLink.skillLink.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class NearByTechniciansRes {
    private Long id;
    private String firstname;
    private  String lastname;
    private String username;
    private String phone;
    private String profileImageUrl;
    private String locationName;
    private double distanceInKm;
    private String bio;
    private List<String> services;

}
