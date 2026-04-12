package com.nexttrip.backend.service;

import java.util.List;

import com.nexttrip.backend.dto.request.DestinationCreateRequest;
import com.nexttrip.backend.dto.request.DestinationUpdateRequest;
import com.nexttrip.backend.dto.response.DestinationDetailsResponse;
import com.nexttrip.backend.dto.response.DestinationResponse;

public interface DestinationService {

	List<DestinationResponse> getAllDestinations();

	DestinationDetailsResponse getDestinationById(String id);

	DestinationResponse createDestination(DestinationCreateRequest request);

	DestinationResponse updateDestination(String id, DestinationUpdateRequest request);

	void deleteDestination(String id);

	List<DestinationResponse> searchDestinations(
			String country,
			String continent,
			String climate,
			String ambiance,
			Double minPrice,
			Double maxPrice,
			String sortBy);
}
