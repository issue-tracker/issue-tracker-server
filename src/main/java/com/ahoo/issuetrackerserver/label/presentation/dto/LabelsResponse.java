package com.ahoo.issuetrackerserver.label.presentation.dto;

import com.ahoo.issuetrackerserver.label.domain.Label;
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

    public static LabelsResponse from(Label label) {
        return new LabelsResponse(label.getTitle(), label.getColorCode(), label.getDescription());
    }
}
