package com.ahoo.issuetrackerserver.milestone.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateMilestoneRequest {

	@Schema(required = true)
	@NotBlank
	@Length(max = 255)
	private String title;

	@Length(max = 255)
	private String description;

	private LocalDate dueDate;
}
