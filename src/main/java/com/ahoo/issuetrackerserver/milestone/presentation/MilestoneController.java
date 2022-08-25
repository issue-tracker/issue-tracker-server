package com.ahoo.issuetrackerserver.milestone.presentation;

import com.ahoo.issuetrackerserver.common.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.milestone.application.MilestoneService;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneCreateRequest;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneResponse;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneUpdateRequest;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestonesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "milestones", description = "마일스톤 API")
@RestController
@RequestMapping("/api/milestones")
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneService milestoneService;

    @Operation(summary = "마일스톤 단건 조회",
        description = "마일스톤 id로 단건의 마일스톤을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "마일스톤 조회 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = MilestoneResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "마일스톤 조회 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @GetMapping("/{id}")
    public MilestoneResponse milestone(@PathVariable Long id) {
        return milestoneService.findOne(id);
    }

    @Operation(summary = "마일스톤 다건 조회",
        description = "조건에 맞는 마일스톤의 전체 목록을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "마일스톤 다건 조회 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = MilestonesResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "마일스톤 다건 조회 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @GetMapping
    public MilestonesResponse milestones() {
        return milestoneService.findAll();
    }

    @Operation(summary = "마일스톤 등록",
        description = "새로운 마일스톤을 등록합니다.",
        responses = {
            @ApiResponse(responseCode = "201",
                description = "마일스톤 등록 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = MilestoneResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "마일스톤 등록 실패",
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
    public MilestoneResponse addMilestone(@Valid @RequestBody MilestoneCreateRequest milestoneCreateRequest) {
        return milestoneService.save(milestoneCreateRequest);
    }

    @Operation(summary = "마일스톤 수정",
        description = "마일스톤을 수정합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "마일스톤 수정 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = MilestoneResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "마일스톤 수정 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PatchMapping("/{id}")
    public MilestoneResponse updateMilestone(
        @PathVariable Long id,
        @Valid @RequestBody MilestoneUpdateRequest milestoneUpdateRequest) {
        return milestoneService.update(id, milestoneUpdateRequest);
    }

    @Operation(summary = "마일스톤 상태 변경(toggle)",
        description = "마일스톤의 상태를 변경(toggle)합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "마일스톤 상태 변경(toggle) 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = MilestoneResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "마일스톤 상태 변경(toggle) 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PatchMapping("/{id}/status")
    public MilestoneResponse toggleMilestoneStatus(@PathVariable Long id) {
        return milestoneService.toggleStatus(id);
    }

    @Operation(summary = "마일스톤 삭제",
        description = "마일스톤을 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "마일스톤 삭제 성공"),
            @ApiResponse(responseCode = "400",
                description = "마일스톤 삭제 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        milestoneService.delete(id);
        return ResponseEntity.ok().build();
    }
}
