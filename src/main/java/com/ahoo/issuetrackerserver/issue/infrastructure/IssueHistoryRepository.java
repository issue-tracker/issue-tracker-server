package com.ahoo.issuetrackerserver.issue.infrastructure;

import com.ahoo.issuetrackerserver.issue.domain.IssueHistory;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueHistoryResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssueHistoryRepository extends JpaRepository<IssueHistory, Long> {

    void deleteAllByIssueId(Long issueId);

    @Query(
        "select distinct new com.ahoo.issuetrackerserver.issue.presentation.dto.IssueHistoryResponse(ih.modifier, ih.createdAt, ih.action, l, m, assignee, ih.previousTitle, ih.changedTitle) "
            + "from IssueHistory ih "
            + "left outer join Label l on ih.label.id = l.id "
            + "left outer join Milestone m on ih.milestone.id = m.id "
            + "left outer join IssueAssignee ia on ih.assignee.id = ia.assignee.id "
            + "left outer join Member assignee on ia.assignee.id = assignee.id "
            + "where ih.issue.id = :id")
    List<IssueHistoryResponse> findByIssueId(@Param("id") Long id);
}
