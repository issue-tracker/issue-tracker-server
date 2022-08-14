package com.ahoo.issuetrackerserver.label.infrastructure;

import com.ahoo.issuetrackerserver.label.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {

}
