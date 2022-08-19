package com.ahoo.issuetrackerserver.issue.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이슈 등록 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueCreateRequest {

    @Schema(description = "이슈 제목", required = true)
    @NotBlank
    private String title;

    @Schema(description = "이슈 코멘트 내용")
    private String comment;

    @Schema(description = "담당자 아이디 목록")
    private List<Long> assigneeIds = new ArrayList<>();

    @Schema(description = "이슈 제목 아이디 목록")
    private List<Long> labelIds = new ArrayList<>();

    @Schema(description = "마일스톤 아이디")
    private Long milestoneId;
}
