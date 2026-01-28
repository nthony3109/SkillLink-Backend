package com.skillLink.skillLink.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateLocationNameReq {
    @NotBlank(message = "Location name is required")
    private String locationName;
}
