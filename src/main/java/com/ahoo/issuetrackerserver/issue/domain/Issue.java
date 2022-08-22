package com.ahoo.issuetrackerserver.issue.domain;

import com.ahoo.issuetrackerserver.common.BaseEntity;
import com.ahoo.issuetrackerserver.common.exception.ErrorMessage;
import com.ahoo.issuetrackerserver.member.domain.Member;
import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Issue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<IssueAssignee> assignees = new ArrayList<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<IssueLabel> labels = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id")
    private Milestone milestone;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.PERSIST)
    private List<Comment> comments = new ArrayList<>();

    private boolean isClosed;

    public static Issue of(String title, Member author, Milestone milestone) {
        return new Issue(null, title, author, new ArrayList<>(), new ArrayList<>(), milestone, new ArrayList<>(),
            false);
    }

    public void addAssignee(IssueAssignee assignee) {
        this.assignees.add(assignee);
    }

    public void addAssignees(List<IssueAssignee> assignees) {
        this.assignees.addAll(assignees);
    }

    public void clearAssignees() {
        this.assignees.clear();
    }

    public void removeAssignee(Long assigneeId) {
        IssueAssignee issueAssignee = this.assignees.stream()
            .filter(a -> Objects.equals(a.getAssignee().getId(), assigneeId))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_MEMBER));
        this.assignees.remove(issueAssignee);
    }

    public void addLabel(IssueLabel label) {
        this.labels.add(label);
    }

    public void addLabels(List<IssueLabel> labels) {
        this.labels.addAll(labels);
    }

    public void removeLabel(Long labelId) {
        IssueLabel issueLabel = this.labels.stream()
            .filter(l -> Objects.equals(l.getLabel().getId(), labelId))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_EXISTS_LABEL));
        this.labels.remove(issueLabel);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void changeTitle(String title) {
        this.title = title;
    }
}
