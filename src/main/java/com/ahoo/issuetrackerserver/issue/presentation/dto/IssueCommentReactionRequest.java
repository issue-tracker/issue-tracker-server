package com.ahoo.issuetrackerserver.issue.presentation.dto;

import com.ahoo.issuetrackerserver.issue.domain.Emoji;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이슈 코멘트 리액션 추가 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueCommentReactionRequest {

    @Schema(description = "리액션 이모지 이름")
    @NotNull
    private Emoji emoji;

}
