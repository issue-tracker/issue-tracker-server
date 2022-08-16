package com.ahoo.issuetrackerserver.milestone.presentation.dto;

import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "마일스톤 목록 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MilestonesResponse {

	@Schema(description = "열린 마일스톤")
	List<MilestoneResponse> openedMilestones;

	@Schema(description = "닫힌 마일스톤")
	List<MilestoneResponse> closedMilestones;

	public static MilestonesResponse from(List<Milestone> milestones) {
		List<MilestoneResponse> openedMilestoneResponses = milestones.stream()
			.filter(m -> !m.isClosed())
			.map(MilestoneResponse::from)
			.collect(Collectors.toList());
		List<MilestoneResponse> closedMilestoneResponses = milestones.stream()
			.filter(Milestone::isClosed)
			.map(MilestoneResponse::from)
			.collect(Collectors.toList());
		return new MilestonesResponse(openedMilestoneResponses, closedMilestoneResponses);
	}
}
