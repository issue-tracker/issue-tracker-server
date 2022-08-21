package com.ahoo.issuetrackerserver.issue.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "이슈 상태 변경 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueStatusUpdateRequest {

    @Schema(description = "변경할 이슈 상태")
    @NotNull
    private Boolean status;

    @Schema(description = "상태를 변경할 이슈 아이디 목록")
    @NotNull
    private List<Long> ids;
}
