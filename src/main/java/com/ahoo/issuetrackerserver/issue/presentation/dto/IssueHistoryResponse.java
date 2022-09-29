package com.ahoo.issuetrackerserver.issue.presentation.dto;

import com.ahoo.issuetrackerserver.issue.domain.IssueHistory;
import com.ahoo.issuetrackerserver.issue.domain.IssueUpdateAction;
import com.ahoo.issuetrackerserver.label.domain.Label;
import com.ahoo.issuetrackerserver.member.domain.Member;
import com.ahoo.issuetrackerserver.member.presentation.dto.MemberResponse;
import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이슈 변경 이력")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueHistoryResponse {

    @Schema(description = "수정자")
    private MemberResponse modifier;

    @Schema(description = "수정시간")
    private LocalDateTime modifiedAt;

    @Schema(description = "변경 행동")
    private IssueUpdateAction action;

    @Schema(description = "수정된 라벨", nullable = true)
    private Label label;

    @Schema(description = "수정된 마일스톤", nullable = true)
    private IssueHistoryMilestone milestone;

    @Schema(description = "수정된 할당자", nullable = true)
    private Member assignee;

    @Schema(description = "변경 전 제목", nullable = true)
    private String previousTitle;

    @Schema(description = "변경 후 제목", nullable = true)
    private String changedTitle;

    public static IssueHistoryResponse issueHistoryMapper(IssueUpdateAction action, IssueHistory issueHistory) {
        if (action == IssueUpdateAction.ADD_ASSIGNEE || action == IssueUpdateAction.REMOVE_ASSIGNEE) {
            return updateIssueAssigneeHistoryFrom(issueHistory);
        }
        if (action == IssueUpdateAction.ADD_LABEL || action == IssueUpdateAction.REMOVE_LABEL) {
            return updateIssueLabelHistoryFrom(issueHistory);
        }
        if (action == IssueUpdateAction.ADD_MILESTONE || action == IssueUpdateAction.REMOVE_MILESTONE) {
            return updateIssueMilestoneHistoryFrom(issueHistory);
        }
        if (action == IssueUpdateAction.OPEN_ISSUE || action == IssueUpdateAction.CLOSE_ISSUE) {
            return updateIssueStatusHistoryFrom(issueHistory);
        }
        return updateIssueTitleHistoryFrom(issueHistory);
    }

    private static IssueHistoryResponse updateIssueTitleHistoryFrom(IssueHistory issueHistory) {
        return new IssueHistoryResponse(
            MemberResponse.from(issueHistory.getModifier()),
            issueHistory.getCreatedAt(),
            issueHistory.getAction(),
            null,
            null,
            null,
            issueHistory.getPreviousTitle(),
            issueHistory.getChangedTitle()
        );
    }

    private static IssueHistoryResponse updateIssueStatusHistoryFrom(IssueHistory issueHistory) {
        return new IssueHistoryResponse(
            MemberResponse.from(issueHistory.getModifier()),
            issueHistory.getCreatedAt(),
            issueHistory.getAction(),
            null,
            null,
            null,
            null,
            null
        );
    }

    private static IssueHistoryResponse updateIssueLabelHistoryFrom(IssueHistory issueHistory) {
        return new IssueHistoryResponse(
            MemberResponse.from(issueHistory.getModifier()),
            issueHistory.getCreatedAt(),
            issueHistory.getAction(),
            issueHistory.getLabel(),
            null,
            null,
            null,
            null
        );
    }

    private static IssueHistoryResponse updateIssueMilestoneHistoryFrom(IssueHistory issueHistory) {
        return new IssueHistoryResponse(
            MemberResponse.from(issueHistory.getModifier()),
            issueHistory.getCreatedAt(),
            issueHistory.getAction(),
            null,
            IssueHistoryMilestone.from(issueHistory.getMilestone()),
            null,
            null,
            null
        );
    }

    private static IssueHistoryResponse updateIssueAssigneeHistoryFrom(IssueHistory issueHistory) {
        return new IssueHistoryResponse(
            MemberResponse.from(issueHistory.getModifier()),
            issueHistory.getCreatedAt(),
            issueHistory.getAction(),
            null,
            null,
            issueHistory.getAssignee(),
            null,
            null
        );
    }

    public IssueHistoryResponse(Member modifier, LocalDateTime modifiedAt,
        IssueUpdateAction action, Label label, Milestone milestone,
        Member assignee, String previousTitle, String changedTitle) {
        this.modifier = MemberResponse.from(modifier);
        this.modifiedAt = modifiedAt;
        this.action = action;
        this.label = label;
        this.milestone = IssueHistoryMilestone.from(milestone);
        this.assignee = assignee;
        this.previousTitle = previousTitle;
        this.changedTitle = changedTitle;
    }
}
