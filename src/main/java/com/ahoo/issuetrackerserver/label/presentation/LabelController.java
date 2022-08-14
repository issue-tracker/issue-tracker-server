package com.ahoo.issuetrackerserver.label.presentation;

import com.ahoo.issuetrackerserver.label.application.LabelService;
import com.ahoo.issuetrackerserver.label.presentation.dto.LabelCreateRequest;
import com.ahoo.issuetrackerserver.label.presentation.dto.LabelsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService labelService;

    //TODO
    // 라벨 조회 기능 구현
    // 라벨 수정 기능 구현
    // 라벨 삭제 기능 구현

    @Operation(summary = "라벨 리스트 조회",
        description = "모든 라벨을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "라벨 리스트 조회 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = LabelsResponse.class))
                    )
                })
        }
    )
    @GetMapping
    public List<LabelsResponse> getLabels() {
        return labelService.findAll();
    }

    @Operation(summary = "라벨 등록",
        description = "라벨을 등록합니다.",
        responses = {
            @ApiResponse(responseCode = "204",
                description = "라벨 등록 성공"
            ),
            @ApiResponse(responseCode = "400",
                description = "라벨 등록 실패"
            )
        }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestBody LabelCreateRequest labelCreateRequest) {
        labelService.save(labelCreateRequest.getTitle(), labelCreateRequest.getBackgroundColorCode(),
            labelCreateRequest.getDescription(), labelCreateRequest.getTextBrightness());
    }

}
