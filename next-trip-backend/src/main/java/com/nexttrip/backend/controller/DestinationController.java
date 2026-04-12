package com.nexttrip.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexttrip.backend.dto.request.DestinationCreateRequest;
import com.nexttrip.backend.dto.request.DestinationUpdateRequest;
import com.nexttrip.backend.dto.response.DestinationDetailsResponse;
import com.nexttrip.backend.dto.response.DestinationResponse;
import com.nexttrip.backend.service.DestinationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
public class DestinationController {

	private final DestinationService destinationService;

	@GetMapping
	public ResponseEntity<List<DestinationResponse>> getAll() {
		return ResponseEntity.ok(destinationService.getAllDestinations());
	}

	@GetMapping("/{id}")
	public ResponseEntity<DestinationDetailsResponse> getById(@PathVariable String id) {
		return ResponseEntity.ok(destinationService.getDestinationById(id));
	}

	@PostMapping
	public ResponseEntity<DestinationResponse> create(@Valid @RequestBody DestinationCreateRequest request) {
		DestinationResponse created = destinationService.createDestination(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DestinationResponse> update(
			@PathVariable String id,
			@Valid @RequestBody DestinationUpdateRequest request) {
		return ResponseEntity.ok(destinationService.updateDestination(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		destinationService.deleteDestination(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	public ResponseEntity<List<DestinationResponse>> search(
			@RequestParam(required = false) String country,
			@RequestParam(required = false) String continent,
			@RequestParam(required = false) String climate,
			@RequestParam(required = false) String ambiance,
			@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) String sortBy) {
		return ResponseEntity.ok(destinationService.searchDestinations(
				country, continent, climate, ambiance, minPrice, maxPrice, sortBy));
	}
}
