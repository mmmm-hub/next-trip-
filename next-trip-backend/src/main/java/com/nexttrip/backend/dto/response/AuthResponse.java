package com.nexttrip.backend.dto.response;

import com.nexttrip.backend.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

	private String token;
	private String email;
	private String fullName;
	private UserRole role;
}
