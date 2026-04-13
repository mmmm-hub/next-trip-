package com.nexttrip.backend.service;

import java.util.List;

import com.nexttrip.backend.dto.request.RecommendationRequest;
import com.nexttrip.backend.dto.response.RecommendationItemResponse;

public interface RecommendationService {

	List<RecommendationItemResponse> recommend(String userEmail, RecommendationRequest request);
}
