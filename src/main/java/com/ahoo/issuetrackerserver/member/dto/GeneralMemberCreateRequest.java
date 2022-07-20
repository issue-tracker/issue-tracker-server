package com.ahoo.issuetrackerserver.member.dto;

import com.ahoo.issuetrackerserver.member.AuthProvider;
import com.ahoo.issuetrackerserver.member.Member;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GeneralMemberCreateRequest {

    private String loginId;
    private String password;
    private String email;
    private String nickName;
    private String profileImage;

    public Member toEntity() {
        return Member.of(
            null,
            this.loginId,
            this.password,
            this.email,
            this.nickName,
            Optional.ofNullable(this.profileImage).orElse("defaultS3ImageUrl"),
            AuthProvider.NONE);
    }
}
