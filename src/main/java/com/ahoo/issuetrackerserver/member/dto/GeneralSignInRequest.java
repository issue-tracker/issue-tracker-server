package com.ahoo.issuetrackerserver.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "일반 로그인 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneralSignInRequest {

    @Schema(description = "아이디", required = true)
    private String id;

    @Schema(description = "비밀번호", required = true)
    private String password;

    public static GeneralSignInRequest of(String id, String password) {
        return new GeneralSignInRequest(id, password);
    }
}
