package com.flowmate.backend.task.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.flowmate.backend.common.auth.AuthUserResolver;
import com.flowmate.backend.common.exception.ProjectNotFoundException;
import com.flowmate.backend.common.exception.TaskNotFoundException;
import com.flowmate.backend.project.mapper.ProjectMapper;
import com.flowmate.backend.task.domain.Task;
import com.flowmate.backend.task.dto.TaskCreateRequest;
import com.flowmate.backend.task.dto.TaskResponse;
import com.flowmate.backend.task.dto.TaskUpdateRequest;
import com.flowmate.backend.task.mapper.TaskMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

	private static final String DEFAULT_STATUS = "TODO";
	private static final String DEFAULT_PRIORITY = "MEDIUM";

	private final TaskMapper taskMapper;
	private final ProjectMapper projectMapper;
	private final AuthUserResolver authUserResolver;

	@Transactional(readOnly = true)
	public List<TaskResponse> getProjectTasks(Integer projectId) {
		Integer userId = authUserResolver.getCurrentUserId();
		verifyProjectAccess(projectId, userId);
		return taskMapper.findTasksByProjectId(projectId, userId);
	}

	@Transactional
	public TaskResponse createTask(Integer projectId, TaskCreateRequest request) {
		Integer userId = authUserResolver.getCurrentUserId();
		verifyProjectAccess(projectId, userId);

		Integer assigneeId = request.assigneeId() != null ? request.assigneeId() : userId;

		Task task = Task.builder()
				.projectId(projectId)
				.title(request.title().trim())
				.description(trimToNull(request.description()))
				.status(normalizeStatus(request.status()))
				.priority(normalizePriority(request.priority()))
				.assigneeId(assigneeId)
				.createdBy(userId)
				.dueDate(request.dueDate())
				.build();

		taskMapper.insertTask(task);

		return getTask(projectId, task.getId());
	}

	@Transactional
	public TaskResponse updateTask(Integer projectId, Integer taskId, TaskUpdateRequest request) {
		Integer userId = authUserResolver.getCurrentUserId();
		verifyProjectAccess(projectId, userId);

		TaskResponse existing = taskMapper.findTaskByIdAndProjectId(taskId, projectId, userId)
				.orElseThrow(() -> new TaskNotFoundException(taskId));

		Task task = Task.builder()
				.id(taskId)
				.projectId(projectId)
				.title(request.title() != null ? request.title().trim() : existing.title())
				.description(request.description() != null
						? trimToNull(request.description())
						: existing.description())
				.status(request.status() != null
						? normalizeStatus(request.status())
						: existing.status())
				.priority(request.priority() != null
						? normalizePriority(request.priority())
						: existing.priority())
				.assigneeId(request.assigneeId() != null ? request.assigneeId() : existing.assigneeId())
				.dueDate(request.dueDate() != null ? request.dueDate() : existing.dueDate())
				.build();

		int updated = taskMapper.updateTask(task);
		if (updated == 0) {
			throw new TaskNotFoundException(taskId);
		}

		return getTask(projectId, taskId);
	}

	@Transactional
	public void deleteTask(Integer projectId, Integer taskId) {
		Integer userId = authUserResolver.getCurrentUserId();
		verifyProjectAccess(projectId, userId);

		taskMapper.findTaskByIdAndProjectId(taskId, projectId, userId)
				.orElseThrow(() -> new TaskNotFoundException(taskId));

		int deleted = taskMapper.deleteTask(taskId, projectId);
		if (deleted == 0) {
			throw new TaskNotFoundException(taskId);
		}
	}

	private TaskResponse getTask(Integer projectId, Integer taskId) {
		Integer userId = authUserResolver.getCurrentUserId();
		return taskMapper.findTaskByIdAndProjectId(taskId, projectId, userId)
				.orElseThrow(() -> new TaskNotFoundException(taskId));
	}

	private void verifyProjectAccess(Integer projectId, Integer userId) {
		projectMapper.findProjectByIdAndUserId(projectId, userId)
				.orElseThrow(() -> new ProjectNotFoundException(projectId));
	}

	private String normalizeStatus(String status) {
		if (!StringUtils.hasText(status)) {
			return DEFAULT_STATUS;
		}
		return status.trim().toUpperCase().replace('-', '_');
	}

	private String normalizePriority(String priority) {
		if (!StringUtils.hasText(priority)) {
			return DEFAULT_PRIORITY;
		}
		return priority.trim().toUpperCase();
	}

	private String trimToNull(String value) {
		if (!StringUtils.hasText(value)) {
			return null;
		}
		return value.trim();
	}
}
