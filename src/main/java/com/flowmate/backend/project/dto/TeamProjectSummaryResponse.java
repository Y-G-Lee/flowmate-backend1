package com.flowmate.backend.project.dto;

public record TeamProjectSummaryResponse(
		Integer id,
		String name,
		String status,
		int progress
) {
}
