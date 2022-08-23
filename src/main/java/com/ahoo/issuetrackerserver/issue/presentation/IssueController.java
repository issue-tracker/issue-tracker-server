package com.ahoo.issuetrackerserver.issue.presentation;

import com.ahoo.issuetrackerserver.common.argumentresolver.SignInMemberId;
import com.ahoo.issuetrackerserver.common.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.issue.application.IssueService;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueCommentRequest;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueCreateRequest;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueResponse;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueStatusUpdateRequest;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueTitleUpdateRequest;
import com.ahoo.issuetrackerserver.label.application.LabelService;
import com.ahoo.issuetrackerserver.milestone.application.MilestoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final LabelService labelService;
    private final MilestoneService milestoneService;

    //TODO
    // 1. 이슈 전체 목록
    // 2. 이슈 수정
    // 3. 이슈 삭제

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
    public void changeIssueStatus(@Valid @RequestBody IssueStatusUpdateRequest issueStatusUpdateRequest) {
        issueService.updateStatus(issueStatusUpdateRequest.getStatus(), issueStatusUpdateRequest.getIds());
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
        @PathVariable Long id,
        @Valid @RequestBody IssueTitleUpdateRequest issueTitleUpdateRequest) {
        return issueService.updateTitle(id, issueTitleUpdateRequest.getTitle());
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
        @PathVariable("issueId") Long issueId,
        @PathVariable("assigneeId") Long assigneeId) {
        return issueService.addAssignee(issueId, assigneeId);
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
        @PathVariable Long issueId,
        @RequestParam(required = false, defaultValue = "false") boolean clear,
        @RequestParam(required = false) Long assigneeId
    ) {
        issueService.deleteAssignee(issueId, clear, assigneeId);
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
        @PathVariable("issueId") Long issueId,
        @PathVariable("labelId") Long labelId) {
        return issueService.addLabel(issueId, labelId);
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
    public void deleteAssignees(
        @PathVariable("issueId") Long issueId,
        @PathVariable("labelId") Long labelId
    ) {
        issueService.deleteLabel(issueId, labelId);
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
    @PatchMapping("/{issueId}/milestones/{milestoneId}")
    public IssueResponse addMilestone(
        @PathVariable("issueId") Long issueId,
        @PathVariable(name = "milestoneId", required = true) Long milestoneId) {
        return issueService.addMilestone(issueId, milestoneId);
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
    @DeleteMapping("/{id}/milestones")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMilestone(@PathVariable Long id) {
        issueService.deleteMilestone(id);
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
    public void deleteIssue(@PathVariable Long id) {
        issueService.deleteIssue(id);
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
        @RequestBody IssueCommentRequest issueCommentRequest) {
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
        @RequestBody IssueCommentRequest issueCommentRequest) {
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
}
