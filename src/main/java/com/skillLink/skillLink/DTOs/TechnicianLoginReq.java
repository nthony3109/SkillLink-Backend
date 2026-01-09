package com.skillLink.skillLink.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianLoginReq {
    @NotBlank(message = "email is required for login")
    private String email;

    @NotBlank(message = "password is required")
    private String password;
}
