package com.skillLink.skillLink.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetNearByTechniciansReq {
    @NotNull(message = "Latitude is required")
    private double lat;

    @NotNull(message = "Longitude is required")
    private double lng;

      @NotBlank(message = "Service name is required")
    private String serviceName;
}
