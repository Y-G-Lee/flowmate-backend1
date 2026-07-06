package com.flowmate.backend.common.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.flowmate.backend.common.exception.UnauthorizedException;

@Component
public class AuthUserResolver {

	public AuthUserPrincipal getCurrentUser() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !(authentication.getPrincipal() instanceof AuthUserPrincipal principal)) {
			throw new UnauthorizedException();
		}

		return principal;
	}

	public Integer getCurrentUserId() {
		return getCurrentUser().userId();
	}
}
