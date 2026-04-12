package com.nexttrip.backend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nexttrip.backend.dto.request.CompareRequest;
import com.nexttrip.backend.dto.response.CompareResponse;
import com.nexttrip.backend.dto.response.DestinationDetailsResponse;
import com.nexttrip.backend.exception.BadRequestException;
import com.nexttrip.backend.exception.ResourceNotFoundException;
import com.nexttrip.backend.mapper.DestinationMapper;
import com.nexttrip.backend.model.Destination;
import com.nexttrip.backend.repository.DestinationRepository;
import com.nexttrip.backend.service.CompareService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompareServiceImpl implements CompareService {

	private static final int MAX_COMPARE = 3;

	private final DestinationRepository destinationRepository;
	private final DestinationMapper destinationMapper;

	@Override
	public CompareResponse compareDestinations(CompareRequest request) {
		List<String> ids = request.getDestinationIds();
		if (ids == null || ids.size() < 2) {
			throw new BadRequestException("At least two destination ids are required");
		}
		int limit = Math.min(ids.size(), MAX_COMPARE);
		List<DestinationDetailsResponse> details = new ArrayList<>();
		for (int i = 0; i < limit; i++) {
			String id = ids.get(i);
			Destination dest = destinationRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Destination", id));
			DestinationDetailsResponse d = destinationMapper.toDetailsResponse(dest);
			details.add(d);
		}
		return CompareResponse.builder().destinations(details).build();
	}
}
