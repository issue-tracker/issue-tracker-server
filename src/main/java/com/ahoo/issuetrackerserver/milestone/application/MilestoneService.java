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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MilestoneService {

	private final MilestoneRepository milestoneRepository;

	@Transactional(readOnly = true)
	public MilestoneResponse findOne(Long id) {
		Milestone milestone = milestoneRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_MILESTONE));
		return MilestoneResponse.from(milestone);
	}

	@Transactional(readOnly = true)
	public MilestonesResponse findAll(Boolean isClosed) {
		//TODO: 이슈 개발 후, 연관된 이슈 count(open, close) 로직 추가
		List<Milestone> milestones;
		if (isClosed == null) {
			milestones = milestoneRepository.findAll();
		} else {
			milestones = milestoneRepository.findAllByIsClosed(isClosed);
		}
		return MilestonesResponse.from(milestones);
	}

	@Transactional
	public MilestoneResponse save(AddMilestoneRequest addMilestoneRequest) {
		Milestone newMilestone = addMilestoneRequest.toEntity();
		milestoneRepository.save(newMilestone);
		return MilestoneResponse.from(newMilestone);
	}

	@Transactional
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

	@Transactional
	public MilestoneResponse toggleStatus(Long id) {
		Milestone milestone = milestoneRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_MILESTONE));
		milestone.toggleStatus();
		return MilestoneResponse.from(milestone);
	}

	@Transactional
	public void delete(Long id) {
		//TODO: 이슈 도메인 개발 시, 연관관계 끊어주는 로직 추가
		Milestone milestone = milestoneRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_MILESTONE));
		milestoneRepository.delete(milestone);
	}
}
