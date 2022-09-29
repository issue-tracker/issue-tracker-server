package com.ahoo.issuetrackerserver.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "에러 응답")
@Getter
@AllArgsConstructor
public class ErrorResponse {

    @Schema(description = "에러 코드")
    private int errorCode;

    @Schema(description = "에러 메시지")
    private String message;
}
