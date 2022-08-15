package com.ahoo.issuetrackerserver.milestone.application;

import com.ahoo.issuetrackerserver.common.exception.ErrorMessage;
import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import com.ahoo.issuetrackerserver.milestone.infrastructure.MilestoneRepository;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.AddMilestoneRequest;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneResponse;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestonesResponse;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.UpdateMilestoneRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

	public MilestonesResponse findAll(Boolean isClosed) {
		List<Milestone> milestones;
		if (isClosed == null) {
			milestones = milestoneRepository.findAll();
		} else {
			milestones = milestoneRepository.findAllByIsClosed(isClosed);
		}
		return MilestonesResponse.from(milestones);
	}

	public MilestoneResponse save(AddMilestoneRequest addMilestoneRequest) {
		Milestone newMilestone = addMilestoneRequest.toEntity();
		milestoneRepository.save(newMilestone);
		return MilestoneResponse.from(newMilestone);
	}

	public MilestoneResponse update(Long id, UpdateMilestoneRequest updateMilestoneRequest) {
		Milestone milestone = milestoneRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_MILESTONE));
		milestone.update(
			updateMilestoneRequest.getTitle(),
			updateMilestoneRequest.getDescription(),
			LocalDate.parse(updateMilestoneRequest.getDueDate(), DateTimeFormatter.ISO_LOCAL_DATE)
		);
		return MilestoneResponse.from(milestone);
	}
}
