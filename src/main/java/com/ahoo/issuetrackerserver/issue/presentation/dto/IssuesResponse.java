package com.ahoo.issuetrackerserver.issue.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Schema(description = "이슈 목록 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssuesResponse {

    @Schema(description = "열린 이슈 갯수")
    private Long openIssueCount;

    @Schema(description = "닫힌 이슈 갯수")
    private Long closedIssueCount;

    @Schema(description = "이슈 목록")
    private Page<IssueResponse> issues;

    public static IssuesResponse of(Long openIssueCount, Long closedIssueCount, Page<IssueResponse> issues) {
        return new IssuesResponse(
            openIssueCount,
            closedIssueCount,
            issues
        );
    }
}
