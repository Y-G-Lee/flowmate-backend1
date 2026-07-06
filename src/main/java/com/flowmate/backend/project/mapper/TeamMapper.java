package com.flowmate.backend.project.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.flowmate.backend.project.dto.TeamMemberResponse;
import com.flowmate.backend.project.dto.TeamProjectSummaryResponse;
import com.flowmate.backend.project.dto.TeamResponse;

@Mapper
public interface TeamMapper {

	List<TeamResponse> findTeamsByUserId(@Param("userId") Integer userId);

	Optional<TeamResponse> findTeamByIdAndUserId(
			@Param("teamId") Integer teamId,
			@Param("userId") Integer userId
	);

	String findTeamRoleByTeamIdAndUserId(
			@Param("teamId") Integer teamId,
			@Param("userId") Integer userId
	);

	List<TeamMemberResponse> findMembersByTeamId(@Param("teamId") Integer teamId);

	List<TeamProjectSummaryResponse> findProjectsByTeamId(@Param("teamId") Integer teamId);

	List<TeamProjectSummaryResponse> findActiveProjectsByTeamId(@Param("teamId") Integer teamId);

	List<Integer> findProjectIdsByTeamId(@Param("teamId") Integer teamId);

	int deleteTeamMembersByTeamId(@Param("teamId") Integer teamId);

	int deleteTeam(@Param("teamId") Integer teamId);
}
