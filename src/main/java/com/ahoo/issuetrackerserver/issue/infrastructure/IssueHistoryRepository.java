package com.ahoo.issuetrackerserver.issue.infrastructure;

import com.ahoo.issuetrackerserver.issue.domain.IssueHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueHistoryRepository extends JpaRepository<IssueHistory, Long> {

    void deleteAllByIssueId(Long issueId);

}
