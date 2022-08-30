package com.ahoo.issuetrackerserver.issue.infrastructure;

import com.ahoo.issuetrackerserver.issue.domain.Issue;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssueRepository extends JpaRepository<Issue, Long>, IssueRepositoryCustom {

    @Query("select distinct i "
        + "from Issue i "
        + "join fetch i.author "
        + "left join fetch i.milestone "
        + "left join fetch i.comments "
        + "where i.id = :id")
    Optional<Issue> findByIdFetchJoinComments(@Param("id") Long id);

    @Query("select distinct i "
        + "from Issue i "
        + "join fetch i.author "
        + "left join fetch i.milestone "
        + "left join fetch i.assignees "
        + "where i.id = :id")
    Optional<Issue> findByIdFetchJoinAssignees(@Param("id") Long id);

    @Query("select distinct i "
        + "from Issue i "
        + "join fetch i.author "
        + "left join fetch i.milestone "
        + "left join fetch i.labels "
        + "where i.id = :id")
    Optional<Issue> findByIdFetchJoinLabels(@Param("id") Long id);

    @Query("select distinct i "
        + "from Issue i "
        + "left join fetch i.milestone "
        + "where i.id = :id")
    Optional<Issue> findByIdFetchJoinMilestone(@Param("id") Long id);

    @Modifying
    @Query("update Issue i "
        + "set i.isClosed = :status "
        + "where i.id in :ids")
    void updateStatus(@Param("status") boolean status, @Param("ids") List<Long> ids);

    Page<Issue> findAllByIsClosedTrue(Pageable pageable);

    Page<Issue> findAllByIsClosedFalse(Pageable pageable);

    @Modifying
    @Query("update Issue i "
        + "set i.milestone = null, "
        + "i.lastModifiedAt = :now "
        + "where i.id in :ids")
    void removeMilestoneByIds(@Param("ids") List<Long> ids, @Param("now") LocalDateTime now);
}
