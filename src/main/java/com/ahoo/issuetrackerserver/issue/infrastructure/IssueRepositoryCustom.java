package com.ahoo.issuetrackerserver.issue.infrastructure;

import com.ahoo.issuetrackerserver.issue.domain.Issue;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssueRepositoryCustom {

    Page<Issue> findAllByFilter(Pageable pageable, IssueSearchFilter filter);

    Long countByFilterAndIsClosed(IssueSearchFilter filter, boolean isClosed);
}
