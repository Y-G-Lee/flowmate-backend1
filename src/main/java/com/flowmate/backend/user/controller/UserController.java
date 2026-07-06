package com.flowmate.backend.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flowmate.backend.user.dto.UserSearchResponse;
import com.flowmate.backend.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/search")
	public ResponseEntity<List<UserSearchResponse>> searchUsers(@RequestParam String keyword) {
		return ResponseEntity.ok(userService.searchUsers(keyword));
	}
}
