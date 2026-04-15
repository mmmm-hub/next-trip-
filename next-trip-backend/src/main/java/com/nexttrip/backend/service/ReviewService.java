package com.nexttrip.backend.service;

import java.util.List;

import com.nexttrip.backend.dto.request.ReviewRequest;
import com.nexttrip.backend.dto.response.ReviewResponse;

public interface ReviewService {

	List<ReviewResponse> getReviewsByDestinationId(String destinationId);

	ReviewResponse addReview(String destinationId, String userId, ReviewRequest request);

	void deleteReview(String destinationId, String reviewId, String requesterEmail);
}
