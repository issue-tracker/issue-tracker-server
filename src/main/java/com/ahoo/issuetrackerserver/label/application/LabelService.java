package com.ahoo.issuetrackerserver.label.application;

import com.ahoo.issuetrackerserver.label.infrastructure.LabelRepository;
import com.ahoo.issuetrackerserver.label.presentation.dto.LabelsResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;

    @Transactional(readOnly = true)
    public List<LabelsResponse> findAll() {
        return labelRepository.findAll().stream()
            .map(LabelsResponse::from)
            .collect(Collectors.toUnmodifiableList());
    }
}
