package com.nexttrip.backend.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nexttrip.backend.dto.request.UserPreferencesRequest;
import com.nexttrip.backend.dto.response.UserProfileResponse;
import com.nexttrip.backend.exception.ResourceNotFoundException;
import com.nexttrip.backend.model.User;
import com.nexttrip.backend.repository.UserRepository;
import com.nexttrip.backend.service.UserProfileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

	private final UserRepository userRepository;

	@Override
	public UserProfileResponse getProfile(String email) {
		User u = userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new ResourceNotFoundException("User", email));
		return toDto(u);
	}

	@Override
	public UserProfileResponse updatePreferences(String email, UserPreferencesRequest request) {
		User u = userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new ResourceNotFoundException("User", email));
		if (request.getPreferredClimateTags() != null) {
			u.setPreferredClimateTags(request.getPreferredClimateTags());
		}
		if (request.getPreferredAmbianceTags() != null) {
			u.setPreferredAmbianceTags(request.getPreferredAmbianceTags());
		}
		u.setBudgetMin(request.getBudgetMin());
		u.setBudgetMax(request.getBudgetMax());
		User saved = userRepository.save(u);
		return toDto(saved);
	}

	private static UserProfileResponse toDto(User u) {
		List<String> climates = u.getPreferredClimateTags() != null ? u.getPreferredClimateTags() : Collections.emptyList();
		List<String> ambiances = u.getPreferredAmbianceTags() != null ? u.getPreferredAmbianceTags() : Collections.emptyList();
		return UserProfileResponse.builder()
				.id(u.getId())
				.email(u.getEmail())
				.fullName(u.getFullName())
				.role(u.getRole())
				.preferredClimateTags(climates)
				.preferredAmbianceTags(ambiances)
				.budgetMin(u.getBudgetMin())
				.budgetMax(u.getBudgetMax())
				.build();
	}
}
