package com.flowmate.backend.dashboard.dto;

public record DashboardSummaryResponse(
		int activeProjectCount,
		int assignedTaskCount,
		int completedThisWeekCount
) {
}
