package com.ahoo.issuetrackerserver.issue.domain;

import com.ahoo.issuetrackerserver.common.BaseEntity;
import com.ahoo.issuetrackerserver.label.domain.Label;
import com.ahoo.issuetrackerserver.member.domain.Member;
import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifier_id", nullable = false)
    private Member modifier;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IssueUpdateAction action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "label_id")
    private Label label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id")
    private Milestone milestone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private Member assignee;

    private String previousTitle;

    private String changedTitle;

    public static IssueHistory changeTitleType(Issue issue, Member modifier, String previousTitle,
        String changedTitle) {
        return new IssueHistory(null, issue, modifier, IssueUpdateAction.CHANGE_TITLE, null, null, null, previousTitle,
            changedTitle);
    }

    public static IssueHistory addAssigneeType(Issue issue, Member modifier, Member assignee) {
        return new IssueHistory(null, issue, modifier, IssueUpdateAction.ADD_ASSIGNEE, null, null, assignee, null,
            null);
    }

    public static IssueHistory deleteAssigneeType(Issue issue, Member modifier, Member assignee) {
        return new IssueHistory(null, issue, modifier, IssueUpdateAction.REMOVE_ASSIGNEE, null, null, assignee, null,
            null);
    }

    public static IssueHistory addLabelType(Issue issue, Member modifier, Label label) {
        return new IssueHistory(null, issue, modifier, IssueUpdateAction.ADD_LABEL, label, null, null, null, null);
    }

    public static IssueHistory deleteLabelType(Issue issue, Member modifier, Label label) {
        return new IssueHistory(null, issue, modifier, IssueUpdateAction.REMOVE_LABEL, label, null, null, null, null);
    }

    public static IssueHistory addMilestoneType(Issue issue, Member modifier, Milestone milestone) {
        return new IssueHistory(null, issue, modifier, IssueUpdateAction.ADD_MILESTONE, null, milestone, null, null,
            null);
    }

    public static IssueHistory deleteMilestoneType(Issue issue, Member modifier, Milestone milestone) {
        return new IssueHistory(null, issue, modifier, IssueUpdateAction.REMOVE_MILESTONE, null, milestone, null, null,
            null);
    }

    public static IssueHistory openIssueType(Issue issue, Member modifier) {
        return new IssueHistory(null, issue, modifier, IssueUpdateAction.OPEN_ISSUE, null, null, null, null, null);
    }

    public static IssueHistory closeIssueType(Issue issue, Member modifier) {
        return new IssueHistory(null, issue, modifier, IssueUpdateAction.CLOSE_ISSUE, null, null, null, null, null);
    }
}
