package com.skillLink.skillLink.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateServiceReq {
    @NotBlank(message = "old service name is required")
    private String oldServiceName;

    @NotBlank(message = "new service name is required")
    private String newServiceName;
}
