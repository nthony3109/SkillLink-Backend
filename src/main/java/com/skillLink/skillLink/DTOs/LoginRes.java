package com.skillLink.skillLink.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRes {
    private String accessToken;
    private String refreshToken;
    private Long userId;
}
