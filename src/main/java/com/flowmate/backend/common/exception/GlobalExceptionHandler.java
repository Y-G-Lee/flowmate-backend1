package com.flowmate.backend.common.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex) {
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(ErrorResponse.of(HttpStatus.CONFLICT.value(), ex.getMessage()));
	}

	@ExceptionHandler(InvalidLoginException.class)
	public ResponseEntity<ErrorResponse> handleInvalidLogin(InvalidLoginException ex) {
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(ErrorResponse.of(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
		return ResponseEntity
				.status(HttpStatus.UNAUTHORIZED)
				.body(ErrorResponse.of(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
	}

	@ExceptionHandler(ProjectNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProjectNotFound(ProjectNotFoundException ex) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(TeamNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleTeamNotFound(TeamNotFoundException ex) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(DuplicateProjectMemberException.class)
	public ResponseEntity<ErrorResponse> handleDuplicateProjectMember(DuplicateProjectMemberException ex) {
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(ErrorResponse.of(HttpStatus.CONFLICT.value(), ex.getMessage()));
	}

	@ExceptionHandler(ProjectMemberNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProjectMemberNotFound(ProjectMemberNotFoundException ex) {
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ErrorResponse.of(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
	}

	@ExceptionHandler(ForbiddenAccessException.class)
	public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenAccessException ex) {
		return ResponseEntity
				.status(HttpStatus.FORBIDDEN)
				.body(ErrorResponse.of(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
		List<ErrorResponse.FieldError> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(this::toFieldError)
				.toList();

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "입력값이 올바르지 않습니다.", errors));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
		List<ErrorResponse.FieldError> errors = ex.getConstraintViolations()
				.stream()
				.map(this::toFieldError)
				.toList();

		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), "입력값이 올바르지 않습니다.", errors));
	}

	private ErrorResponse.FieldError toFieldError(FieldError fieldError) {
		return new ErrorResponse.FieldError(fieldError.getField(), fieldError.getDefaultMessage());
	}

	private ErrorResponse.FieldError toFieldError(ConstraintViolation<?> violation) {
		String field = violation.getPropertyPath().toString();
		int dotIndex = field.lastIndexOf('.');
		if (dotIndex >= 0) {
			field = field.substring(dotIndex + 1);
		}
		return new ErrorResponse.FieldError(field, violation.getMessage());
	}
}
