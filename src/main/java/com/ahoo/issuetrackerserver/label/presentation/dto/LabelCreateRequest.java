package com.ahoo.issuetrackerserver.label.presentation.dto;

import com.ahoo.issuetrackerserver.label.domain.TextBrightness;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelCreateRequest {

    private String title;
    private String backgroundColorCode;
    private String description;
    private TextBrightness textBrightness;

}
