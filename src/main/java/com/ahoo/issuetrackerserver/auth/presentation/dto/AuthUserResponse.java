package com.ahoo.issuetrackerserver.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "Auth 유저정보 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AuthUserResponse {

    @Schema(description = "Auth 회원가입 여부를 판단하기 위한 리소스 오너 식별자값")
    private String resourceOwnerId;

    @Schema(description = "이메일", example = "hoo@gmail.com")
    private String email;

    @Schema(description = "프로필 이미지")
    private String profileImage;

    public void setEmail(String email) {
        this.email = email;
    }
}
