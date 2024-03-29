package com.ahoo.issuetrackerserver.issue.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Schema(description = "이슈 제목 수정 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueTitleUpdateRequest {

    @Schema(description = "수정할 제목")
    @NotBlank
    @Length(max = 255)
    private String title;
}
