package com.ahoo.issuetrackerserver.upload.presentation;

import com.ahoo.issuetrackerserver.common.exception.ErrorResponse;
import com.ahoo.issuetrackerserver.upload.application.S3UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3UploadController {

    private final S3UploadService s3UploadService;

    @Operation(summary = "파일 등록",
        description = "파일을 S3 등록합니다.",
        responses = {
            @ApiResponse(responseCode = "201",
                description = "파일 등록 성공"
            ),
            @ApiResponse(responseCode = "400",
                description = "파일 등록 실패",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class)
                    )
                }
            )}
    )
    @PostMapping("/api/images/upload")
    public String upload(@RequestPart MultipartFile file) {
        return s3UploadService.upload(file);
    }
}
