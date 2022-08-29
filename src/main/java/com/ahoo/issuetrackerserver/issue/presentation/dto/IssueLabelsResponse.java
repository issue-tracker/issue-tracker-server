package com.ahoo.issuetrackerserver.issue.presentation.dto;

import com.ahoo.issuetrackerserver.issue.domain.IssueLabel;
import com.ahoo.issuetrackerserver.label.presentation.dto.LabelResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이슈 라벨 목록 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueLabelsResponse {

    @Schema(description = "이슈 라벨 목록")
    private List<LabelResponse> issueLabels = new ArrayList<>();

    public static IssueLabelsResponse from(List<IssueLabel> issueLabels) {
        List<LabelResponse> labels = issueLabels.stream()
            .map(IssueLabel::getLabel)
            .map(LabelResponse::from)
            .collect(Collectors.toUnmodifiableList());
        return new IssueLabelsResponse(labels);
    }
}
