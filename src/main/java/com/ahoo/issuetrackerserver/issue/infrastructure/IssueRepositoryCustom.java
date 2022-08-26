package com.ahoo.issuetrackerserver.issue.infrastructure;

import com.ahoo.issuetrackerserver.issue.domain.Issue;
import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssueRepositoryCustom {

    Page<Issue> findAllByIsClosedTrueAndFilter(Pageable pageable, IssueSearchFilter filter);

    Page<Issue> findAllByIsClosedFalseAndFilter(Pageable pageable, IssueSearchFilter filter);
}
