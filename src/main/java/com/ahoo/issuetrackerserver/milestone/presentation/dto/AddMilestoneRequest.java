package com.ahoo.issuetrackerserver.milestone.presentation.dto;

import com.ahoo.issuetrackerserver.common.exception.ErrorMessage;
import com.ahoo.issuetrackerserver.milestone.domain.Milestone;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddMilestoneRequest {

	@Schema(required = true)
	@NotBlank
	@Length(max = 255)
	private String title;

	@Length(max = 255)
	private String description;

	@Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = ErrorMessage.INVALID_DATE_FORMAT)
	private String dueDate;

	public Milestone toEntity() {
		return Milestone.of(
			title,
			description,
			LocalDate.parse(dueDate, DateTimeFormatter.ISO_LOCAL_DATE)
		);
	}
}
