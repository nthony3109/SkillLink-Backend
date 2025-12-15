package com.skillLink.skillLink.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteServiceReq {
    @NotBlank(message = "service name is required")
    private String serviceName;
}
