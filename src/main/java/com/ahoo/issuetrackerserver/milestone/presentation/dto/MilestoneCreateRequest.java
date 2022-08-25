package com.ahoo.issuetrackerserver.milestone.presentation.dto;

import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Schema(description = "마일스톤 추가 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MilestoneCreateRequest {

    @Schema(required = true, description = "마일스톤 이름")
    @NotBlank
    @Length(max = 255)
    private String title;

    @Schema(description = "마일스톤 설명(선택)")
    @Length(max = 1000)
    private String description;

    @Schema(description = "마일스톤 완료일(선택) ex)YYYY-MM-DD")
    private LocalDate dueDate;

    public Milestone toEntity() {
        return Milestone.of(
            title,
            description,
            dueDate
        );
    }
}
