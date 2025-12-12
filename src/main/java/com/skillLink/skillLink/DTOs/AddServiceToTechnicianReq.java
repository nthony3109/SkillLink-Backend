package com.skillLink.skillLink.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddServiceToTechnicianReq {
    @NotBlank(message = "Service name is required")
    private String name;
}
