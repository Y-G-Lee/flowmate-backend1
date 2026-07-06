package com.flowmate.backend.dashboard.dto;

import java.time.LocalDate;

public record RecentTaskResponse(
		Long id,
		String title,
		String status,
		String assigneeName,
		String priority,
		LocalDate dueDate,
		Long projectId
) {
}
