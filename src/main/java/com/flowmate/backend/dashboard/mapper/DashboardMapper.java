package com.flowmate.backend.dashboard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.flowmate.backend.dashboard.dto.RecentProjectResponse;
import com.flowmate.backend.dashboard.dto.RecentTaskResponse;

@Mapper
public interface DashboardMapper {

	int countActiveProjects(@Param("userId") Integer userId);

	int countAssignedTasks(@Param("userId") Integer userId);

	int countCompletedTasksThisWeek(@Param("userId") Integer userId);

	List<RecentProjectResponse> findRecentProjects(
			@Param("userId") Integer userId,
			@Param("limit") int limit
	);

	List<RecentTaskResponse> findRecentTasks(
			@Param("userId") Integer userId,
			@Param("limit") int limit
	);
}
