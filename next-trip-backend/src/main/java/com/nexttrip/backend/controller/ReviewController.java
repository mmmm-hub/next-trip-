package com.nexttrip.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.request.ReviewRequest;
import com.nexttrip.backend.dto.response.ReviewResponse;
import com.nexttrip.backend.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Avis liés à une destination. Le {@code userId} est passé en query tant que l'authentification JWT n'est pas branchée.
 */
@RestController
@RequestMapping("/api/destinations/{destinationId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@GetMapping
	public ResponseEntity<List<ReviewResponse>> list(@PathVariable String destinationId) {
		return ResponseEntity.ok(reviewService.getReviewsByDestinationId(destinationId));
	}

	@PostMapping
	public ResponseEntity<ReviewResponse> add(
			@PathVariable String destinationId,
			@RequestParam(defaultValue = "guest-user") String userId,
			@Valid @RequestBody ReviewRequest request) {
		ReviewResponse created = reviewService.addReview(destinationId, userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
}
