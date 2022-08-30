package com.ahoo.issuetrackerserver.milestone.presentation.dto;

import com.ahoo.issuetrackerserver.issue.domain.Issue;
import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "마일스톤 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MilestoneResponse {

    @Schema(description = "마일스톤 id")
    private Long id;

    @Schema(description = "마일스톤 제목")
    private String title;

    @Schema(description = "마일스톤 설명")
    private String description;

    @Schema(description = "마일스톤 완료일")
    private String dueDate;

    @Schema(description = "마일스톤 닫힘여부")
    private boolean isClosed;

    @Schema(description = "열린 이슈 갯수")
    private int openIssueCount;

    @Schema(description = "닫힌 이슈 갯수")
    private int closedIssueCount;

    public static MilestoneResponse from(Milestone milestone) {
        int closedIssueCount = (int) milestone.getIssues().stream()
            .filter(Issue::isClosed)
            .count();

        int openIssueCount = milestone.getIssues().size() - closedIssueCount;

        return new MilestoneResponse(
            milestone.getId(),
            milestone.getTitle(),
            milestone.getDescription(),
            (milestone.getDueDate() != null) ? milestone.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : null,
            milestone.isClosed(),
            openIssueCount,
            closedIssueCount
        );
    }
}
