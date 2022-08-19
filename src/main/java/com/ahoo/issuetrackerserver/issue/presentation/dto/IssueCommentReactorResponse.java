package com.ahoo.issuetrackerserver.issue.presentation.dto;

import com.ahoo.issuetrackerserver.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이슈의 댓글에 반응한 사람들")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueCommentReactorResponse {

    @Schema(description = "이슈의 댓글에 반응한 사람들 아이디")
    private Long id;

    @Schema(description = "이슈의 댓글에 반응한 사람들 닉네임")
    private String nickname;

    public static IssueCommentReactorResponse from(Member reactor) {
        return new IssueCommentReactorResponse(reactor.getId(), reactor.getNickname());
    }
}
