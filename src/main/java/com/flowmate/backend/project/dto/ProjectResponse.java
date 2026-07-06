package com.flowmate.backend.project.dto;

import java.time.LocalDateTime;

public record ProjectResponse(
		Integer id,
		String name,
		String description,
		String status,
		Integer teamId,
		String teamName,
		int progress,
		int taskCount,
		int completedTaskCount,
		int memberCount,
		String deadline,
		LocalDateTime createdAt
) {
}
