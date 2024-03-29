package com.ahoo.issuetrackerserver.auth.presentation.dto;

import com.ahoo.issuetrackerserver.auth.infrastructure.jwt.AccessToken;
import com.ahoo.issuetrackerserver.member.presentation.dto.MemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "Auth 유저정보 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthResponse {

    @Schema(title = "회원가입을 폼을 위한 Auth User 정보")
    private AuthUserResponse signUpFormData;

    @Schema(title = "로그인 성공 응답")
    private MemberResponse signInMember;

    @Schema(title = "AccessToken 값")
    private AccessToken accessToken;

    public static AuthResponse from(AuthUserResponse authUserResponse) {
        return new AuthResponse(authUserResponse, null, null);
    }

    public static AuthResponse from(MemberResponse memberResponse, AccessToken accessToken) {
        return new AuthResponse(null, memberResponse, accessToken);
    }
}
