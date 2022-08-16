package com.ahoo.issuetrackerserver.milestone.domain;

import com.ahoo.issuetrackerserver.common.BaseEntity;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Milestone extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "milestone_id")
	private Long id;

	@Column(nullable = false)
	private String title;

	private String description;
	private LocalDate dueDate;

	@Column(nullable = false)
	private boolean isClosed;

	public static Milestone of(String title, String description, LocalDate dueDate) {
		return new Milestone(
			null,
			title,
			description,
			dueDate,
			false
		);
	}

	public void update(String title, String description, LocalDate dueDate) {
		this.title = title;
		this.description = description;
		this.dueDate = dueDate;
	}

	public void toggleStatus() {
		this.isClosed = !this.isClosed;
	}
}
