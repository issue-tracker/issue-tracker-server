package com.ahoo.issuetrackerserver.label.application;

import com.ahoo.issuetrackerserver.common.exception.ApplicationException;
import com.ahoo.issuetrackerserver.common.exception.DuplicatedLabelTitleException;
import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import com.ahoo.issuetrackerserver.label.domain.Label;
import com.ahoo.issuetrackerserver.label.domain.TextColor;
import com.ahoo.issuetrackerserver.label.infrastructure.LabelRepository;
import com.ahoo.issuetrackerserver.label.presentation.dto.LabelResponse;
import com.ahoo.issuetrackerserver.label.presentation.dto.LabelUpdateRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;

    @Transactional(readOnly = true)
    public List<LabelResponse> findAll() {
        return labelRepository.findAll().stream()
            .map(LabelResponse::from)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public LabelResponse save(String title, String colorCode, String description, TextColor textColor) {
        if (isExistsTitle(title)) {
            throw new DuplicatedLabelTitleException(ErrorType.DUPLICATED_LABEL_TITLE);
        }
        Label savedLabel = labelRepository.save(Label.of(title, colorCode, description, textColor));

        return LabelResponse.from(savedLabel);
    }

    private boolean isExistsTitle(String title) {
        return labelRepository.existsByTitle(title);
    }

    @Transactional
    public void deleteById(Long id) {
        try {
            labelRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ApplicationException(ErrorType.NOT_EXISTS_LABEL, new NoSuchElementException());
        }

    }

    @Transactional(readOnly = true)
    public LabelResponse findById(Long id) {
        Label findLabel = labelRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_LABEL, new NoSuchElementException()));

        return LabelResponse.from(findLabel);
    }

    @Transactional
    public LabelResponse update(LabelUpdateRequest labelUpdateRequest, Long id) {
        Label findLabel = labelRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(ErrorType.NOT_EXISTS_LABEL, new NoSuchElementException()));

        if (isExistsTitle(labelUpdateRequest.getTitle())) {
            throw new DuplicatedLabelTitleException(ErrorType.DUPLICATED_LABEL_TITLE);
        }

        findLabel.update(labelUpdateRequest.getTitle(), labelUpdateRequest.getBackgroundColorCode(),
            labelUpdateRequest.getDescription(), labelUpdateRequest.getTextColor());

        return LabelResponse.from(findLabel);
    }

}
