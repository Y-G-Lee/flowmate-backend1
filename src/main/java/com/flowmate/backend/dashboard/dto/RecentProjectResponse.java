package com.flowmate.backend.dashboard.dto;

import java.time.LocalDateTime;

public record RecentProjectResponse(
		Long id,
		String name,
		String description,
		int progress,
		String status,
		LocalDateTime createdAt
) {
}
