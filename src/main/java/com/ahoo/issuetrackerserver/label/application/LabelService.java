package com.ahoo.issuetrackerserver.label.application;

import com.ahoo.issuetrackerserver.common.exception.DuplicatedLabelTitleException;
import com.ahoo.issuetrackerserver.common.exception.ErrorMessage;
import com.ahoo.issuetrackerserver.label.domain.Label;
import com.ahoo.issuetrackerserver.label.domain.TextBrightness;
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

    @Transactional
    public void save(String title, String colorCode, String description, TextBrightness textBrightness) {
        if (isExistsTitle(title)) {
            throw new DuplicatedLabelTitleException(ErrorMessage.DUPLICATED_LABEL_TITLE);
        }
        labelRepository.save(Label.of(title, colorCode, description, textBrightness));
    }

    private boolean isExistsTitle(String title) {
        return labelRepository.existsByTitle(title);
    }
}
