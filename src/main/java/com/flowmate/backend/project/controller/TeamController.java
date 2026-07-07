package com.flowmate.backend.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowmate.backend.project.dto.TeamCreateRequest;
import com.flowmate.backend.project.dto.TeamDetailResponse;
import com.flowmate.backend.project.dto.TeamMemberAddRequest;
import com.flowmate.backend.project.dto.TeamMemberResponse;
import com.flowmate.backend.project.dto.TeamResponse;
import com.flowmate.backend.project.service.TeamService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

	private final TeamService teamService;

	@GetMapping
	public ResponseEntity<List<TeamResponse>> getMyTeams() {
		return ResponseEntity.ok(teamService.getMyTeams());
	}

	@GetMapping("/{teamId}")
	public ResponseEntity<TeamDetailResponse> getTeamDetail(@PathVariable Integer teamId) {
		return ResponseEntity.ok(teamService.getTeamDetail(teamId));
	}

	@PostMapping
	public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody TeamCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(request));
	}

	@DeleteMapping("/{teamId}")
	public ResponseEntity<Void> deleteTeam(@PathVariable Integer teamId) {
		teamService.deleteTeam(teamId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{teamId}/members")
	public ResponseEntity<TeamMemberResponse> addTeamMember(
			@PathVariable Integer teamId,
			@Valid @RequestBody TeamMemberAddRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(teamService.addTeamMember(teamId, request));
	}
}
