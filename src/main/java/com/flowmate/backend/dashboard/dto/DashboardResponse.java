package com.flowmate.backend.dashboard.dto;

import java.util.List;

public record DashboardResponse(
		DashboardSummaryResponse summary,
		List<RecentProjectResponse> recentProjects,
		List<RecentTaskResponse> recentTasks
) {
}
