package com.flowmate.backend.user.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flowmate.backend.user.dto.EmailCheckResponse;
import com.flowmate.backend.user.dto.LoginRequest;
import com.flowmate.backend.user.dto.LoginResponse;
import com.flowmate.backend.user.dto.SignUpRequest;
import com.flowmate.backend.user.dto.SignUpResponse;
import com.flowmate.backend.user.service.AuthService;
import com.flowmate.backend.user.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

	private final UserService userService;
	private final AuthService authService;

	@GetMapping("/email/check")
	public ResponseEntity<EmailCheckResponse> checkEmail(
			@RequestParam @NotBlank(message = "이메일은 필수입니다.")
			@Email(message = "올바른 이메일 형식이 아닙니다.") String email) {
		return ResponseEntity.ok(userService.checkEmailAvailability(email));
	}

	@PostMapping("/signup")
	public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
		SignUpResponse response = userService.signUp(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		AuthService.LoginResult result = authService.login(request);
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, result.cookie().toString())
				.body(result.response());
	}
}
