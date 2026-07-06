package com.flowmate.backend.project.dto;

import java.util.List;

public record TeamDetailResponse(
		Integer id,
		String name,
		String description,
		int memberCount,
		int projectCount,
		List<TeamMemberResponse> members,
		List<TeamProjectSummaryResponse> projects
) {
}
