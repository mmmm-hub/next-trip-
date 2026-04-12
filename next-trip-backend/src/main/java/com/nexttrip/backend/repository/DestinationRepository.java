package com.nexttrip.backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nexttrip.backend.model.Destination;

public interface DestinationRepository extends MongoRepository<Destination, String> {

	List<Destination> findByCountryIgnoreCase(String country);

	List<Destination> findByContinentIgnoreCase(String continent);

	List<Destination> findByPromotedTrue();
}
