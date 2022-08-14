package com.ahoo.issuetrackerserver.milestone.application;

import com.ahoo.issuetrackerserver.common.exception.ErrorMessage;
import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import com.ahoo.issuetrackerserver.milestone.infrastructure.MilestoneRepository;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneResponse;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MilestoneService {

	private final MilestoneRepository milestoneRepository;

	public MilestoneResponse findOne(Long id) {
		Milestone milestone = milestoneRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_MILESTONE));
		return MilestoneResponse.from(milestone);
	}
}
