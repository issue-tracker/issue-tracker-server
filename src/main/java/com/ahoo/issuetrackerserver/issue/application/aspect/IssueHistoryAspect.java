package com.ahoo.issuetrackerserver.issue.application.aspect;

import com.ahoo.issuetrackerserver.common.exception.ApplicationException;
import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import com.ahoo.issuetrackerserver.issue.domain.Issue;
import com.ahoo.issuetrackerserver.issue.domain.IssueHistory;
import com.ahoo.issuetrackerserver.issue.infrastructure.IssueHistoryRepository;
import com.ahoo.issuetrackerserver.issue.infrastructure.IssueRepository;
import com.ahoo.issuetrackerserver.label.domain.Label;
import com.ahoo.issuetrackerserver.label.infrastructure.LabelRepository;
import com.ahoo.issuetrackerserver.member.domain.Member;
import com.ahoo.issuetrackerserver.member.infrastructure.MemberRepository;
import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import com.ahoo.issuetrackerserver.milestone.infrastructure.MilestoneRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class IssueHistoryAspect {

    private final IssueHistoryRepository issueHistoryRepository;
    private final IssueRepository issueRepository;
    private final MemberRepository memberRepository;
    private final LabelRepository labelRepository;
    private final MilestoneRepository milestoneRepository;

    @Around("@annotation(com.ahoo.issuetrackerserver.issue.application.aspect.IssueHistoryLogging)")
    public void createHistory(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            if (joinPoint.getSignature().getName().contains("updateStatus")) {
                joinPoint.proceed();
                Object[] args = joinPoint.getArgs();
                List<Long> ids = (List<Long>) args[1];
                Long modifiedId = (Long) args[2];
                Member modifier = memberRepository.findById(modifiedId)
                    .orElseThrow(
                        () -> new ApplicationException(ErrorType.NOT_EXISTS_MEMBER, new NoSuchElementException()));
                List<IssueHistory> issueHistories = issueRepository.findAllById(ids).stream()
                    .map(issue -> issue.isClosed() ?
                        IssueHistory.closeIssueType(issue, modifier) :
                        IssueHistory.openIssueType(issue, modifier))
                    .collect(Collectors.toList());
                issueHistoryRepository.saveAll(issueHistories);
                return;
            }

            Object[] args = joinPoint.getArgs();
            Long issueId = (Long) args[0];
            Long modifiedId = (Long) args[2];
            String previousTitle = null;

            if (joinPoint.getSignature().getName().equals("updateTitle")) {
                previousTitle = issueRepository.findById(issueId)
                    .map(Issue::getTitle)
                    .orElseThrow(
                        () -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));
            }

            joinPoint.proceed();

            Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));
            Member modifier = memberRepository.findById(modifiedId)
                .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MEMBER, new NoSuchElementException()));

            IssueHistory issueHistory = null;
            if (joinPoint.getSignature().getName().equals("updateTitle")) {
                issueHistory = IssueHistory.changeTitleType(issue, modifier, previousTitle);
            } else if (joinPoint.getSignature().getName().equals("addAssignee")) {
                Long assigneeId = (Long) args[1];
                Member assignee = memberRepository.findById(assigneeId)
                    .orElseThrow(
                        () -> new ApplicationException(ErrorType.NOT_EXISTS_MEMBER, new NoSuchElementException()));

                issueHistory = IssueHistory.addAssigneeType(issue, modifier, assignee);
            } else if (joinPoint.getSignature().getName().equals("deleteAssignee")) {
                Long assigneeId = (Long) args[2];
                Member removedAssignee = memberRepository.findById(assigneeId)
                    .orElseThrow(
                        () -> new ApplicationException(ErrorType.NOT_EXISTS_MEMBER, new NoSuchElementException()));

                issueHistory = IssueHistory.deleteAssigneeType(issue, modifier, removedAssignee);
            } else if (joinPoint.getSignature().getName().equals("addLabel")) {
                Long labelId = (Long) args[1];
                Label label = labelRepository.findById(labelId)
                    .orElseThrow(
                        () -> new ApplicationException(ErrorType.NOT_EXISTS_LABEL, new NoSuchElementException()));

                issueHistory = IssueHistory.addLabelType(issue, modifier, label);
            } else if (joinPoint.getSignature().getName().equals("deleteLabel")) {
                Long labelId = (Long) args[1];
                Label removedLabel = labelRepository.findById(labelId)
                    .orElseThrow(
                        () -> new ApplicationException(ErrorType.NOT_EXISTS_LABEL, new NoSuchElementException()));

                issueHistory = IssueHistory.deleteLabelType(issue, modifier, removedLabel);
            } else if (joinPoint.getSignature().getName().equals("addMilestone")) {
                Long milestoneId = (Long) args[1];
                Milestone milestone = milestoneRepository.findById(milestoneId)
                    .orElseThrow(
                        () -> new ApplicationException(ErrorType.NOT_EXISTS_MILESTONE, new NoSuchElementException()));

                issueHistory = IssueHistory.addMilestoneType(issue, modifier, milestone);
            } else if (joinPoint.getSignature().getName().equals("deleteMilestone")) {
                Long milestoneId = (Long) args[1];
                Milestone removedMilestone = milestoneRepository.findById(milestoneId)
                    .orElseThrow(
                        () -> new ApplicationException(ErrorType.NOT_EXISTS_MILESTONE, new NoSuchElementException()));

                issueHistory = IssueHistory.deleteMilestoneType(issue, modifier, removedMilestone);
            }
            issueHistoryRepository.save(issueHistory);
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }
}
