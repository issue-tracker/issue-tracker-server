package com.ahoo.issuetrackerserver.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthUserResponse {

    private String email;

    @JsonProperty("avatar_url")
    private String profileImage;

    public void setEmail(String email) {
        this.email = email;
    }
}
