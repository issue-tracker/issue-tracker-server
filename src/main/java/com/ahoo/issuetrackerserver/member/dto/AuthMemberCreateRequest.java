package com.ahoo.issuetrackerserver.member.dto;

import com.ahoo.issuetrackerserver.member.AuthProvider;
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
public class AuthMemberCreateRequest {

    @NotNull(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 12, message = "닉네임은 최소 2자리, 최대 12자리여야 합니다.")
    private String nickname;

    private String profileImage;

    //TODO: Add Enum Validate Custom Annotation
    private String authProviderType;

    public Member toEntity() {
        return Member.of(
            null,
            null,
            null,
            this.email,
            this.nickname,
            Optional.ofNullable(this.profileImage).orElse("defaultS3ImageUrl"),
            AuthProvider.valueOf(this.authProviderType)
        );
    }
}
