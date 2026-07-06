package com.flowmate.backend.task.domain;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Task {

	private Integer id;
	private Integer projectId;
	private String title;
	private String description;
	private String status;
	private String priority;
	private Integer assigneeId;
	private Integer createdBy;
	private LocalDate dueDate;
}
