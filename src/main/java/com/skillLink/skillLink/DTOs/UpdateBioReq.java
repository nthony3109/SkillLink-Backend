package com.skillLink.skillLink.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBioReq {
    @NotBlank(message = "Bio cannot be blank")
    private String bio;
}
