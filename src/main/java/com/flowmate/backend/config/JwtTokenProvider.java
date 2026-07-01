package com.flowmate.backend.config;

import java.util.Date;
import java.util.HexFormat;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final JwtProperties jwtProperties;
	private final SecretKey secretKey;

	public JwtTokenProvider(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.secretKey = Keys.hmacShaKeyFor(HexFormat.of().parseHex(jwtProperties.secret()));
	}

	public String createToken(Integer userId, String email) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtProperties.expirationMilliseconds());

		return Jwts.builder()
				.issuer(jwtProperties.iss())
				.subject(jwtProperties.sub())
				.claim("userId", userId)
				.claim("email", email)
				.issuedAt(now)
				.expiration(expiry)
				.signWith(secretKey)
				.compact();
	}
}
