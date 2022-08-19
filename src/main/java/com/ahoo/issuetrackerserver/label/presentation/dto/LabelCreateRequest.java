package com.ahoo.issuetrackerserver.label.presentation.dto;

import com.ahoo.issuetrackerserver.common.exception.ErrorMessage;
import com.ahoo.issuetrackerserver.label.domain.TextColor;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "라벨 생성 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelCreateRequest {

    @Schema(description = "라벨 이름")
    @NotBlank
    private String title;

    @Schema(description = "라벨 배경색 코드")
    @NotBlank
    @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = ErrorMessage.INVALID_HEX_COLOR_CODE)
    private String backgroundColorCode;

    @Schema(description = "라벨 설명")
    private String description;

    @Schema(description = "라벨 글자색")
    @NotNull
    private TextColor textColor;

}