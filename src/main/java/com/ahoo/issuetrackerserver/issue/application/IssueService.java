package com.ahoo.issuetrackerserver.issue.application;

import com.ahoo.issuetrackerserver.common.exception.ErrorMessage;
import com.ahoo.issuetrackerserver.issue.domain.Comment;
import com.ahoo.issuetrackerserver.issue.domain.Issue;
import com.ahoo.issuetrackerserver.issue.domain.IssueAssignee;
import com.ahoo.issuetrackerserver.issue.domain.IssueLabel;
import com.ahoo.issuetrackerserver.issue.infrastructure.IssueRepository;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueCreateRequest;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final MemberRepository memberRepository;
    private final LabelRepository labelRepository;
    private final MilestoneRepository milestoneRepository;

    @Transactional
    public IssueResponse save(Long memberId, IssueCreateRequest issueCreateRequest) {
        Member author = memberRepository.findById(memberId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_MEMBER));
        Milestone milestone = milestoneRepository.findById(issueCreateRequest.getMilestoneId())
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_MILESTONE));

        Issue issue = Issue.of(issueCreateRequest.getTitle(), author, milestone);

        List<IssueAssignee> assignees = memberRepository.findAllById(issueCreateRequest.getAssigneeIds()).stream()
            .map(member -> IssueAssignee.of(issue, member))
            .collect(Collectors.toUnmodifiableList());

        List<IssueLabel> labels = labelRepository.findAllById(issueCreateRequest.getLabelIds()).stream()
            .map(label -> IssueLabel.of(issue, label))
            .collect(Collectors.toUnmodifiableList());

        Comment comment = Comment.of(author, issueCreateRequest.getComment(), issue);

        issue.addAssignees(assignees);
        issue.addLabels(labels);
        issue.addComment(comment);

        Issue savedIssue = issueRepository.save(issue);

        return IssueResponse.from(savedIssue);
    }

    @Transactional(readOnly = true)
    public IssueResponse findById(Long id) {
        Issue findIssue = issueRepository.findByIdFetchJoinComments(id)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_ISSUE));

        return IssueResponse.from(findIssue);
    }

    @Transactional
    public void updateStatus(boolean status, List<Long> ids) {
        issueRepository.updateStatus(status, ids);
    }

    @Transactional
    public IssueResponse updateTitle(Long id, String title) {
        Issue issue = issueRepository.findByIdFetchJoinComments(id)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_ISSUE));

        issue.changeTitle(title);

        return IssueResponse.from(issue);
    }

    @Transactional
    public IssueResponse addAssignee(Long issueId, Long assigneeId) {
        Issue issue = issueRepository.findByIdFetchJoinComments(issueId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_ISSUE));

        Member assignee = memberRepository.findById(assigneeId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_MEMBER));

        IssueAssignee newAssignee = IssueAssignee.of(issue, assignee);
        issue.addAssignee(newAssignee);

        return IssueResponse.from(issue);
    }

    @Transactional
    public void deleteAssignee(Long issueId, boolean clear, Long assigneeId) {
        Issue issue = issueRepository.findByIdFetchJoinAssignees(issueId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_ISSUE));

        if (clear) {
            issue.clearAssignees();
        } else {
            issue.removeAssignee(assigneeId);
        }
    }

    @Transactional
    public IssueResponse addLabel(Long issueId, Long labelId) {
        Issue issue = issueRepository.findByIdFetchJoinComments(issueId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_ISSUE));

        Label label = labelRepository.findById(labelId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_LABEL));

        IssueLabel newLabel = IssueLabel.of(issue, label);
        issue.addLabel(newLabel);

        return IssueResponse.from(issue);
    }

    @Transactional
    public void deleteLabel(Long issueId, Long labelId) {
        Issue issue = issueRepository.findByIdFetchJoinLabels(issueId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_ISSUE));
        
        issue.removeLabel(labelId);
    }

    @Transactional
    public IssueResponse addMilestone(Long issueId, Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_MILESTONE));

        Issue issue = issueRepository.findByIdFetchJoinComments(issueId)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_ISSUE));

        issue.updateMilestone(milestone);

        return IssueResponse.from(issue);
    }

    @Transactional
    public void deleteMilestone(Long id) {
        Issue issue = issueRepository.findByIdFetchJoinMilestone(id)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_ISSUE));

        issue.clearMilestone();
    }

    @Transactional
    public void deleteIssue(Long id) {
        Issue issue = issueRepository.findByIdFetchJoinMilestone(id)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_ISSUE));

        if (issue.getMilestone() != null) {
            issue.getMilestone().removeIssue(issue);
        }

        issueRepository.delete(issue);
    }
}
