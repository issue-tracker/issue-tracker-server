package com.ahoo.issuetrackerserver.issue.presentation;

import com.ahoo.issuetrackerserver.common.argumentresolver.SignInMemberId;
import com.ahoo.issuetrackerserver.common.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.issue.application.IssueService;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueCreateRequest;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    // 4. 이슈 상태 변경

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
}
