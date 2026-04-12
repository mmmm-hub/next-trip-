package com.nexttrip.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nexttrip.backend.model.Review;

public interface ReviewRepository extends MongoRepository<Review, String> {

	List<Review> findByDestinationIdOrderByCreatedAtDesc(String destinationId);

	List<Review> findByUserId(String userId);

	long countByDestinationId(String destinationId);
}
