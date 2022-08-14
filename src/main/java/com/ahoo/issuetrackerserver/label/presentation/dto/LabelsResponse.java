package com.ahoo.issuetrackerserver.label.presentation.dto;

import com.ahoo.issuetrackerserver.label.domain.Label;
import com.ahoo.issuetrackerserver.label.domain.TextBrightness;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelsResponse {

    private String title;
    private String colorCode;
    private String description;
    private TextBrightness textBrightness;

    public static LabelsResponse from(Label label) {
        return new LabelsResponse(label.getTitle(), label.getBackgroundColorCode(), label.getDescription(),
            label.getTextBrightness());
    }
}
