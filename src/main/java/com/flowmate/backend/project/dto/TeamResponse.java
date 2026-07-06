package com.flowmate.backend.project.dto;

public record TeamResponse(
		Integer id,
		String name,
		String description,
		int memberCount,
		int projectCount
) {
}
