package com.nexttrip.backend.service;

import com.nexttrip.backend.dto.request.LoginRequest;
import com.nexttrip.backend.dto.request.RegisterRequest;
import com.nexttrip.backend.dto.response.AuthResponse;

public interface AuthService {

	AuthResponse register(RegisterRequest request);

	AuthResponse login(LoginRequest request);
}
