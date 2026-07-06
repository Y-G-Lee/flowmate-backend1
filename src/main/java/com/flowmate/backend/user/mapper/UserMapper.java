package com.flowmate.backend.user.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.flowmate.backend.user.domain.User;

@Mapper
public interface UserMapper {

	Optional<User> findByEmail(@Param("email") String email);

	Optional<User> findById(@Param("id") Integer id);

	java.util.List<com.flowmate.backend.user.dto.UserSearchResponse> searchByKeyword(@Param("keyword") String keyword);

	int insert(User user);
}
