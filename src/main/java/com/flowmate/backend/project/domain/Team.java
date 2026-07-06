package com.flowmate.backend.project.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Team {

	private Integer id;
	private String name;
	private String description;
	private Integer createdBy;
}
