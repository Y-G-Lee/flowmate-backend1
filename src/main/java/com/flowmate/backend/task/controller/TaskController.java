package com.flowmate.backend.task.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flowmate.backend.task.dto.TaskCreateRequest;
import com.flowmate.backend.task.dto.TaskResponse;
import com.flowmate.backend.task.dto.TaskUpdateRequest;
import com.flowmate.backend.task.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskController {

	private final TaskService taskService;

	@GetMapping("/api/projects/{projectId}/tasks")
	public ResponseEntity<List<TaskResponse>> getProjectTasks(@PathVariable Integer projectId) {
		return ResponseEntity.ok(taskService.getProjectTasks(projectId));
	}

	@PostMapping("/api/projects/{projectId}/tasks")
	public ResponseEntity<TaskResponse> createTask(
			@PathVariable Integer projectId,
			@Valid @RequestBody TaskCreateRequest request
	) {
		TaskResponse response = taskService.createTask(projectId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/api/projects/{projectId}/tasks/{taskId}")
	public ResponseEntity<TaskResponse> updateTask(
			@PathVariable Integer projectId,
			@PathVariable Integer taskId,
			@Valid @RequestBody TaskUpdateRequest request
	) {
		return ResponseEntity.ok(taskService.updateTask(projectId, taskId, request));
	}

	@DeleteMapping("/api/projects/{projectId}/tasks/{taskId}")
	public ResponseEntity<Void> deleteTask(
			@PathVariable Integer projectId,
			@PathVariable Integer taskId
	) {
		taskService.deleteTask(projectId, taskId);
		return ResponseEntity.noContent().build();
	}
}
