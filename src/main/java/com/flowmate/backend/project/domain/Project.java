package com.flowmate.backend.project.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Project {

	private Integer id;
	private Integer teamId;
	private String name;
	private String description;
	private String status;
	private String deadline;
	private Integer createdBy;
}
