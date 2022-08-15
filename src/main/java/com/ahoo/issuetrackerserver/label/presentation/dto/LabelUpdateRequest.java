package com.ahoo.issuetrackerserver.label.presentation.dto;

import com.ahoo.issuetrackerserver.label.domain.Label;
import com.ahoo.issuetrackerserver.label.domain.TextBrightness;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelUpdateRequest {

    private String title;
    private String backgroundColorCode;
    private String description;
    private TextBrightness textBrightness;

    public void updateRequest(Label label) {
        this.title = Optional.ofNullable(this.title).orElseGet(label::getTitle);
        this.backgroundColorCode = Optional.ofNullable(this.backgroundColorCode)
            .orElseGet(label::getBackgroundColorCode);
        this.description = Optional.ofNullable(this.description).orElseGet(label::getDescription);
        this.textBrightness = Optional.ofNullable(this.textBrightness).orElseGet(label::getTextBrightness);
    }
}
