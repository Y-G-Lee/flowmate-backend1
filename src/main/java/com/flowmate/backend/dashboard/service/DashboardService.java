package com.flowmate.backend.dashboard.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowmate.backend.common.auth.AuthUserResolver;
import com.flowmate.backend.dashboard.dto.DashboardResponse;
import com.flowmate.backend.dashboard.dto.DashboardSummaryResponse;
import com.flowmate.backend.dashboard.mapper.DashboardMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {

	private static final int RECENT_PROJECT_LIMIT = 5;
	private static final int RECENT_TASK_LIMIT = 10;

	private final DashboardMapper dashboardMapper;
	private final AuthUserResolver authUserResolver;

	@Transactional(readOnly = true)
	public DashboardResponse getDashboard() {
		Integer userId = authUserResolver.getCurrentUserId();

		DashboardSummaryResponse summary = new DashboardSummaryResponse(
				dashboardMapper.countActiveProjects(userId),
				dashboardMapper.countAssignedTasks(userId),
				dashboardMapper.countCompletedTasksThisWeek(userId)
		);

		return new DashboardResponse(
				summary,
				dashboardMapper.findRecentProjects(userId, RECENT_PROJECT_LIMIT),
				dashboardMapper.findRecentTasks(userId, RECENT_TASK_LIMIT)
		);
	}
}
