package com.ahoo.issuetrackerserver.milestone.domain;

import com.ahoo.issuetrackerserver.common.BaseEntity;
import com.ahoo.issuetrackerserver.issue.domain.Issue;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@EqualsAndHashCode(exclude = {"title", "dueDate", "isClosed", "issues"}, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Milestone extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "milestone_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;
    private LocalDate dueDate;

    @Column(nullable = false)
    private boolean isClosed;

    @OneToMany(mappedBy = "milestone")
    private List<Issue> issues = new ArrayList<>();

    public static Milestone of(String title, String description, LocalDate dueDate) {
        return new Milestone(
            null,
            title,
            description,
            dueDate,
            false,
            new ArrayList<>()
        );
    }

    public void update(String title, String description, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public void toggleStatus() {
        this.isClosed = !this.isClosed;
    }

    public void addIssue(Issue issue) {
        this.issues.add(issue);
    }

    public void removeIssue(Issue issue) {
        this.issues.remove(issue);
    }
}
