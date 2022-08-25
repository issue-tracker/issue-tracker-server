package com.ahoo.issuetrackerserver.label.presentation.dto;

import com.ahoo.issuetrackerserver.label.domain.TextColor;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Schema(description = "라벨 생성 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelCreateRequest {

    @Schema(description = "라벨 이름")
    @NotBlank
    @Length(max = 255)
    private String title;

    @Schema(description = "라벨 배경색 코드")
    @NotBlank
    @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$", message = "유효하지 않은 색상 코드입니다.")
    private String backgroundColorCode;

    @Schema(description = "라벨 설명")
    @Length(max = 1000)
    private String description;

    @Schema(description = "라벨 글자색")
    @NotNull
    private TextColor textColor;

}
