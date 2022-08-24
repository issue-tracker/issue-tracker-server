package com.ahoo.issuetrackerserver.issue.presentation.dto;

import com.ahoo.issuetrackerserver.issue.domain.IssueAssignee;
import com.ahoo.issuetrackerserver.member.presentation.dto.MemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이슈 할당자 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueAssigneesResponse {

    @Schema(description = "이슈 할당자 목록")
    private List<MemberResponse> issueAssignees = new ArrayList<>();

    public static IssueAssigneesResponse from(List<IssueAssignee> issueAssignees) {
        List<MemberResponse> assignees = issueAssignees.stream()
            .map(IssueAssignee::getAssignee)
            .map(MemberResponse::from)
            .collect(Collectors.toUnmodifiableList());

        return new IssueAssigneesResponse(assignees);
    }
}
