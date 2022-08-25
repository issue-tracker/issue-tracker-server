package com.ahoo.issuetrackerserver.milestone.application;

import com.ahoo.issuetrackerserver.common.exception.ApplicationException;
import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import com.ahoo.issuetrackerserver.milestone.infrastructure.MilestoneRepository;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneCreateRequest;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneResponse;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneUpdateRequest;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestonesResponse;
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
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MILESTONE, new NoSuchElementException()));
        return MilestoneResponse.from(milestone);
    }

    @Transactional(readOnly = true)
    public MilestonesResponse findAll() {
        //TODO: 이슈 개발 후, 연관된 이슈 count(open, close) 로직 추가
        List<Milestone> milestones = milestoneRepository.findAll();
        return MilestonesResponse.from(milestones);
    }

    @Transactional
    public MilestoneResponse save(MilestoneCreateRequest milestoneCreateRequest) {
        Milestone newMilestone = milestoneCreateRequest.toEntity();
        milestoneRepository.save(newMilestone);
        return MilestoneResponse.from(newMilestone);
    }

    @Transactional
    public MilestoneResponse update(Long id, MilestoneUpdateRequest milestoneUpdateRequest) {
        Milestone milestone = milestoneRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MILESTONE, new NoSuchElementException()));
        milestone.update(
            milestoneUpdateRequest.getTitle(),
            milestoneUpdateRequest.getDescription(),
            milestoneUpdateRequest.getDueDate()
        );
        return MilestoneResponse.from(milestone);
    }

    @Transactional
    public MilestoneResponse toggleStatus(Long id) {
        Milestone milestone = milestoneRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MILESTONE, new NoSuchElementException()));
        milestone.toggleStatus();
        return MilestoneResponse.from(milestone);
    }

    @Transactional
    public void delete(Long id) {
        //TODO: 이슈 도메인 개발 시, 연관관계 끊어주는 로직 추가
        Milestone milestone = milestoneRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MILESTONE, new NoSuchElementException()));
        milestoneRepository.delete(milestone);
    }
}
