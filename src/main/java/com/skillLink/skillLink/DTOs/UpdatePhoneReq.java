package com.skillLink.skillLink.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePhoneReq {
    @NotBlank(message = "Phone number is required")
    private String phone;

}
