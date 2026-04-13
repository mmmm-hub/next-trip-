package com.nexttrip.backend.service;

import com.nexttrip.backend.dto.request.UserPreferencesRequest;
import com.nexttrip.backend.dto.response.UserProfileResponse;

public interface UserProfileService {

	UserProfileResponse getProfile(String email);

	UserProfileResponse updatePreferences(String email, UserPreferencesRequest request);
}
