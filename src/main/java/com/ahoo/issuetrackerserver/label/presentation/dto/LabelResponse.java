package com.ahoo.issuetrackerserver.label.presentation.dto;

import com.ahoo.issuetrackerserver.label.domain.Label;
import com.ahoo.issuetrackerserver.label.domain.TextColor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "라벨 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelResponse {

    @Schema(description = "라벨 id")
    private Long id;

    @Schema(description = "라벨 제목")
    private String title;

    @Schema(description = "라벨 배경색 코드")
    private String backgroundColorCode;

    @Schema(description = "라벨 설명")
    private String description;

    @Schema(description = "라벨 글자색")
    private TextColor textColor;

    public static LabelResponse from(Label label) {
        return new LabelResponse(
            label.getId(),
            label.getTitle(),
            label.getBackgroundColorCode(),
            label.getDescription(),
            label.getTextColor());
    }
}
