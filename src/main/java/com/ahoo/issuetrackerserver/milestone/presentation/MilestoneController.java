package com.ahoo.issuetrackerserver.milestone.presentation;

import com.ahoo.issuetrackerserver.common.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.milestone.application.MilestoneService;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneResponse;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestonesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	@ResponseStatus(HttpStatus.OK)
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
	@ResponseStatus(HttpStatus.OK)
	public MilestonesResponse milestones(@RequestParam(required = false) Boolean isClosed) {
		return milestoneService.findAll(isClosed);
	}
}
