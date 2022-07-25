package com.ahoo.issuetrackerserver.member.dto;

import com.ahoo.issuetrackerserver.auth.AuthProvider;
import com.ahoo.issuetrackerserver.member.Member;
import java.util.Optional;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneralMemberCreateRequest {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 6, max = 12, message = "아이디는 최소 6자리, 최대 12자리여야 합니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 최소 8자리, 최대 16자리여야 합니다.")
    private String password;

    @NotNull(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 12, message = "닉네임은 최소 2자리, 최대 12자리여야 합니다.")
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

    public static GeneralMemberCreateRequest of(String loginId, String password, String email, String nickname,
        String profileImage) {
        return new GeneralMemberCreateRequest(loginId, password, email, nickname,
            Optional.ofNullable(profileImage).orElse("defaultS3ImageUrl"));
    }
}
