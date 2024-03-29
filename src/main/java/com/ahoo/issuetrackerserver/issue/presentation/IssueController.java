package com.ahoo.issuetrackerserver.issue.presentation;

import com.ahoo.issuetrackerserver.common.argumentresolver.SignInMemberId;
import com.ahoo.issuetrackerserver.common.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.issue.application.IssueQueryParser;
import com.ahoo.issuetrackerserver.issue.application.IssueService;
import com.ahoo.issuetrackerserver.issue.presentation.dto.EmojiResponse;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueCommentRequest;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueCreateRequest;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueResponse;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueSearchFilter;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueStatusUpdateRequest;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueTitleUpdateRequest;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssuesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "issues", description = "이슈 API")
@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @Operation(summary = "이슈 등록",
        description = "이슈를 등록합니다.",
        responses = {
            @ApiResponse(responseCode = "201",
                description = "이슈 등록 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = IssueResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "이슈 등록 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponse create(@SignInMemberId Long memberId,
        @Valid @RequestBody IssueCreateRequest issueCreateRequest) {
        return issueService.save(memberId, issueCreateRequest);
    }

    @Operation(summary = "이슈 전체 조회",
        description = "이슈를 전체 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "이슈 전체 조회 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = IssueResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "이슈 전체 조회 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @GetMapping
    public IssuesResponse getIssues(
        @RequestParam int page,
        @RequestParam(required = false) String q) {
        IssueSearchFilter issueSearchFilter = IssueQueryParser.parse(q);
        return issueService.findAll(page, issueSearchFilter);
    }

    @Operation(summary = "이슈 단건 조회",
        description = "이슈를 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "이슈 조회 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = IssueResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "이슈 조회 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @GetMapping("/{id}")
    public IssueResponse getIssue(@PathVariable Long id) {
        return issueService.findById(id);
    }

    @Operation(summary = "이슈 상태 변경",
        description = "이슈의 상태를 변경합니다.",
        responses = {
            @ApiResponse(responseCode = "204",
                description = "이슈 상태 변경 성공"
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 상태 변경 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PatchMapping("/update-status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeIssueStatus(
        @SignInMemberId Long memberId,
        @Valid @RequestBody IssueStatusUpdateRequest issueStatusUpdateRequest) {
        issueService.updateStatus(issueStatusUpdateRequest.getStatus(), issueStatusUpdateRequest.getIds(), memberId);
    }

    @Operation(summary = "이슈 제목 수정",
        description = "이슈 제목을 수정합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "이슈 제목 수정 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = IssueResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "이슈 제목 수정 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PatchMapping("/{id}/title")
    public IssueResponse updateTitle(
        @SignInMemberId Long memberId,
        @PathVariable Long id,
        @Valid @RequestBody IssueTitleUpdateRequest issueTitleUpdateRequest) {
        return issueService.updateTitle(id, issueTitleUpdateRequest.getTitle(), memberId);
    }

    @Operation(summary = "이슈 담당자 추가",
        description = "이슈 담당자를 추가합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "이슈 담당자 추가 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = IssueResponse.class)
                    )
                }
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 담당자 추가 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PostMapping("/{issueId}/assignees/{assigneeId}")
    public IssueResponse addAssignee(
        @SignInMemberId Long memberId,
        @PathVariable("issueId") Long issueId,
        @PathVariable("assigneeId") Long assigneeId) {
        return issueService.addAssignee(issueId, assigneeId, memberId);
    }

    @Operation(summary = "이슈 담당자 삭제",
        description = "이슈 담당자를 일괄삭제(clear=true)하거나 단일삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "204",
                description = "이슈 담당자 삭제 성공"
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 담당자 삭제 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @DeleteMapping("/{issueId}/assignees")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssignees(
        @SignInMemberId Long memberId,
        @PathVariable Long issueId,
        @RequestParam(required = false, defaultValue = "false") boolean clear,
        @RequestParam(required = false) Long assigneeId
    ) {
        issueService.deleteAssignee(issueId, clear, assigneeId, memberId);
    }

    @Operation(summary = "이슈 라벨 추가",
        description = "이슈 라벨을 추가합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "이슈 라벨 추가 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = IssueResponse.class)
                    )
                }
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 라벨 추가 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PostMapping("/{issueId}/labels/{labelId}")
    public IssueResponse addLabel(
        @SignInMemberId Long memberId,
        @PathVariable("issueId") Long issueId,
        @PathVariable("labelId") Long labelId) {
        return issueService.addLabel(issueId, labelId, memberId);
    }

    @Operation(summary = "이슈 라벨 삭제",
        description = "이슈 라벨을 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "204",
                description = "이슈 라벨 삭제 성공"
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 라벨 삭제 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @DeleteMapping("/{issueId}/labels/{labelId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLabels(
        @SignInMemberId Long memberId,
        @PathVariable("issueId") Long issueId,
        @PathVariable("labelId") Long labelId
    ) {
        issueService.deleteLabel(issueId, labelId, memberId);
    }

    @Operation(summary = "이슈 마일스톤 추가",
        description = "이슈 마일스톤을 추가합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "이슈 마일스톤 추가 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = IssueResponse.class)
                    )
                }
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 마일스톤 추가 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PatchMapping("/{issueId}/milestone/{milestoneId}")
    public IssueResponse addMilestone(
        @SignInMemberId Long memberId,
        @PathVariable("issueId") Long issueId,
        @PathVariable(name = "milestoneId", required = true) Long milestoneId) {
        return issueService.addMilestone(issueId, milestoneId, memberId);
    }

    @Operation(summary = "이슈 마일스톤 삭제",
        description = "이슈 마일스톤을 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "204",
                description = "이슈 마일스톤 삭제 성공"
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 마일스톤 삭제 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @DeleteMapping("/{issueId}/milestone/{milestoneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMilestone(
        @SignInMemberId Long memberId,
        @PathVariable("issueId") Long issueId,
        @PathVariable("milestoneId") Long milestoneId) {
        issueService.deleteMilestone(issueId, milestoneId, memberId);
    }

    @Operation(summary = "이슈 삭제",
        description = "이슈 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "204",
                description = "이슈 삭제 성공"
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 삭제 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIssue(
        @SignInMemberId Long memberId,
        @PathVariable Long id) {
        issueService.deleteIssue(id, memberId);
    }

    @Operation(summary = "이슈 코멘트 등록",
        description = "이슈에 코멘트를 등록합니다.",
        responses = {
            @ApiResponse(responseCode = "201",
                description = "이슈 코멘트 등록 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = IssueResponse.class)
                    )
                }
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 코멘트 등록 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PostMapping("/{issueId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponse addComment(
        @SignInMemberId Long memberId,
        @PathVariable Long issueId,
        @Valid @RequestBody IssueCommentRequest issueCommentRequest) {
        return issueService.addComment(memberId, issueId, issueCommentRequest.getContent());
    }

    @Operation(summary = "이슈 코멘트 수정",
        description = "이슈에 코멘트를 수정합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "이슈 코멘트 수정 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = IssueResponse.class)
                    )
                }
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 코멘트 수정 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PatchMapping("/{issueId}/comments/{commentId}")
    public IssueResponse updateComment(
        @SignInMemberId Long memberId,
        @PathVariable("issueId") Long issueId,
        @PathVariable("commentId") Long commentId,
        @Valid @RequestBody IssueCommentRequest issueCommentRequest) {
        return issueService.updateComment(memberId, issueId, commentId, issueCommentRequest.getContent());
    }

    @Operation(summary = "이슈 코멘트 삭제",
        description = "이슈에 코멘트를 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "이슈 코멘트 삭제 성공"
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 코멘트 삭제 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @DeleteMapping("/{issueId}/comments/{commentId}")
    public void deleteComment(
        @SignInMemberId Long memberId,
        @PathVariable("issueId") Long issueId,
        @PathVariable("commentId") Long commentId) {
        issueService.deleteComment(memberId, issueId, commentId);
    }

    @Operation(summary = "이슈 코멘트 리액션 추가",
        description = "이슈에 코멘트 리액션을 추가합니다.",
        responses = {
            @ApiResponse(responseCode = "201",
                description = "이슈 코멘트 리액션 추가 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = IssueResponse.class)
                    )
                }
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 코멘트 리액션 추가 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PostMapping("/{issueId}/comments/{commentId}/reactions/{emojiName}")
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponse addReaction(
        @SignInMemberId Long memberId,
        @PathVariable("issueId") Long issueId,
        @PathVariable("commentId") Long commentId,
        @PathVariable("emojiName") String emojiName) {
        return issueService.addReaction(memberId, issueId, commentId, emojiName);
    }

    @Operation(summary = "이슈 코멘트 리액션 삭제",
        description = "이슈에 코멘트 리액션을 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "이슈 코멘트 리액션 삭제 성공"
            ),
            @ApiResponse(responseCode = "400",
                description = "이슈 코멘트 리액션 삭제 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @DeleteMapping("/{issueId}/comments/{commentId}/reactions/{reactionId}")
    public void deleteReaction(
        @SignInMemberId Long memberId,
        @PathVariable("issueId") Long issueId,
        @PathVariable("commentId") Long commentId,
        @PathVariable("reactionId") Long reactionId
    ) {
        issueService.deleteReaction(memberId, reactionId);
    }

    @Operation(summary = "이모지 전체 조회",
        description = "이모지를 전체 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "이모지 전체 조회 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = EmojiResponse.class))
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "이모지 전체 조회 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @GetMapping("/comments/reactions/emojis")
    public List<EmojiResponse> getEmojis() {
        return issueService.findAllEmoji();
    }
}

