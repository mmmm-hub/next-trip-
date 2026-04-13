package com.nexttrip.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nexttrip.backend.model.Booking;

public interface BookingRepository extends MongoRepository<Booking, String> {

	List<Booking> findByUserEmailOrderByCreatedAtDesc(String userEmail);
}
