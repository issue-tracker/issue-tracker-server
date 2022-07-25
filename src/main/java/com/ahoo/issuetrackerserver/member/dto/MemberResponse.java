package com.ahoo.issuetrackerserver.member.dto;

import com.ahoo.issuetrackerserver.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "회원 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    @Schema(description = "식별자")
    private Long id;

    @Schema(description = "이메일", example = "ader@gmail.com")
    private String email;

    @Schema(description = "닉네임", minLength = 2, maxLength = 12)
    private String nickname;

    @Schema(description = "프로필 이미지")
    private String profileImage;

    public static MemberResponse of(Long id, String email, String nickname, String profileImage) {
        return new MemberResponse(id, email, nickname, profileImage);
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getEmail(),
            member.getNickname(),
            member.getProfileImage()
        );
    }
}
