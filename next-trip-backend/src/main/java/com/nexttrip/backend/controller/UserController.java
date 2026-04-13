package com.nexttrip.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.request.UserPreferencesRequest;
import com.nexttrip.backend.dto.response.UserProfileResponse;
import com.nexttrip.backend.security.SecurityUtils;
import com.nexttrip.backend.service.UserProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserProfileService userProfileService;

	@GetMapping("/me")
	public ResponseEntity<UserProfileResponse> me() {
		String email = SecurityUtils.currentUserEmail();
		if (email == null) {
			return ResponseEntity.status(401).build();
		}
		return ResponseEntity.ok(userProfileService.getProfile(email));
	}

	@PatchMapping("/preferences")
	public ResponseEntity<UserProfileResponse> prefs(@Valid @RequestBody UserPreferencesRequest request) {
		String email = SecurityUtils.currentUserEmail();
		if (email == null) {
			return ResponseEntity.status(401).build();
		}
		return ResponseEntity.ok(userProfileService.updatePreferences(email, request));
	}
}
