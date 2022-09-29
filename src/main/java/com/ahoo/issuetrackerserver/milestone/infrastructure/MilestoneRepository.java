package com.ahoo.issuetrackerserver.milestone.infrastructure;

import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

    @Query("select distinct m "
        + "from Milestone m "
        + "left outer join fetch m.issues ")
    List<Milestone> findAllFetchJoin();

    @Query("select m "
        + "from Milestone m "
        + "left outer join fetch m.issues "
        + "where m.id = :id")
    Optional<Milestone> findByIdFetchJoin(@Param("id") Long id);

    boolean existsByTitle(String title);
}
