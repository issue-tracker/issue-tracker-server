package com.ahoo.issuetrackerserver.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthUserResponse {

    private String email;
    private String profileImage;
}
