package com.nexttrip.backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nexttrip.backend.model.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${nexttrip.jwt.secret}")
	private String secret;

	@Value("${nexttrip.jwt.expiration-ms:86400000}")
	private long expirationMs;

	public String generateToken(String email, UserRole role) {
		Date now = new Date();
		Date exp = new Date(now.getTime() + expirationMs);
		return Jwts.builder()
				.subject(email)
				.claim("role", role.name())
				.issuedAt(now)
				.expiration(exp)
				.signWith(signingKey())
				.compact();
	}

	public boolean isValid(String token, UserDetails userDetails) {
		String email = extractEmail(token);
		return email.equals(userDetails.getUsername()) && !isExpired(token);
	}

	public String extractEmail(String token) {
		return parseClaims(token).getSubject();
	}

	public UserRole extractRole(String token) {
		String r = parseClaims(token).get("role", String.class);
		return r != null ? UserRole.valueOf(r) : UserRole.USER;
	}

	private boolean isExpired(String token) {
		return parseClaims(token).getExpiration().before(new Date());
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
				.verifyWith(signingKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public UserDetails toUserDetails(String token) {
		String email = extractEmail(token);
		UserRole role = extractRole(token);
		String springRole = "ROLE_" + role.name();
		return User.builder()
				.username(email)
				.password("")
				.authorities(List.of(new SimpleGrantedAuthority(springRole)))
				.build();
	}

	private SecretKey signingKey() {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
