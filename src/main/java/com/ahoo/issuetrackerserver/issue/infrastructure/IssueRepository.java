package com.ahoo.issuetrackerserver.issue.infrastructure;

import com.ahoo.issuetrackerserver.issue.domain.Issue;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("select distinct i "
        + "from Issue i left join fetch i.comments "
        + "where i.id = :id")
    Optional<Issue> findByIdFetchJoin(@Param("id") Long id);
}
