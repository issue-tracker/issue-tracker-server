package com.ahoo.issuetrackerserver.issue.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이슈 코멘트 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueCommentRequest {

    @Schema(description = "코멘트 내용")
    private String content;
}
