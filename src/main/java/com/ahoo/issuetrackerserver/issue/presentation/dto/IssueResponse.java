package com.ahoo.issuetrackerserver.issue.presentation.dto;

import com.ahoo.issuetrackerserver.issue.domain.Issue;
import com.ahoo.issuetrackerserver.member.presentation.dto.MemberResponse;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이슈 단건 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueResponse {

    @Schema(description = "이슈 아이디")
    private Long id;

    @Schema(description = "이슈 제목")
    private String title;

    @Schema(description = "이슈 생성자")
    private MemberResponse author;

    @Schema(description = "이슈 상태")
    private boolean isClosed;

    @Schema(description = "이슈 코멘트 목록")
    private List<CommentResponse> comments;

    @Schema(description = "이슈 할당자 목록")
    private IssueAssigneesResponse issueAssignees;

    @Schema(description = "이슈 라벨 목록")
    private IssueLabelsResponse issueLabels;

    @Schema(description = "이슈 마일스톤")
    private MilestoneResponse milestone;

    @Schema(description = "이슈 변경이력")
    private List<IssueHistoryResponse> issueHistories = new ArrayList<>();

    @Schema(description = "이슈 생성 시각")
    private LocalDateTime createdAt;

    @Schema(description = "이슈 수정 시각")
    private LocalDateTime lastModifiedAt;

    public static IssueResponse from(Issue issue) {
        return new IssueResponse(
            issue.getId(),
            issue.getTitle(),
            MemberResponse.from(issue.getAuthor()),
            issue.isClosed(),
            issue.getComments().stream()
                .map(CommentResponse::from)
                .collect(Collectors.toUnmodifiableList()),
            IssueAssigneesResponse.from(issue.getAssignees()),
            IssueLabelsResponse.from(issue.getLabels()),
            MilestoneResponse.from(issue.getMilestone()),
            issue.getLogs().stream()
                .map(history -> IssueHistoryResponse.issueHistoryMapper(history.getAction(), history))
                .collect(Collectors.toUnmodifiableList()),
            issue.getCreatedAt(),
            issue.getLastModifiedAt()
        );
    }
}
