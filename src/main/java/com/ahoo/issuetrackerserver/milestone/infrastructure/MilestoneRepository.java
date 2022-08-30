package com.ahoo.issuetrackerserver.milestone.infrastructure;

import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

    @Query("select distinct m "
        + "from Milestone m "
        + "left outer join fetch m.issues ")
    List<Milestone> findAllFetchJoin();
}
