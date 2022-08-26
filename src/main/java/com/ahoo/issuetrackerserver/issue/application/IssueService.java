package com.ahoo.issuetrackerserver.issue.application;

import com.ahoo.issuetrackerserver.common.exception.ApplicationException;
import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import com.ahoo.issuetrackerserver.issue.domain.Comment;
import com.ahoo.issuetrackerserver.issue.domain.Emoji;
import com.ahoo.issuetrackerserver.issue.domain.Issue;
import com.ahoo.issuetrackerserver.issue.domain.IssueAssignee;
import com.ahoo.issuetrackerserver.issue.domain.IssueLabel;
import com.ahoo.issuetrackerserver.issue.domain.Reaction;
import com.ahoo.issuetrackerserver.issue.infrastructure.CommentRepository;
import com.ahoo.issuetrackerserver.issue.infrastructure.IssueRepository;
import com.ahoo.issuetrackerserver.issue.infrastructure.ReactionRepository;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueCreateRequest;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueResponse;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueSearchFilter;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssuesResponse;
import com.ahoo.issuetrackerserver.label.domain.Label;
import com.ahoo.issuetrackerserver.label.infrastructure.LabelRepository;
import com.ahoo.issuetrackerserver.member.domain.Member;
import com.ahoo.issuetrackerserver.member.infrastructure.MemberRepository;
import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import com.ahoo.issuetrackerserver.milestone.infrastructure.MilestoneRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final MemberRepository memberRepository;
    private final LabelRepository labelRepository;
    private final MilestoneRepository milestoneRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;

    private final int PAGE_SIZE = 5;

    @Transactional
    public IssueResponse save(Long memberId, IssueCreateRequest issueCreateRequest) {
        Member author = memberRepository.findById(memberId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MEMBER, new NoSuchElementException()));
        Milestone milestone = milestoneRepository.findById(issueCreateRequest.getMilestoneId())
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MILESTONE, new NoSuchElementException()));

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
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        return IssueResponse.from(findIssue);
    }

    @Transactional
    public void updateStatus(boolean status, List<Long> ids) {
        issueRepository.updateStatus(status, ids);
    }

    @Transactional
    public IssueResponse updateTitle(Long id, String title) {
        Issue issue = issueRepository.findByIdFetchJoinComments(id)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        issue.changeTitle(title);

        return IssueResponse.from(issue);
    }

    @Transactional
    public IssueResponse addAssignee(Long issueId, Long assigneeId) {
        Issue issue = issueRepository.findByIdFetchJoinComments(issueId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        Member assignee = memberRepository.findById(assigneeId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MEMBER, new NoSuchElementException()));

        IssueAssignee newAssignee = IssueAssignee.of(issue, assignee);
        issue.addAssignee(newAssignee);

        return IssueResponse.from(issue);
    }

    @Transactional
    public void deleteAssignee(Long issueId, boolean clear, Long assigneeId) {
        Issue issue = issueRepository.findByIdFetchJoinAssignees(issueId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        if (clear) {
            issue.clearAssignees();
        } else {
            issue.removeAssignee(assigneeId);
        }
    }

    @Transactional
    public IssueResponse addLabel(Long issueId, Long labelId) {
        Issue issue = issueRepository.findByIdFetchJoinComments(issueId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        Label label = labelRepository.findById(labelId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_LABEL, new NoSuchElementException()));

        IssueLabel newLabel = IssueLabel.of(issue, label);
        issue.addLabel(newLabel);

        return IssueResponse.from(issue);
    }

    @Transactional
    public void deleteLabel(Long issueId, Long labelId) {
        Issue issue = issueRepository.findByIdFetchJoinLabels(issueId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        issue.removeLabel(labelId);
    }

    @Transactional
    public IssueResponse addMilestone(Long issueId, Long milestoneId) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MILESTONE, new NoSuchElementException()));

        Issue issue = issueRepository.findByIdFetchJoinComments(issueId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        issue.updateMilestone(milestone);

        return IssueResponse.from(issue);
    }

    @Transactional
    public void deleteMilestone(Long id) {
        Issue issue = issueRepository.findByIdFetchJoinMilestone(id)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        issue.clearMilestone();
    }

    @Transactional
    public void deleteIssue(Long id) {
        Issue issue = issueRepository.findByIdFetchJoinMilestone(id)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        if (issue.getMilestone() != null) {
            issue.getMilestone().removeIssue(issue);
        }

        issueRepository.delete(issue);
    }

    @Transactional
    public IssueResponse addComment(Long memberId, Long issueId, String content) {
        Member author = memberRepository.findById(memberId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MEMBER, new NoSuchElementException()));

        Issue issue = issueRepository.findByIdFetchJoinComments(issueId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        Comment newComment = Comment.of(author, content, issue);
        commentRepository.save(newComment);
        issue.addComment(newComment);

        return IssueResponse.from(issue);
    }

    @Transactional
    public IssueResponse updateComment(Long memberId, Long issueId, Long commentId, String content) {
        Issue issue = issueRepository.findByIdFetchJoinComments(issueId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        issue.updateComment(memberId, commentId, content);

        return IssueResponse.from(issue);
    }

    @Transactional
    public void deleteComment(Long memberId, Long issueId, Long commentId) {
        Issue issue = issueRepository.findByIdFetchJoinComments(issueId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        issue.deleteComment(memberId, commentId);
    }

    @Transactional
    public IssueResponse addReaction(Long memberId, Long issueId, Long commentId, String emojiName) {
        Emoji emoji = Emoji.valueOf(emojiName);

        Member reactor = memberRepository.findById(memberId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_MEMBER, new NoSuchElementException()));

        Issue issue = issueRepository.findByIdFetchJoinComments(issueId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_ISSUE, new NoSuchElementException()));

        Comment comment = issue.getComments().stream()
            .filter(c -> Objects.equals(c.getId(), commentId))
            .findFirst()
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_COMMENT, new NoSuchElementException()));

        comment.validateDuplicateReaction(emoji, reactor);

        Reaction reaction = Reaction.of(comment, emoji, reactor);
        reactionRepository.save(reaction);

        comment.addReaction(reaction);

        return IssueResponse.from(issue);
    }

    @Transactional
    public void deleteReaction(Long memberId, Long reactionId) {
        Reaction reaction = reactionRepository.findById(reactionId)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_REACTION, new NoSuchElementException()));

        reaction.validateReactor(memberId);

        reactionRepository.delete(reaction);
    }

    @Transactional(readOnly = true)
    public IssuesResponse findAll(int page, IssueSearchFilter issueSearchFilter) {
        Page<IssueResponse> openIssues = issueRepository.findAllByIsClosedFalseAndFilter(
                PageRequest.of(page, PAGE_SIZE), issueSearchFilter)
            .map(IssueResponse::from);

        Page<IssueResponse> closedIssues = issueRepository.findAllByIsClosedTrueAndFilter(
                PageRequest.of(page, PAGE_SIZE), issueSearchFilter)
            .map(IssueResponse::from);

        return IssuesResponse.of(openIssues, closedIssues);
    }
}
