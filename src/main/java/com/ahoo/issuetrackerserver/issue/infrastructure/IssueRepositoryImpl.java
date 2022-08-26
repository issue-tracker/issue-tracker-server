package com.ahoo.issuetrackerserver.issue.infrastructure;

import static com.ahoo.issuetrackerserver.issue.domain.QComment.comment;
import static com.ahoo.issuetrackerserver.issue.domain.QIssue.issue;
import static com.ahoo.issuetrackerserver.issue.domain.QIssueAssignee.issueAssignee;
import static com.ahoo.issuetrackerserver.issue.domain.QIssueLabel.issueLabel;
import static com.ahoo.issuetrackerserver.member.domain.QMember.member;
import static com.ahoo.issuetrackerserver.milestone.domain.QMilestone.milestone;

import com.ahoo.issuetrackerserver.issue.domain.Issue;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueSearchFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class IssueRepositoryImpl implements IssueRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Issue> findAllByIsClosedTrueAndFilter(Pageable pageable, IssueSearchFilter issueSearchFilter) {
        List<Issue> content = jpaQueryFactory.selectDistinct(issue)
            .from(issue)
            .join(issue.author, member)
            .leftJoin(issue.milestone, milestone)
            .leftJoin(issue.comments, comment)
            .leftJoin(issue.assignees, issueAssignee)
            .leftJoin(issue.labels, issueLabel)
            .where(
                issue.isClosed.isTrue(),
                assigneeNicknamesFilter(issueSearchFilter.getAssigneeNicknames()),
                labelTitlesFilter(issueSearchFilter.getLabelTitles()),
                milestoneTitleLikeFilter(issueSearchFilter.getMilestoneTitle()),
                authorNicknameLikeFilter(issueSearchFilter.getAuthorNickname()),
                issueTitleLikeFilter(issueSearchFilter.getIssueTitle())
            )
            .offset(pageable.getOffset())
            .limit(10)
            .fetch();

        Long count = jpaQueryFactory.select(issue.countDistinct())
            .from(issue)
            .join(issue.author, member)
            .leftJoin(issue.milestone, milestone)
            .leftJoin(issue.comments, comment)
            .leftJoin(issue.assignees, issueAssignee)
            .leftJoin(issue.labels, issueLabel)
            .where(
                issue.isClosed.isTrue(),
                assigneeNicknamesFilter(issueSearchFilter.getAssigneeNicknames()),
                labelTitlesFilter(issueSearchFilter.getLabelTitles()),
                milestoneTitleLikeFilter(issueSearchFilter.getMilestoneTitle()),
                authorNicknameLikeFilter(issueSearchFilter.getAuthorNickname()),
                issueTitleLikeFilter(issueSearchFilter.getIssueTitle())
            )
            .offset(pageable.getOffset())
            .limit(10)
            .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<Issue> findAllByIsClosedFalseAndFilter(Pageable pageable, IssueSearchFilter issueSearchFilter) {
        List<Issue> content = jpaQueryFactory.selectDistinct(issue)
            .from(issue)
            .join(issue.author, member)
            .leftJoin(issue.milestone, milestone)
            .leftJoin(issue.comments, comment)
            .leftJoin(issue.assignees, issueAssignee)
            .leftJoin(issue.labels, issueLabel)
            .where(
                issue.isClosed.isFalse(),
                assigneeNicknamesFilter(issueSearchFilter.getAssigneeNicknames()),
                labelTitlesFilter(issueSearchFilter.getLabelTitles()),
                milestoneTitleLikeFilter(issueSearchFilter.getMilestoneTitle()),
                authorNicknameLikeFilter(issueSearchFilter.getAuthorNickname()),
                issueTitleLikeFilter(issueSearchFilter.getIssueTitle())
            )
            .offset(pageable.getOffset())
            .limit(10)
            .fetch();

        Long count = jpaQueryFactory.select(issue.countDistinct())
            .from(issue)
            .join(issue.author, member)
            .leftJoin(issue.milestone, milestone)
            .leftJoin(issue.comments, comment)
            .leftJoin(issue.assignees, issueAssignee)
            .leftJoin(issue.labels, issueLabel)
            .where(
                issue.isClosed.isFalse(),
                assigneeNicknamesFilter(issueSearchFilter.getAssigneeNicknames()),
                labelTitlesFilter(issueSearchFilter.getLabelTitles()),
                milestoneTitleLikeFilter(issueSearchFilter.getMilestoneTitle()),
                authorNicknameLikeFilter(issueSearchFilter.getAuthorNickname()),
                issueTitleLikeFilter(issueSearchFilter.getIssueTitle())
            )
            .offset(pageable.getOffset())
            .limit(10)
            .fetchOne();
        return new PageImpl<>(content, pageable, count);
    }

    private BooleanBuilder assigneeNicknamesFilter(List<String> assigneeNicknames) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String assigneeNickname : assigneeNicknames) {
            booleanBuilder.or(issueAssignee.assignee.nickname.eq(assigneeNickname));
        }
        return booleanBuilder;
    }

    private BooleanBuilder labelTitlesFilter(List<String> labelTitles) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (String labelTitle : labelTitles) {
            booleanBuilder.or(issueLabel.label.title.eq(labelTitle));
        }
        return booleanBuilder;
    }

    private BooleanExpression issueTitleLikeFilter(String issueTitle) {
        if (issueTitle == null) {
            return null;
        }
        return issue.title.contains(issueTitle);
    }

    private BooleanExpression authorNicknameLikeFilter(String authorNickname) {
        if (authorNickname == null) {
            return null;
        }
        return issue.author.nickname.eq(authorNickname);
    }

    private BooleanExpression milestoneTitleLikeFilter(String milestoneTitle) {
        if (milestoneTitle == null) {
            return null;
        }
        return issue.milestone.title.eq(milestoneTitle);
    }
}
