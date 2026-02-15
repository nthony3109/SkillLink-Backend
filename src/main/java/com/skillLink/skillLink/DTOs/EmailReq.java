package com.skillLink.skillLink.DTOs;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailReq {
    @NotEmpty(message = "email must not be empty")
    private String email;
}
