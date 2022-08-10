package com.ahoo.issuetrackerserver.member.dto;

import com.ahoo.issuetrackerserver.auth.AccessToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "회원 정보와 액세스 토큰 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SignResponse {

    @Schema(description = "회원 응답")
    private MemberResponse memberResponse;

    @Schema(description = "액세스 토큰 응답")
    private AccessToken accessToken;

    public static SignResponse of(MemberResponse memberResponse, AccessToken accessToken) {
        return new SignResponse(memberResponse, accessToken);
    }
}
