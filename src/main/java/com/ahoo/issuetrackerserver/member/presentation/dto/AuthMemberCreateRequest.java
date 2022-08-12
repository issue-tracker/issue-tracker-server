package com.ahoo.issuetrackerserver.member.presentation.dto;

import com.ahoo.issuetrackerserver.auth.domain.AuthProvider;
import com.ahoo.issuetrackerserver.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "Auth 회원가입 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthMemberCreateRequest {

    @Schema(description = "이메일", required = true, example = "ader@gmail.com")
    @NotNull(message = "이메일은 필수 입력 값입니다.")
    @Email
    private String email;

    @Schema(description = "닉네임", required = true, minLength = 2, maxLength = 12)
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 12, message = "닉네임은 최소 2자리, 최대 12자리여야 합니다.")
    private String nickname;

    @Schema(description = "프로필 사진 경로")
    private String profileImage;

    //TODO: Add Enum Validate Custom Annotation
    @Schema(description = "Auth Provider 이름", example = "GITHUB")
    private String authProviderType;

    private String resourceOwnerId;

    public Member toEntity() {
        return Member.of(
            null,
            null,
            null,
            this.email,
            this.nickname,
            Optional.ofNullable(this.profileImage).orElse("defaultS3ImageUrl"),
            AuthProvider.valueOf(this.authProviderType),
            this.resourceOwnerId
        );
    }

    public static AuthMemberCreateRequest of(String email, String nickname, String profileImage,
        String authProviderType, String resourceOwnerId) {
        return new AuthMemberCreateRequest(email, nickname, profileImage, authProviderType, resourceOwnerId);
    }
}
