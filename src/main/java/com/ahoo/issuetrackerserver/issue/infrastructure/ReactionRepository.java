package com.ahoo.issuetrackerserver.issue.infrastructure;

import com.ahoo.issuetrackerserver.issue.domain.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

}
