package com.ahoo.issuetrackerserver.issue.presentation.dto;

import com.ahoo.issuetrackerserver.issue.domain.Reaction;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이슈 댓글 반응 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueCommentReactionResponse {

    @Schema(description = "이슈 댓글 반응 이모지 유니코드")
    private String emoji;

    @Schema(description = "이슈 댓글에 반응한 사람 응답")
    private IssueCommentReactorResponse issueCommentReactorResponse;

    public static IssueCommentReactionResponse from(Reaction reaction) {
        return new IssueCommentReactionResponse(
            reaction.getEmoji().getUnicode(),
            IssueCommentReactorResponse.from(reaction.getReactor())
        );
    }
}
