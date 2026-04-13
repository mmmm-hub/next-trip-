package com.nexttrip.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.request.RecommendationRequest;
import com.nexttrip.backend.dto.response.RecommendationItemResponse;
import com.nexttrip.backend.security.SecurityUtils;
import com.nexttrip.backend.service.RecommendationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

	private final RecommendationService recommendationService;

	@GetMapping
	public ResponseEntity<List<RecommendationItemResponse>> getRecommendations(
			@RequestParam(required = false) Double minBudget,
			@RequestParam(required = false) Double maxBudget,
			@RequestParam(required = false) String climate,
			@RequestParam(required = false) String season,
			@RequestParam(required = false) List<String> ambiance,
			@RequestParam(required = false) Integer limit) {
		String userEmail = SecurityUtils.currentUserEmail();
		RecommendationRequest request = RecommendationRequest.builder()
				.minBudget(minBudget)
				.maxBudget(maxBudget)
				.climate(climate)
				.season(season)
				.ambianceTags(ambiance == null ? List.of() : ambiance)
				.limit(limit)
				.build();
		return ResponseEntity.ok(recommendationService.recommend(userEmail, request));
	}
}
