package com.ahoo.issuetrackerserver.milestone.application;

import com.ahoo.issuetrackerserver.common.exception.ApplicationException;
import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import com.ahoo.issuetrackerserver.issue.domain.Issue;
import com.ahoo.issuetrackerserver.issue.infrastructure.IssueRepository;
import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import com.ahoo.issuetrackerserver.milestone.infrastructure.MilestoneRepository;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneCreateRequest;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneResponse;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestoneUpdateRequest;
import com.ahoo.issuetrackerserver.milestone.presentation.dto.MilestonesResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final IssueRepository issueRepository;

    @Transactional(readOnly = true)
    public MilestoneResponse findOne(Long id) {
        Milestone milestone = milestoneRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MILESTONE, new NoSuchElementException()));
        return MilestoneResponse.from(milestone);
    }

    @Transactional(readOnly = true)
    public MilestonesResponse findAll() {
        List<Milestone> milestones = milestoneRepository.findAllFetchJoin();
        return MilestonesResponse.from(milestones);
    }

    @Transactional
    public MilestoneResponse save(MilestoneCreateRequest milestoneCreateRequest) {
        if (milestoneRepository.existsByTitle(milestoneCreateRequest.getTitle())) {
            throw new ApplicationException(ErrorType.DUPLICATED_MILESTONE_TITLE);
        }

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
        Milestone milestone = milestoneRepository.findByIdFetchJoin(id)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MILESTONE, new NoSuchElementException()));

        if (milestone.hasIssue()) {
            List<Long> ids = milestone.getIssues().stream()
                .map(Issue::getId)
                .collect(Collectors.toList());
            issueRepository.removeMilestoneByIds(ids, LocalDateTime.now());
        }

        milestoneRepository.delete(milestone);
    }
}
