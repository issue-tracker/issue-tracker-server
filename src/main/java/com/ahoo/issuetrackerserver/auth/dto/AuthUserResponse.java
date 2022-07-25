package com.ahoo.issuetrackerserver.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AuthUserResponse {

    private String email;

    private String profileImage;

    public void setEmail(String email) {
        this.email = email;
    }
}
