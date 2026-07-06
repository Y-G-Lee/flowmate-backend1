package com.flowmate.backend.task.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.flowmate.backend.task.domain.Task;
import com.flowmate.backend.task.dto.TaskResponse;

@Mapper
public interface TaskMapper {

	List<TaskResponse> findTasksByProjectId(
			@Param("projectId") Integer projectId,
			@Param("userId") Integer userId
	);

	Optional<TaskResponse> findTaskByIdAndProjectId(
			@Param("taskId") Integer taskId,
			@Param("projectId") Integer projectId,
			@Param("userId") Integer userId
	);

	int insertTask(Task task);

	int updateTask(Task task);

	int deleteTask(
			@Param("taskId") Integer taskId,
			@Param("projectId") Integer projectId
	);
}
