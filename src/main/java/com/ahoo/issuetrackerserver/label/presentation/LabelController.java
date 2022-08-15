package com.ahoo.issuetrackerserver.label.presentation;

import com.ahoo.issuetrackerserver.common.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.label.application.LabelService;
import com.ahoo.issuetrackerserver.label.presentation.dto.LabelCreateRequest;
import com.ahoo.issuetrackerserver.label.presentation.dto.LabelResponse;
import com.ahoo.issuetrackerserver.label.presentation.dto.LabelUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "라벨 리스트 조회",
        description = "모든 라벨을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "라벨 리스트 조회 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = LabelResponse.class))
                    )
                })
        }
    )
    @GetMapping
    public List<LabelResponse> getLabels() {
        return labelService.findAll();
    }

    @Operation(summary = "라벨 등록",
        description = "라벨을 등록합니다.",
        responses = {
            @ApiResponse(responseCode = "204",
                description = "라벨 등록 성공"
            ),
            @ApiResponse(responseCode = "400",
                description = "라벨 등록 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )
        }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestBody LabelCreateRequest labelCreateRequest) {
        labelService.save(labelCreateRequest.getTitle(), labelCreateRequest.getBackgroundColorCode(),
            labelCreateRequest.getDescription(), labelCreateRequest.getTextBrightness());
    }

    @Operation(summary = "라벨 삭제",
        description = "라벨을 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "라벨 삭제 성공"
            ),
            @ApiResponse(responseCode = "400",
                description = "라벨 삭제 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )
        }
    )
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        labelService.deleteById(id);
    }

    @Operation(summary = "라벨 상세 조회",
        description = "라벨을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200",
                description = "라벨 조회 성공",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = LabelResponse.class)
                    )
                }),
            @ApiResponse(responseCode = "400",
                description = "라벨 조회 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )
        }
    )
    @GetMapping("/{id}")
    public LabelResponse getLabel(@PathVariable Long id) {
        return labelService.findById(id);
    }

    @Operation(summary = "라벨 수정",
        description = "라벨을 수정합니다.",
        responses = {
            @ApiResponse(responseCode = "204",
                description = "라벨 수정 성공"
            ),
            @ApiResponse(responseCode = "400",
                description = "라벨 수정 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )
        }
    )
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody LabelUpdateRequest labelUpdateRequest, @PathVariable Long id) {
        labelService.update(labelUpdateRequest, id);
    }

}
