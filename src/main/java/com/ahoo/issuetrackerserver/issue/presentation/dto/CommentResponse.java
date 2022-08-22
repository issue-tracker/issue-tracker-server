package com.ahoo.issuetrackerserver.issue.presentation.dto;

import com.ahoo.issuetrackerserver.issue.domain.Comment;
import com.ahoo.issuetrackerserver.member.presentation.dto.MemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "코멘트 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentResponse {

    @Schema(description = "코멘트 응답 아이디")
    private Long id;

    @Schema(description = "코멘트 작성자")
    private MemberResponse author;

    @Schema(description = "코멘트 내용")
    private String content;

    @Schema(description = "코멘트 생성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "코멘트 반응 목록 응답")
    private List<IssueCommentReactionResponse> issueCommentReactionsResponse = new ArrayList<>();

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            MemberResponse.from(comment.getAuthor()),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getReactions().stream()
                .map(IssueCommentReactionResponse::from)
                .collect(Collectors.toUnmodifiableList())
        );
    }
}
