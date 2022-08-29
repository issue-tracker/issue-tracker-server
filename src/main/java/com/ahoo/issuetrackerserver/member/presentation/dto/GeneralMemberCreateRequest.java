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

@Schema(description = "일반 회원가입 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GeneralMemberCreateRequest {

    private static final String DEFAULT_S3_IMAGE_URL = "https://avatars.githubusercontent.com/u/85747667?v=4";

    @Schema(description = "아이디", required = true, minLength = 6, maxLength = 12)
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 6, max = 12, message = "아이디는 최소 6자리, 최대 12자리여야 합니다.")
    private String signInId;

    @Schema(description = "비밀번호", required = true, minLength = 8, maxLength = 16)
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, max = 16, message = "비밀번호는 최소 8자리, 최대 16자리여야 합니다.")
    private String password;

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

    public Member toEntity() {
        return Member.of(
            null,
            this.signInId,
            this.password,
            this.email,
            this.nickname,
            Optional.ofNullable(this.profileImage).orElse(DEFAULT_S3_IMAGE_URL),
            AuthProvider.NONE,
            null);
    }

    public static GeneralMemberCreateRequest of(String signInId, String password, String email, String nickname,
        String profileImage) {
        return new GeneralMemberCreateRequest(signInId, password, email, nickname,
            Optional.ofNullable(profileImage).orElse(DEFAULT_S3_IMAGE_URL));
    }
}
