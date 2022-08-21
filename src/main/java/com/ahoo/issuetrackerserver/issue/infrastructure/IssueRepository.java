package com.ahoo.issuetrackerserver.issue.infrastructure;

import com.ahoo.issuetrackerserver.issue.domain.Issue;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("select distinct i "
        + "from Issue i left join fetch i.comments "
        + "where i.id = :id")
    Optional<Issue> findByIdFetchJoin(@Param("id") Long id);

    @Modifying
    @Query("update Issue i "
        + "set i.isClosed = :status "
        + "where i.id in :ids")
    void updateStatus(@Param("status") boolean status, @Param("ids") List<Long> ids);
}
