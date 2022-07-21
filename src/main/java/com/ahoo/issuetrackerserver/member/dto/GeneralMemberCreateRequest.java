package com.ahoo.issuetrackerserver.member.dto;

import com.ahoo.issuetrackerserver.member.AuthProvider;
import com.ahoo.issuetrackerserver.member.Member;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneralMemberCreateRequest {

    private String loginId;
    private String password;
    private String email;
    private String nickname;
    private String profileImage;

    public Member toEntity() {
        return Member.of(
            null,
            this.loginId,
            this.password,
            this.email,
            this.nickname,
            Optional.ofNullable(this.profileImage).orElse("defaultS3ImageUrl"),
            AuthProvider.NONE);
    }

    public static GeneralMemberCreateRequest of(String loginId, String password, String email, String nickname, String profileImage) {
        return new GeneralMemberCreateRequest(loginId, password, email, nickname, Optional.ofNullable(profileImage).orElse("defaultS3ImageUrl"));
    }
}
