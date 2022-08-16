package com.ahoo.issuetrackerserver.milestone.infrastructure;

import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

	List<Milestone> findAllByIsClosed(boolean isClosed);
}
