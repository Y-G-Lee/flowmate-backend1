package com.flowmate.backend.project.member.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.flowmate.backend.project.member.dto.ProjectMemberAddRequest;
import com.flowmate.backend.project.member.dto.ProjectMemberResponse;
import com.flowmate.backend.project.member.service.ProjectMemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectMemberController {

	private final ProjectMemberService projectMemberService;

	@GetMapping("/api/projects/{projectId}/members")
	public ResponseEntity<List<ProjectMemberResponse>> getProjectMembers(@PathVariable Integer projectId) {
		return ResponseEntity.ok(projectMemberService.getProjectMembers(projectId));
	}

	@PostMapping("/api/projects/{projectId}/members")
	public ResponseEntity<ProjectMemberResponse> addProjectMember(
			@PathVariable Integer projectId,
			@Valid @RequestBody ProjectMemberAddRequest request
	) {
		ProjectMemberResponse response = projectMemberService.addProjectMember(projectId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("/api/projects/{projectId}/members/{userId}")
	public ResponseEntity<Void> removeProjectMember(
			@PathVariable Integer projectId,
			@PathVariable Integer userId
	) {
		projectMemberService.removeProjectMember(projectId, userId);
		return ResponseEntity.noContent().build();
	}
}
