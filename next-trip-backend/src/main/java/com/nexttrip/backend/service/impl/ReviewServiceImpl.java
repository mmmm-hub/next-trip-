package com.nexttrip.backend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexttrip.backend.dto.request.ReviewRequest;
import com.nexttrip.backend.dto.response.ReviewResponse;
import com.nexttrip.backend.exception.ResourceNotFoundException;
import com.nexttrip.backend.mapper.ReviewMapper;
import com.nexttrip.backend.model.Review;
import com.nexttrip.backend.model.User;
import com.nexttrip.backend.repository.DestinationRepository;
import com.nexttrip.backend.repository.ReviewRepository;
import com.nexttrip.backend.repository.UserRepository;
import com.nexttrip.backend.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final DestinationRepository destinationRepository;
	private final UserRepository userRepository;
	private final ReviewMapper reviewMapper;

	@Override
	public List<ReviewResponse> getReviewsByDestinationId(String destinationId) {
		if (!destinationRepository.existsById(destinationId)) {
			throw new ResourceNotFoundException("Destination", destinationId);
		}
		return reviewRepository.findByDestinationIdOrderByCreatedAtDesc(destinationId).stream()
				.map(this::toResponseWithAuthor)
				.toList();
	}

	@Override
	public ReviewResponse addReview(String destinationId, String userId, ReviewRequest request) {
		if (!destinationRepository.existsById(destinationId)) {
			throw new ResourceNotFoundException("Destination", destinationId);
		}
		Review entity = reviewMapper.toEntity(request, destinationId, userId);
		Review saved = reviewRepository.save(entity);
		return toResponseWithAuthor(saved);
	}

	private ReviewResponse toResponseWithAuthor(Review review) {
		ReviewResponse response = reviewMapper.toResponse(review);
		String author = userRepository.findByEmailIgnoreCase(review.getUserId())
				.map(User::getFullName)
				.orElseGet(() -> userRepository.findById(review.getUserId()).map(User::getFullName).orElse(null));
		response.setAuthorName(author);
		return response;
	}
}
