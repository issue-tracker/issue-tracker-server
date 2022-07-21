package com.ahoo.issuetrackerserver.member.dto;

import com.ahoo.issuetrackerserver.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponse {

    private Long id;
    private String email;
    private String nickname;
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
