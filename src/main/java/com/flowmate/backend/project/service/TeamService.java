package com.flowmate.backend.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowmate.backend.common.auth.AuthUserResolver;
import com.flowmate.backend.common.exception.BadRequestException;
import com.flowmate.backend.common.exception.ForbiddenAccessException;
import com.flowmate.backend.common.exception.TeamNotFoundException;
import com.flowmate.backend.project.domain.Team;
import com.flowmate.backend.project.dto.TeamCreateRequest;
import com.flowmate.backend.project.dto.TeamDetailResponse;
import com.flowmate.backend.project.dto.TeamMemberResponse;
import com.flowmate.backend.project.dto.TeamProjectSummaryResponse;
import com.flowmate.backend.project.dto.TeamResponse;
import com.flowmate.backend.project.mapper.ProjectMapper;
import com.flowmate.backend.project.mapper.TeamMapper;
import com.flowmate.backend.project.member.service.ProjectMemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService {

	private static final String CREATOR_TEAM_ROLE = "OWNER";

	private final TeamMapper teamMapper;
	private final ProjectMapper projectMapper;
	private final ProjectMemberService projectMemberService;
	private final AuthUserResolver authUserResolver;

	@Transactional(readOnly = true)
	public List<TeamResponse> getMyTeams() {
		Integer userId = authUserResolver.getCurrentUserId();
		return teamMapper.findTeamsByUserId(userId);
	}

	@Transactional(readOnly = true)
	public TeamDetailResponse getTeamDetail(Integer teamId) {
		Integer userId = authUserResolver.getCurrentUserId();
		TeamResponse team = teamMapper.findTeamByIdAndUserId(teamId, userId)
				.orElseThrow(() -> new TeamNotFoundException(teamId));

		List<TeamMemberResponse> members = teamMapper.findMembersByTeamId(teamId);
		List<TeamProjectSummaryResponse> projects = teamMapper.findProjectsByTeamId(teamId);

		return new TeamDetailResponse(
				team.id(),
				team.name(),
				team.description(),
				team.memberCount(),
				team.projectCount(),
				members,
				projects
		);
	}

	@Transactional
	public TeamResponse createTeam(TeamCreateRequest request) {
		Integer userId = authUserResolver.getCurrentUserId();
		String name = request.name().trim();

		Integer existingTeamId = projectMapper.findTeamIdByUserAndTeamName(userId, name);
		if (existingTeamId != null && existingTeamId > 0) {
			throw new BadRequestException("이미 같은 이름의 팀이 있습니다.");
		}

		Team team = Team.builder()
				.name(name)
				.description(null)
				.createdBy(userId)
				.build();

		projectMapper.insertTeam(team);
		projectMapper.insertTeamMember(team.getId(), userId, CREATOR_TEAM_ROLE);

		return new TeamResponse(team.getId(), team.getName(), team.getDescription(), 1, 0);
	}

	@Transactional
	public void deleteTeam(Integer teamId) {
		Integer userId = authUserResolver.getCurrentUserId();
		teamMapper.findTeamByIdAndUserId(teamId, userId)
				.orElseThrow(() -> new TeamNotFoundException(teamId));

		String teamRole = teamMapper.findTeamRoleByTeamIdAndUserId(teamId, userId);
		if (teamRole == null || !CREATOR_TEAM_ROLE.equalsIgnoreCase(teamRole)) {
			throw new ForbiddenAccessException("팀 삭제는 팀장만 가능합니다.");
		}

		for (Integer projectId : teamMapper.findProjectIdsByTeamId(teamId)) {
			projectMapper.deleteTasksByProjectId(projectId);
			projectMemberService.deleteMembersByProjectId(projectId);
			projectMapper.deleteProject(projectId);
		}

		teamMapper.deleteTeamMembersByTeamId(teamId);

		int deleted = teamMapper.deleteTeam(teamId);
		if (deleted == 0) {
			throw new TeamNotFoundException(teamId);
		}
	}
}
