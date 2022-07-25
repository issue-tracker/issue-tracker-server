package com.ahoo.issuetrackerserver.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserRequest {

    private String clientId;
    private String clientSecret;
    private String code;
    private String redirectUrl;
}
