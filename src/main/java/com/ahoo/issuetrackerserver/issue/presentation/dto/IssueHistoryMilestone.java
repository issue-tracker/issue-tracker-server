package com.ahoo.issuetrackerserver.issue.presentation.dto;

import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueHistoryMilestone {

    @Schema(description = "마일스톤 아이디")
    private Long id;

    @Schema(description = "마일스톤 제목")
    private String title;

    public static IssueHistoryMilestone from(Milestone milestone) {
        if (Objects.isNull(milestone)) {
            return null;
        }
        return new IssueHistoryMilestone(
            milestone.getId(),
            milestone.getTitle()
        );
    }
}
