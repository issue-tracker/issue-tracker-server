package com.ahoo.issuetrackerserver.issue.infrastructure;

import static com.ahoo.issuetrackerserver.issue.domain.QComment.comment;
import static com.ahoo.issuetrackerserver.issue.domain.QIssue.issue;
import static com.ahoo.issuetrackerserver.issue.domain.QIssueAssignee.issueAssignee;
import static com.ahoo.issuetrackerserver.issue.domain.QIssueHistory.issueHistory;
import static com.ahoo.issuetrackerserver.issue.domain.QIssueLabel.issueLabel;
import static com.ahoo.issuetrackerserver.member.domain.QMember.member;
import static com.ahoo.issuetrackerserver.milestone.domain.QMilestone.milestone;

import com.ahoo.issuetrackerserver.issue.domain.Issue;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueSearchFilter;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class IssueRepositoryImpl implements IssueRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    public static final int PAGE_SIZE = 10;

    @Override
    public Page<Issue> findAllByFilter(Pageable pageable, IssueSearchFilter issueSearchFilter) {
        List<Issue> content = jpaQueryFactory.selectDistinct(issue)
            .from(issue)
            .join(issue.author, member)
            .leftJoin(issue.milestone, milestone)
            .leftJoin(issue.comments, comment)
            .leftJoin(issue.assignees, issueAssignee)
            .leftJoin(issue.labels, issueLabel)
            .leftJoin(issue.logs, issueHistory).fetchJoin()
            .where(
                isEqual(issue.isClosed, issueSearchFilter.getIsClosed()),
                isEqual(issue.milestone.title, issueSearchFilter.getMilestoneTitle()),
                isEqual(issue.author.nickname, issueSearchFilter.getAuthorNickname()),
                isEqualAny(issueAssignee.assignee.nickname, issueSearchFilter.getAssigneeNicknames()),
                isEqualAny(issueLabel.label.title, issueSearchFilter.getLabelTitles()),
                isContains(issue.title, issueSearchFilter.getIssueTitle())
            )
            .offset(pageable.getOffset())
            .limit(PAGE_SIZE)
            .fetch();

        Long count = jpaQueryFactory.select(issue.countDistinct())
            .from(issue)
            .join(issue.author, member)
            .leftJoin(issue.milestone, milestone)
            .leftJoin(issue.comments, comment)
            .leftJoin(issue.assignees, issueAssignee)
            .leftJoin(issue.labels, issueLabel)
            .where(
                isEqual(issue.isClosed, issueSearchFilter.getIsClosed()),
                isEqual(issue.milestone.title, issueSearchFilter.getMilestoneTitle()),
                isEqual(issue.author.nickname, issueSearchFilter.getAuthorNickname()),
                isEqualAny(issueAssignee.assignee.nickname, issueSearchFilter.getAssigneeNicknames()),
                isEqualAny(issueLabel.label.title, issueSearchFilter.getLabelTitles()),
                isContains(issue.title, issueSearchFilter.getIssueTitle())
            )
            .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Long countByFilterAndIsClosed(IssueSearchFilter issueSearchFilter, boolean isClosed) {
        return jpaQueryFactory.select(issue.countDistinct())
            .from(issue)
            .join(issue.author, member)
            .leftJoin(issue.milestone, milestone)
            .leftJoin(issue.comments, comment)
            .leftJoin(issue.assignees, issueAssignee)
            .leftJoin(issue.labels, issueLabel)
            .where(
                isEqual(issue.isClosed, isClosed),
                isEqual(issue.milestone.title, issueSearchFilter.getMilestoneTitle()),
                isEqual(issue.author.nickname, issueSearchFilter.getAuthorNickname()),
                isEqualAny(issueAssignee.assignee.nickname, issueSearchFilter.getAssigneeNicknames()),
                isEqualAny(issueLabel.label.title, issueSearchFilter.getLabelTitles()),
                isContains(issue.title, issueSearchFilter.getIssueTitle())
            )
            .fetchOne();
    }

    private <T> BooleanBuilder isEqualAny(SimpleExpression<T> left, List<T> rights) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        for (T right : rights) {
            booleanBuilder.or(left.eq(right));
        }
        return booleanBuilder;
    }

    private <T> BooleanExpression isEqual(SimpleExpression<T> left, T right) {
        if (right == null) {
            return null;
        }
        return left.eq(right);
    }

    private BooleanExpression isContains(StringPath left, String right) {
        if (right == null) {
            return null;
        }
        return left.contains(right);
    }
}
