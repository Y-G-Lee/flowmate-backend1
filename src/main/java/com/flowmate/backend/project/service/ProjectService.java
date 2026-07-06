package com.flowmate.backend.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.flowmate.backend.common.auth.AuthUserResolver;
import com.flowmate.backend.common.exception.BadRequestException;
import com.flowmate.backend.common.exception.ForbiddenAccessException;
import com.flowmate.backend.common.exception.ProjectNotFoundException;
import com.flowmate.backend.project.domain.Project;
import com.flowmate.backend.project.domain.Team;
import com.flowmate.backend.project.dto.ProjectCreateRequest;
import com.flowmate.backend.project.dto.ProjectResponse;
import com.flowmate.backend.project.dto.ProjectUpdateRequest;
import com.flowmate.backend.project.member.service.ProjectMemberService;
import com.flowmate.backend.project.mapper.ProjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

	private static final String DEFAULT_STATUS = "PLANNING";
	private static final String DEFAULT_TEAM_ROLE = "MEMBER";

	private final ProjectMapper projectMapper;
	private final ProjectMemberService projectMemberService;
	private final AuthUserResolver authUserResolver;

	@Transactional
	public ProjectResponse createProject(ProjectCreateRequest request) {
		Integer userId = authUserResolver.getCurrentUserId();
		Integer teamId = resolveTeamId(request, userId);
		String status = normalizeStatus(request.status());

		Project project = Project.builder()
				.teamId(teamId)
				.name(request.name().trim())
				.description(trimToNull(request.description()))
				.status(status)
				.deadline(trimToNull(request.deadline()))
				.createdBy(userId)
				.build();

		projectMapper.insertProject(project);
		projectMemberService.addOwnerOnProjectCreate(project.getId(), userId);

		return getProject(project.getId());
	}

	@Transactional(readOnly = true)
	public java.util.List<ProjectResponse> getMyProjects() {
		Integer userId = authUserResolver.getCurrentUserId();
		return projectMapper.findProjectsByUserId(userId);
	}

	@Transactional(readOnly = true)
	public ProjectResponse getProject(Integer projectId) {
		Integer userId = authUserResolver.getCurrentUserId();

		return projectMapper.findProjectByIdAndUserId(projectId, userId)
				.orElseThrow(() -> new ProjectNotFoundException(projectId));
	}

	@Transactional
	public ProjectResponse updateProject(Integer projectId, ProjectUpdateRequest request) {
		Integer userId = authUserResolver.getCurrentUserId();
		ProjectResponse existing = projectMapper.findProjectByIdAndUserId(projectId, userId)
				.orElseThrow(() -> new ProjectNotFoundException(projectId));

		Project project = Project.builder()
				.id(projectId)
				.teamId(existing.teamId())
				.name(request.name().trim())
				.description(trimToNull(request.description()))
				.status(normalizeStatus(request.status()))
				.deadline(trimToNull(request.deadline()))
				.build();

		int updated = projectMapper.updateProject(project);
		if (updated == 0) {
			throw new ProjectNotFoundException(projectId);
		}

		return getProject(projectId);
	}

	@Transactional
	public void deleteProject(Integer projectId) {
		Integer userId = authUserResolver.getCurrentUserId();
		projectMapper.findProjectByIdAndUserId(projectId, userId)
				.orElseThrow(() -> new ProjectNotFoundException(projectId));

		projectMapper.deleteTasksByProjectId(projectId);
		projectMemberService.deleteMembersByProjectId(projectId);

		int deleted = projectMapper.deleteProject(projectId);
		if (deleted == 0) {
			throw new ProjectNotFoundException(projectId);
		}
	}

	private Integer resolveTeamId(ProjectCreateRequest request, Integer userId) {
		if (request.teamId() != null) {
			verifyTeamMembership(request.teamId(), userId);
			return request.teamId();
		}

		if (StringUtils.hasText(request.teamName())) {
			String teamName = request.teamName().trim();
			Integer existingTeamId = projectMapper.findTeamIdByUserAndTeamName(userId, teamName);

			if (existingTeamId != null && existingTeamId > 0) {
				return existingTeamId;
			}

			return createTeamWithMember(teamName, userId);
		}

		throw new BadRequestException("teamId 또는 teamName 중 하나는 필수입니다.");
	}

	private Integer createTeamWithMember(String teamName, Integer userId) {
		Team team = Team.builder()
				.name(teamName)
				.description(teamName + " 팀")
				.createdBy(userId)
				.build();

		projectMapper.insertTeam(team);
		projectMapper.insertTeamMember(team.getId(), userId, DEFAULT_TEAM_ROLE);

		return team.getId();
	}

	private void verifyTeamMembership(Integer teamId, Integer userId) {
		if (!projectMapper.existsTeamMember(teamId, userId)) {
			throw new ForbiddenAccessException("해당 팀에 접근할 권한이 없습니다.");
		}
	}

	private String normalizeStatus(String status) {
		if (!StringUtils.hasText(status)) {
			return DEFAULT_STATUS;
		}

		return status.trim().toUpperCase().replace('-', '_');
	}

	private String trimToNull(String value) {
		if (!StringUtils.hasText(value)) {
			return null;
		}
		return value.trim();
	}
}
