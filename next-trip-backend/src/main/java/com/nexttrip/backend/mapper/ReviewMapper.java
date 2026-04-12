package com.nexttrip.backend.mapper;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.nexttrip.backend.dto.request.ReviewRequest;
import com.nexttrip.backend.dto.response.ReviewResponse;
import com.nexttrip.backend.model.Review;

@Component
public class ReviewMapper {

	public ReviewResponse toResponse(Review review) {
		return ReviewResponse.builder()
				.id(review.getId())
				.destinationId(review.getDestinationId())
				.userId(review.getUserId())
				.authorName(null)
				.rating(review.getRating())
				.comment(review.getComment())
				.createdAt(review.getCreatedAt())
				.build();
	}

	public Review toEntity(ReviewRequest request, String destinationId, String userId) {
		return Review.builder()
				.destinationId(destinationId)
				.userId(userId)
				.rating(request.getRating())
				.comment(request.getComment())
				.createdAt(Instant.now())
				.build();
	}
}
