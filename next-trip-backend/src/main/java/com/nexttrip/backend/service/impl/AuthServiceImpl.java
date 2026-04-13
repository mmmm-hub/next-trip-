package com.nexttrip.backend.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nexttrip.backend.dto.request.LoginRequest;
import com.nexttrip.backend.dto.request.RegisterRequest;
import com.nexttrip.backend.dto.response.AuthResponse;
import com.nexttrip.backend.exception.BadRequestException;
import com.nexttrip.backend.model.User;
import com.nexttrip.backend.model.UserRole;
import com.nexttrip.backend.repository.UserRepository;
import com.nexttrip.backend.security.JwtService;
import com.nexttrip.backend.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	@Override
	public AuthResponse register(RegisterRequest request) {
		if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
			throw new BadRequestException("Email already registered");
		}
		User user = User.builder()
				.fullName(request.getFullName())
				.email(request.getEmail().trim().toLowerCase())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(UserRole.USER)
				.build();
		User saved = userRepository.save(user);
		return buildResponse(saved);
	}

	@Override
	public AuthResponse login(LoginRequest request) {
		String email = request.getEmail().trim().toLowerCase();
		User user = userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new BadRequestException("Invalid email or password"));
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BadRequestException("Invalid email or password");
		}
		return buildResponse(user);
	}

	private AuthResponse buildResponse(User user) {
		String token = jwtService.generateToken(user.getEmail(), user.getRole());
		return AuthResponse.builder()
				.token(token)
				.email(user.getEmail())
				.fullName(user.getFullName())
				.role(user.getRole())
				.build();
	}
}
