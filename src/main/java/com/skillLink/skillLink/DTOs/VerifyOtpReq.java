package com.skillLink.skillLink.DTOs;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpReq {
    @NotEmpty(message = "otp  is required to be verified")
    private  String otp;
    @NotEmpty(message = "email must cannot be empty")
    private String email;
}
