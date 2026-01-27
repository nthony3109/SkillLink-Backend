package com.skillLink.skillLink.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianRegisterReq {
    @NotBlank(message = "firstname is requires")
    private String firstname;

    @NotBlank(message = "lastname is requires")
    private String lastname;

    private String othernames;

    @NotBlank(message = "username is requires")
    private String username;

    @NotBlank(message = "phone number  is requires")
    private String phone;

    @NotBlank(message = "email is requires")
    private  String email;

    @NotBlank(message = "password can not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
            message = "Password must contain uppercase, lowercase, number, and special character"
    )
    private  String password;

    private String profileImageUrl;
    private  String locationName;

    @NotNull(message = "lat and lgn is required")
    private double lat;

    @NotNull(message = "longitude is required")
    private double lng;

    private String bio;

    @Size(min = 1, max = 3, message = "A technician must have at least 1 and at most 3 technicianServiceModels")
    private List<String> serviceNames;
}
